/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package cn.vlabs.umt.services.user.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.duckling.vmt.api.domain.VmtOrgDomain;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.bean.OrgDomain;
import cn.vlabs.umt.services.user.dao.IDomainDAO;

public class DomainDAOImpl implements IDomainDAO {
	private static final Logger LOG = Logger.getLogger(DomainDAOImpl.class);
	private DatabaseUtil du;

	public DomainDAOImpl(DatabaseUtil du) {
		this.du = du;
	}

	@Override
	public List<OrgDomain> getAllByDetail() {
		String sql = "select o.*,group_concat(d.org_domain) domains from umt_org o,umt_org_domain d where o.id=d.org_id group by o.org_symbol order by o.id";
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			rs = ps.executeQuery();
			List<OrgDomain> result = new ArrayList<OrgDomain>();
			while (rs.next()) {
				result.add(readDomain(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}
	@Override
	public void insertOrgDomain(List<VmtOrgDomain> domains) {
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		PreparedStatement psD=null;
		ResultSet rs = null;
		
		try {
			ps = conn.prepareStatement("insert into `umt_org` values(?,?,?,?,?,?)");
			psD=conn.prepareStatement("insert into `umt_org_domain` values(?,?,?)");
			
			ps.execute("delete from umt_org");
			ps.execute("delete from umt_org_domain");
			conn.setAutoCommit(false);
			int id=0;
			int domainId=0;
			for(VmtOrgDomain domain:domains){
				int pIndex=0;
				ps.setInt(++pIndex, ++id);
				ps.setString(++pIndex, domain.getOrgSymbol());
				ps.setString(++pIndex, domain.getOrgName());
				ps.setBoolean(++pIndex, domain.isCas());
				ps.setBoolean(++pIndex, domain.isCoreMail());
				ps.setInt(++pIndex, domain.getType());
				ps.addBatch();
				
				for(String d:domain.getDomains()){
					int pdoIndex=0;
					psD.setInt(++pdoIndex, ++domainId);
					psD.setInt(++pdoIndex, id);
					psD.setString(++pdoIndex, d);
					psD.addBatch();
				}
			}
			ps.executeBatch();
			psD.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			if(psD!=null){
				try {
					psD.close();
				} catch (SQLException e) {
					LOG.error("",e);
				}
			}
			DatabaseUtil.closeAll(rs, ps, conn);
			
		}
	}
	private OrgDomain readDomain(ResultSet rs) throws SQLException{
		OrgDomain od = new OrgDomain();
		od.setId(rs.getInt("id"));
		od.setCas(rs.getBoolean("is_cas"));
		od.setCoreMail(rs.getBoolean("is_coremail"));
		od.setDomain(CommonUtils.killNull(rs.getString("domains")).split(","));
		od.setOrgName(rs.getString("org_name"));
		od.setOrgSymbol(rs.getString("org_symbol"));
		od.setType(rs.getInt("type"));
		return od;
	}
	@Override
	public OrgDomain findDetailByDomain(String domain) {
		if(domain==null){
			return null;
		}
		String sql = "select o.*,group_concat(d.org_domain) domains from umt_org o,umt_org_domain d where d.org_domain=? and o.id=d.org_id group by o.org_symbol order by o.id";
		Connection conn = du.getConnection();
		PreparedStatement ps=null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(sql);
			ps.setString(1, domain);
			rs = ps.executeQuery();
			if (rs.next()) {
				return readDomain(rs);
			}
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, ps, conn);
		}
		return null;
	}

}

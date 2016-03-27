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

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.dao.ILdapDAO;

public class LdapDAOImpl implements ILdapDAO{
	private static final Logger LOG=Logger.getLogger(LdapDAOImpl.class);
	private static final String TABLE=" `umt_app_ldap` ";
	private static final String INSERT="insert into"+TABLE+"(`rdn`,`clientName`,`description`,`applicant`,`company`,`contactInfo`,`appStatus`,`uid`,`priv`,`userName`,`userCstnetId`,`ldapPassword`,`pubScope`,`type`) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String UPDATE="update "+TABLE+" set `clientName`=?,`description`=?,`applicant`=?,`company`=?,`contactInfo`=?,`priv`=?,`appStatus`=?,`pubScope`=? where `id`=? ";
	private static final String DELETE="delete from "+TABLE+" where 1=1 ";
	private static final String COUNT="select count(*) c from "+TABLE+" where 1=1 ";
	private static final String SELECT="select * from "+TABLE+" where 1=1";
	private static final String BY_RDN=" and `rdn`=?";
	private static final String BY_UID=" and `uid`=?";
	private static final String BY_ID=" and `id`=?";
	private static final String BY_APP_STATUS=" and `appStatus`=?";
	private static final String BY_PRIV=" and `priv`!=? ";
	private DatabaseUtil du;
	public LdapDAOImpl(DatabaseUtil du) {
		this.du = du;
	}
	@Override
	public void addLdapApp(LdapBean bean) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(INSERT);
			int index=0;
			st.setString(++index, bean.getRdn());
			st.setString(++index, bean.getClientName());
			st.setString(++index, bean.getDescription());
			st.setString(++index, bean.getApplicant());
			st.setString(++index, bean.getCompany());
			st.setString(++index, bean.getContactInfo());
			st.setString(++index, bean.getAppStatus());
			st.setInt(++index, bean.getUid());
			st.setString(++index, bean.getPriv());
			st.setString(++index, bean.getUserName());
			st.setString(++index, bean.getUserCstnetId());
			st.setString(++index, bean.getLdapPassword());
			st.setString(++index, bean.getPubScope());
			st.setString(++index, bean.getType());
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	private LdapBean read(ResultSet rs) throws SQLException{
		LdapBean bean = new LdapBean();
		bean.setApplicant(rs.getString("applicant"));
		bean.setAppStatus(rs.getString("appStatus"));
		bean.setClientName(rs.getString("clientName"));
		bean.setCompany(rs.getString("company"));
		bean.setContactInfo(rs.getString("contactInfo"));
		bean.setDescription(rs.getString("description"));
		bean.setId(rs.getInt("id"));
		bean.setRdn(rs.getString("rdn"));
		bean.setUid(rs.getInt("uid"));
		bean.setUserName(rs.getString("userName"));
		bean.setUserCstnetId(rs.getString("userCstnetId"));
		bean.setPriv(rs.getString("priv"));
		bean.setCreateTime(rs.getTimestamp("createTime"));
		bean.setLdapPassword(rs.getString("ldapPassword"));
		bean.setPubScope(rs.getString("pubScope"));
		bean.setType(rs.getString("type"));
		return bean;
	}
	@Override
	public boolean isRdnUsed(String rdn) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(COUNT+BY_RDN);
			int index=0;
			st.setString(++index, rdn);
			rs=st.executeQuery();
			if(rs.next()){
				return rs.getInt("c")>0;
			}
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return true;
	}
	@Override
	public List<LdapBean> searchMyWifiApp(int uid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(SELECT+" and type='wifi' "+BY_UID);
			int index=0;
			st.setInt(++index, uid);
			rs=st.executeQuery();
			List<LdapBean> result=new ArrayList<LdapBean>();
			while(rs.next()){
				result.add(read(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	@Override
	public List<LdapBean> searchMyLdapApp(int uid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(SELECT+" and type='ldap' "+BY_UID);
			int index=0;
			st.setInt(++index, uid);
			rs=st.executeQuery();
			List<LdapBean> result=new ArrayList<LdapBean>();
			while(rs.next()){
				result.add(read(rs));
			}
			return result;
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	@Override
	public LdapBean getLdapBeanById(int id) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(SELECT+BY_ID);
			int index=0;
			st.setInt(++index, id);
			rs=st.executeQuery();
			if(rs.next()){
				return read(rs);
			}
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	@Override
	public void updateLdapApp(LdapBean bean) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(UPDATE);
			int index=0;
			st.setString(++index, bean.getClientName());
			st.setString(++index, bean.getDescription());
			st.setString(++index, bean.getApplicant());
			st.setString(++index, bean.getCompany());
			st.setString(++index, bean.getContactInfo());
			st.setString(++index, bean.getPriv());
			st.setString(++index, bean.getAppStatus());
			st.setString(++index, bean.getPubScope());
			st.setInt(++index,bean.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	@Override
	public List<LdapBean> findEnableAndAccepted(String viewType) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(SELECT+BY_APP_STATUS+BY_PRIV+ " and type=?");
			int index=0;
			st.setString(++index,LdapBean.APP_STATUS_ACCEPT);
			st.setString(++index, LdapBean.PRIV_CLOSED);
			st.setString(++index, viewType);
			rs=st.executeQuery();
			List<LdapBean> list=new ArrayList<LdapBean>();
			while(rs.next()){
				list.add(read(rs));
			}
			return list;
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	@Override
	public List<LdapBean> findAllApp() {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(SELECT);
			rs=st.executeQuery();
			List<LdapBean> lbs=new ArrayList<LdapBean>();
			while(rs.next()){
				lbs.add(read(rs));
			}
			return lbs;
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	@Override
	public void removeLapApp(int beanId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(DELETE+BY_ID);
			st.setInt(1, beanId);
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	@Override
	public void updateLdapAppPasswd(LdapBean lb) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement("update "+TABLE+" set ldapPassword=? where 1=1 "+BY_ID);
			int index=0;
			st.setString(++index,lb.getLdapPassword());
			st.setInt(++index, lb.getId());
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		
	}
	@Override
	public List<LdapBean> findAvailableWifi(String scope) {
		List<LdapBean> list=new ArrayList<LdapBean>();
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(SELECT+BY_APP_STATUS+BY_PRIV+ " and type='wifi'  and (pubScope=? or pubScope='' or pubScope is null)");
			int index=0;
			st.setString(++index,LdapBean.APP_STATUS_ACCEPT);
			st.setString(++index, LdapBean.PRIV_CLOSED);
			st.setString(++index, scope);
			rs=st.executeQuery();
			while(rs.next()){
				list.add(read(rs));
			}
		} catch (SQLException e) {
			LOG.error("",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return list;
	}
}

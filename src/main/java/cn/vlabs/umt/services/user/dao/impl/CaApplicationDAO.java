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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.user.bean.CaApplication;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.dao.ICaApplicationDAO;

public class CaApplicationDAO implements ICaApplicationDAO {
	public CaApplicationDAO(DatabaseUtil du){
		this.du=du;
	}
	
	@Override
	public int createCaApplication(CaApplication ca) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(CREATE_SQL);
			int index=0;
			st.setInt(++index, ca.getUid());
			st.setString(++index, ca.getDn());
			st.setString(++index,ca.getCn());
			st.setInt(++index, ca.getType());
			st.setTimestamp(++index,new Timestamp(ca.getValiFrom().getTime()));
			st.setTimestamp(++index,new Timestamp(ca.getExpirationOn().getTime()));
			st.setString(++index, ca.getPassword());
			st.setInt(++index,ca.getStatus());
			st.execute();
			rs = st.getGeneratedKeys();
			if (rs.next()){
				return rs.getInt(1);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("infomation:",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return -1;
	}
	
	
	private static final Logger LOGGER = Logger.getLogger(CaApplicationDAO.class);
	private DatabaseUtil du;
	private static final String TABLE="umt_ca";
	private static final String CREATE_SQL="insert into "+TABLE+"(`uid`, `dn`,`cn`,`type`,`valiFrom`,`expirationOn`,`password`,`status`) values(?,?,?,?,?,?,?,?)";
	private static final String REMOVE_SQL="update `"+TABLE+"` set `status`='"+CaApplication.STATUS_DELETED+"' where 1=1 ";
	private static final String SELECT_SQL="select * from `"+TABLE+"` where 1=1 and `status`!='"+CaApplication.STATUS_DELETED+"'";
	private static final String BY_UID=" and `uid`=? "; 
	private static final String BY_TYPE=" and `type`=? "; 
	private static final String BY_ID=" and `id`=? ";
	private static final String BY_STATUS=" and `status`=? ";
	private static final String ORDER_BY_VALIFROM_DESC=" order by `valiFrom` desc";
	
	@Override
	public void removeCaApplication(int CaApplicationid) {
		Connection conn = du.getConnection();
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(REMOVE_SQL+BY_ID);
			int index=0;
			st.setInt(++index, CaApplicationid);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(null, st, conn);
		}
		
	}

	@Override
	public CaApplication getCaApplication(int caApplicationid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(SELECT_SQL+BY_ID);
			int index=0;
			st.setInt(++index, caApplicationid);
			rs = st.executeQuery();
			if (rs.next()){
				return readCaApplication(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public List<CaApplication> getCaApplicationByUidAndType(int uid, int type, int status) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			String sql=SELECT_SQL;
			if(uid>0){
				sql+=BY_UID;
			}
			if(type>0){
				sql+=BY_TYPE;
			}
			if(status>0){
				sql+=BY_STATUS;
			}
			sql+=ORDER_BY_VALIFROM_DESC;
			st = conn.prepareStatement(sql);
			int index=0;
			if(uid>0){
				st.setInt(++index, uid);
			}
			if(type>0){
				st.setInt(++index, type);
			}
			if(status>0){
				st.setInt(++index, status);
			}
			rs = st.executeQuery();
			List<CaApplication> cas=new ArrayList<CaApplication>();
			while(rs.next()){
				cas.add(readCaApplication(rs));
			}
			return cas;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	
	private CaApplication readCaApplication(ResultSet rs)throws SQLException{
		CaApplication ca = new CaApplication();
		ca.setId(rs.getInt("id"));
		ca.setUid(rs.getInt("uid"));
		ca.setDn(rs.getString("dn"));
		ca.setCn(rs.getString("cn"));
		ca.setType(rs.getInt("type"));
		ca.setValiFrom(rs.getTimestamp("valiFrom"));
		ca.setExpirationOn(rs.getTimestamp("expirationOn"));
		ca.setPassword(rs.getString("password"));
		ca.setStatus(rs.getInt("status"));
		return ca;
	}
	
}
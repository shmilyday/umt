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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.dao.IUserLoginNameDAO;

/**
 * @author lvly
 * @since 2013-2-27
 */
public class UserLoginNameDAO implements IUserLoginNameDAO{
	private DatabaseUtil dbUtil;
	public UserLoginNameDAO(DatabaseUtil dbUtil) {
		this.dbUtil = dbUtil;
	}
	private static final Logger LOGGER=Logger.getLogger(UserLoginNameDAO.class);
	private static final String TABLE_NAME=" `umt_login_name` ";
	private static final String INSERT_SQL="insert into "+TABLE_NAME+"(`uid`,`login_name`,`type`,`status`) values(?,?,?,?) ";
	private static final String DELETE_SQL="delete from "+TABLE_NAME+" where 1=1 ";
	private static final String UPDATE_LOGIN_NAME_SQL="update "+TABLE_NAME+" set `login_name`=? where 1=1 ";
	private static final String UPDATE_TO_LOGINNAME_SQL="update "+TABLE_NAME+" set `tmp_login_name`=? where 1=1"; 
	private static final String UPDATE_ACTIVE="update "+TABLE_NAME+" set `status`=? where 1=1";
	
	private static final String SELECT_COUNT_SQL="select count(*) c from "+TABLE_NAME+" where 1=1";
	private static final String SELECT_USER_SQL="select u.* from `umt_user` u,"+TABLE_NAME+" l where l.`uid`=u.`id` ";
	private static final String SELECT_BASE="select * from "+TABLE_NAME+" where 1=1 ";
	private static final String BY_ID=" and `id`=? ";
	private static final String BY_UID=" and `uid`=? ";
	private static final String BY_TYPE=" and `type`=? ";
	private static final String BY_LOGIN_NAME=" and `login_name`=?";
	private static final String BY_STATUS=" and `status`=? ";
	
	private static final String LIMIT_1=" limit 0,1 ";
	
	@Override
	public void removeLoginNamesByUid(int[] uids) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		Statement st = null;
		StringBuffer uidsStr=new StringBuffer();
		for(int uid:uids){
			uidsStr.append(uid).append(",");
		}
		try {
			st = conn.createStatement();
			String sql=DELETE_SQL+" and `uid` in("+CommonUtils.format(uidsStr.toString())+")";
			st.execute(sql);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		
	}
	@Override
	public void toActive(int loginInfoId) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(UPDATE_ACTIVE+BY_ID);
			int index=0;
			st.setString(++index, LoginNameInfo.STATUS_ACTIVE);
			st.setInt(++index, loginInfoId);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		
	}
	@Override
	public LoginNameInfo getALoginNameInfo(int uid, String loginName) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT_BASE+BY_UID+BY_LOGIN_NAME+LIMIT_1);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, loginName.toLowerCase());
			rs=st.executeQuery();
			if(rs.next()){
				return readLoginInfo(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	@Override
	public void removeSameTmpEmail(String loginName) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE_SQL+BY_LOGIN_NAME+BY_STATUS);
			int index=0;
			st.setString(++index, loginName.toLowerCase());
			st.setString(++index, LoginNameInfo.STATUS_TEMP);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	@Override
	public void toActive(int uid, String loginName, String type) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(UPDATE_ACTIVE+BY_UID+BY_LOGIN_NAME+BY_TYPE);
			int index=0;
			st.setString(++index, LoginNameInfo.STATUS_ACTIVE);
			st.setInt(++index, uid);
			st.setString(++index, loginName.toLowerCase());
			st.setString(++index, type);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		
	}
	@Override
	public int createLoginName(String loginName, int uid, String type,String status) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(INSERT_SQL);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, loginName.toLowerCase());
			st.setString(++index, type);
			st.setString(++index, status);
			st.execute();
			rs = st.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return -1;
	}
	@Override
	public void removeSecondaryLoginName(int uid, String email) {
		Connection conn = dbUtil.getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE_SQL+BY_UID+BY_TYPE+BY_LOGIN_NAME);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, LoginNameInfo.LOGINNAME_TYPE_SECONDARY);
			st.setString(++index,email.toLowerCase());
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(null, st, conn);
		}
	}
	@Override
	public void removeLoginName(int loginNameId) {
		Connection conn = dbUtil.getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE_SQL+BY_ID);
			int index=0;
			st.setInt(++index, loginNameId);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(null, st, conn);
		}
	}
	@Override
	public boolean isUsedByMe(int uid, String loginName) {
		Connection conn = dbUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs=null;
		try {
			st = conn.prepareStatement(SELECT_BASE+" and `uid`=? and (`login_name`=? or `tmp_login_name`=?)");
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, loginName);
			st.setString(++index, loginName);
			rs=st.executeQuery();
			if(rs.next()){
				return true;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
			return true;
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return false;
	}
	@Override
	public boolean updateLoginName(int uid,String oldLoginName,String newloginName) {
		Connection conn = dbUtil.getConnection();
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(UPDATE_LOGIN_NAME_SQL+BY_UID+BY_LOGIN_NAME);
			int index=0;
			st.setString(++index, newloginName.toLowerCase());
			st.setInt(++index, uid);
			st.setString(++index, oldLoginName.toLowerCase());
			return st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(null, st, conn);
		}
		return false;
	}
	@Override
	public User getUserByLoginName(String loginName) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try{
			st=conn.prepareStatement(SELECT_USER_SQL+BY_LOGIN_NAME+" order by `create_time` desc limit 0,1");
			int index=0;
			st.setString(++index, loginName.toLowerCase());
			rs=st.executeQuery();
			if(rs.next()){
				return readUser(rs);
			}
		}catch(SQLException e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(null, st, conn);
		}
		return null;
	}
	private User readUser(ResultSet rs){
		User user=null;
		try {
			user=new User();
			user.setId(rs.getInt("id"));
			user.setCstnetId(rs.getString("cstnet_id"));
			user.setPassword(rs.getString("password"));
			String secondaryEmails=rs.getString("secondary_email");
			if(!CommonUtils.isNull(secondaryEmails)){
				user.setSecondaryEmails(secondaryEmails.split(";"));
			}
			user.setSecurityEmail(rs.getString("security_email"));
			user.setTrueName(rs.getString("true_name"));
			user.setUmtId(rs.getString("umt_id"));
			user.setType(rs.getString("type"));
			user.setAccountStatus(rs.getString("account_status"));
			user.setSendGEOEmailSwitch(rs.getBoolean("send_geo_email_switch"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}
		
		return user;
	}
	@Override
	public boolean isUsed(String loginName) {
		Connection conn = dbUtil.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try{
			st=conn.prepareStatement(SELECT_COUNT_SQL+BY_LOGIN_NAME+BY_STATUS);
			int index=0;
			st.setString(++index, loginName.toLowerCase());
			st.setString(++index, LoginNameInfo.STATUS_ACTIVE);
			rs=st.executeQuery();
			if(rs.next()){
				return rs.getInt("c")>0;
			}
		}catch(SQLException e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return false;
	}
	private String buildSetString(int size){
		boolean first=true;
		StringBuffer buffer = new StringBuffer();
		buffer.append("select login_name from umt_login_name where `status`='"+LoginNameInfo.STATUS_ACTIVE+"' and `login_name` in (");
		for (int i=0;i<size;i++){
				if (first){
					buffer.append("?");
					first=false;
				}else{
					buffer.append(",?");
				}
		}
		buffer.append(")");
		return buffer.toString();
	}
	@Override
	public String[] isUsed(String[] loginNames) {
		ArrayList<String> usernames= new ArrayList<String>();
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(buildSetString(loginNames.length));
			int index=0;
			for(String loginName:loginNames){
				st.setString(++index, loginName.toLowerCase());
			}
			rs = st.executeQuery();
			while (rs.next()){
				usernames.add(rs.getString(1));
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return toStringArray(usernames.toArray());
	}
	private String[] toStringArray(Object[] objs){
		String[] result=new String[objs.length];
		int index=0;
		for(Object obj:objs){
			result[index++]=obj.toString();
		}
		return result;
		
	}
	@Override
	public List<String> getLoginNameInfos(int uid, String type,String status) {
		ArrayList<String> result= new ArrayList<String>();
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT_BASE+BY_UID+BY_TYPE+BY_STATUS);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, type);
			st.setString(++index, status);
			rs = st.executeQuery();
			while (rs.next()){
				result.add(rs.getString("login_name"));
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return result;
	}
	@Override
	public LoginNameInfo getLoginNameInfoById(int loginNameInfoId) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT_BASE+BY_ID);
			int index=0;
			st.setInt(++index, loginNameInfoId);
			rs=st.executeQuery();
			if(rs.next()){
				return readLoginInfo(rs);
			}
			
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	@Override
	public void updateToLoginName(int uid, String oldLoginName, String newLoginName) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(UPDATE_TO_LOGINNAME_SQL+BY_UID+BY_LOGIN_NAME);
			int index=0;
			st.setString(++index, CommonUtils.killNull(newLoginName).toLowerCase());
			st.setInt(++index, uid);
			st.setString(++index, oldLoginName.toLowerCase());
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	@Override
	public List<LoginNameInfo> getLoginNameInfo(int uid, String type) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		List<LoginNameInfo> result=new ArrayList<LoginNameInfo>();
		try {
			st = conn.prepareStatement(SELECT_BASE+BY_UID+BY_TYPE);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, type);
			rs=st.executeQuery();
			while(rs.next()){
				result.add(readLoginInfo(rs));
			}
			return result;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return result;
	}
	/**
	 * @param rs
	 * @return
	 */
	private LoginNameInfo readLoginInfo(ResultSet rs) {
		LoginNameInfo info=new LoginNameInfo();
		try {
			info.setId(rs.getInt("id"));
			info.setLoginName(rs.getString("login_name"));
			info.setTmpLoginName(rs.getString("tmp_login_name"));
			info.setType(rs.getString("type"));
			info.setUid(rs.getInt("uid"));
			info.setStatus(rs.getString("status"));
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return info;
	}
	@Override
	public int getLoginNameId(int uid, String loginName, String type) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT_BASE+BY_UID+BY_LOGIN_NAME+BY_TYPE);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, loginName.toLowerCase());
			st.setString(++index, type);
			rs=st.executeQuery();
			if(rs.next()){
				return rs.getInt("id");
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return -1;
	}	
	@Override
	public void removeLdapLoginName(int uid) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE_SQL+BY_UID+BY_TYPE);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, LoginNameInfo.LOGINNAME_TYPE_LDAP);
			st.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}	
	@Override
	public List<LoginNameInfo> getLoginNameInfos(List<String> uids,
			String type) {
		Connection conn = dbUtil.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			
			StringBuffer sb=new StringBuffer(SELECT_BASE+BY_TYPE);
			sb.append(" and uid in(");
			for(int i=0;i<uids.size();i++){
				sb.append("?,");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
			st = conn.prepareStatement(sb.toString());
			int index=0;
			st.setString(++index, type);
			for(String uid:uids){
				st.setString(++index, uid);
			}
			rs=st.executeQuery();
			List<LoginNameInfo> result=new ArrayList<LoginNameInfo>();
			while(rs.next()){
				result.add(readLoginInfo(rs));
			}
			return result;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	

}
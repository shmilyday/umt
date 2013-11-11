/*
 * Copyright (c) 2008-2013 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
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
package cn.vlabs.umt.services.session.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.session.LoginRecordVO;
import cn.vlabs.umt.services.session.SessionDAO;

public class SessionDAOImpl implements SessionDAO {
	public SessionDAOImpl(DatabaseUtil du){
		this.du=du;
	}
	public void refreshSession(String sessionid, String appname, Date lastUpdate) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(REFRESH_LOGIN_RECORD);
			pst.setTimestamp(1, new Timestamp(lastUpdate.getTime()));
			pst.setString(2, sessionid);
			pst.setString(3, appname);
			pst.execute();
		} catch (SQLException e) {
			log.error("CreateGlobalSession:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}

	public void removeAllSession(String username, String userip) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(REMOVE_ALL_SESSION);
			pst.setString(1, username);
			pst.setString(2, userip);
			pst.execute();
		} catch (SQLException e) {
			log.error("RemoveAllLoginRecord:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	public void removeSession(String username, String appname, String sessionid) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(REMOVE_ONE_SESSION);
			pst.setString(1, username);
			pst.setString(2, appname);
			pst.setString(3, sessionid);
			pst.execute();
		} catch (SQLException e) {
			log.error("removeSession:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	
	public Collection<LoginRecord> getAllAppSession(String username, String userip) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<LoginRecord> records = new ArrayList<LoginRecord>();
		try{
			pst = conn.prepareStatement(GET_ALL_INHOST_SESSION);
			pst.setString(1, username);
			pst.setString(2, userip);
			rs = pst.executeQuery();
			while (rs.next()){
				LoginRecord record = readRecord(rs);
				records.add(record);
			}
		} catch (SQLException e) {
			log.error("getAllAppSession:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return records;
	}
	
	public Collection<LoginRecordVO> getAllAppSession(String username) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<LoginRecordVO> records = new ArrayList<LoginRecordVO>();
		try{
			pst = conn.prepareStatement(GET_ALL_SESSION);
			pst.setString(1, username);
			rs = pst.executeQuery();
			while (rs.next()){
				LoginRecordVO record = new LoginRecordVO();
				record.setId(rs.getInt("id"));
				record.setAppname(rs.getString("appname"));
				record.setAppdesc(rs.getString("description"));
				record.setLogintime(rs.getTimestamp("logintime"));
				record.setUserip(rs.getString("userip"));
				record.setAppurl(rs.getString("url"));
				records.add(record);
			}
		} catch (SQLException e) {
			log.error("getAllAppSession:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return records;
	}
	public void createAppSession(String username, String appname, String userip,
			String logouturl, String appType, Date lastupdate,
			String appsessionid) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(INSERT_LOGINRECORD);
			pst.setString(1, username);
			pst.setString(2, appname);
			pst.setString(3, appsessionid);
			pst.setString(4, logouturl);
			pst.setString(5, appType);
			Timestamp logintime= new Timestamp(lastupdate.getTime());
			pst.setTimestamp(6,logintime);
			pst.setString(7, userip);
			pst.setTimestamp(8,logintime);
			pst.execute();
		} catch (SQLException e) {
			log.error("记录登录信息时发生错误:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	public void removeTimeOutSession(Date time) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(REMOVE_TIME_OUT);
			pst.setTimestamp(1, new Timestamp(time.getTime()));
			pst.execute();
		} catch (SQLException e) {
			log.error("RemoveTimeOutSession:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	
	public LoginRecord getLoginRecord(int id) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(GET_BY_ID);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next()){
				return readRecord(rs);
			}
		} catch (SQLException e) {
			log.error("getAllAppSession:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
	public void removeSession(int id) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(REMOVE_BY_ID);
			pst.setInt(1, id);
			pst.execute();
		} catch (SQLException e) {
			log.error("getAllAppSession:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	
	private LoginRecord readRecord(ResultSet rs) throws SQLException {
		LoginRecord record = new LoginRecord();
		record.setId(rs.getInt("id"));
		record.setAppname(rs.getString("appname"));
		record.setAppSessionid(rs.getString("sessionid"));
		record.setLastupdate(rs.getTimestamp("lastupdate"));
		record.setLogoutURL(rs.getString("logouturl"));
		record.setAppType(rs.getString("appType"));
		return record;
	}
	private String REMOVE_BY_ID="delete from umt_session where id=?";
	private String GET_BY_ID="select * from umt_session where id=?";
	private String GET_ALL_SESSION="select umt_session.id,  description, logintime,url,  umt_session.appname appname, logintime, userip from umt_application, umt_session where umt_session.appname=umt_application.name and username=?";
	private String GET_ALL_INHOST_SESSION="select * from umt_session where username=? and userip=?";
	private String REMOVE_TIME_OUT="delete from umt_session where lastupdate<?";
	private String REMOVE_ONE_SESSION="delete from umt_session where username=? and appname=? and sessionid=?";
	private String REMOVE_ALL_SESSION="delete from umt_session where username=? and userip=?";
	private String REFRESH_LOGIN_RECORD="update umt_session set lastupdate=? where sessionid=? and appname=?";
	private String INSERT_LOGINRECORD="insert into umt_session(username, appname, sessionid, logouturl, appType, lastupdate, userip, logintime) values(?,?,?,?,?,?,?,?)";
	private DatabaseUtil du;
	private Logger log = Logger.getLogger(SessionDAOImpl.class);
}
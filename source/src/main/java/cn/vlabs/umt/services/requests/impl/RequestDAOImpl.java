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
package cn.vlabs.umt.services.requests.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.requests.RequestDAO;
import cn.vlabs.umt.services.requests.UserRequest;

public class RequestDAOImpl implements RequestDAO {
	public RequestDAOImpl(DatabaseUtil du){
		this.du=du;
	}
	
	public int createRequest(UserRequest request) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(createSQL);
			int index=0;
			st.setString(++index, request.getUsername());
			st.setString(++index, request.getTruename());
			st.setString(++index, request.getEmail());
			st.setTimestamp(++index, new Timestamp(request.getCreateTime().getTime()));
			st.setString(++index, request.getPhonenumber());
			st.setString(++index, request.getOrgnization());
			st.setString(++index, request.getPassword());
			st.setInt(++index, request.getState());
			st.execute();
			rs = st.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			log.error(e.getMessage());
			log.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return 0;
	}
	
	public int getRequestCount(){
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(countAllSQL);
			rs = st.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			log.error(e.getMessage());
			log.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return 0;
	}
	
	public int getRequestCount(int state) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(countStateSQL);
			st.setInt(1, state);
			rs = st.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			log.error(e.getMessage());
			log.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return 0;
	}

	public Collection<UserRequest> getRequests(int state, int start, int count) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<UserRequest> requests= new ArrayList<UserRequest>();
		
		try {
			st = conn.prepareStatement(selectStateSQL);
			int index=0;
			st.setInt(++index, state);
			st.setInt(++index, start);
			st.setInt(++index, count);
			rs = st.executeQuery();
			while (rs.next()){
				requests.add(readUserRequest(rs));
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			log.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return requests;
	}
	
	public Collection<UserRequest> getRequests(int start, int count) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<UserRequest> requests= new ArrayList<UserRequest>();
		
		try {
			st = conn.prepareStatement(selectAllSQL);
			st.setInt(1, start);
			st.setInt(2, count);
			rs = st.executeQuery();
			while (rs.next()){
				requests.add(readUserRequest(rs));
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			log.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return requests;
	}
	
	private UserRequest readUserRequest(ResultSet rs) throws SQLException {
		UserRequest request= new UserRequest();
		request.setId(rs.getInt("id"));
		request.setUsername(rs.getString("username"));
		request.setTruename(rs.getString("trueName"));
		request.setEmail(rs.getString("email"));
		request.setPassword(rs.getString("password"));
		request.setState(rs.getInt("state"));
		request.setOrgnization(rs.getString("orgnization"));
		request.setPhonenumber(rs.getString("phoneNumber"));
		request.setCreateTime(rs.getTimestamp("createTime"));
		request.setOperator(rs.getString("operator"));
		return request;
	}

	public void removeRequest(int rid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(removeSQL);
			st.setInt(1, rid);
			st.execute();
		} catch (SQLException e) {
			log.error(e.getMessage());
			log.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	public void updateState(int rid, int state, String operator) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(updateStateSQL);
			st.setInt(1, state);
			st.setString(2, operator);
			st.setInt(3, rid);
			st.execute();
		} catch (SQLException e) {
			log.error(e.getMessage());
			log.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	public UserRequest getRequest(int rid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(selectSQL);
			st.setInt(1, rid);
			rs = st.executeQuery();
			if (rs.next()){
				return readUserRequest(rs);
			}
		} catch (SQLException e) {
			log.error(e.getMessage());
			log.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	private String selectSQL="select * from umt_requests where id=?";
	private String createSQL="insert into umt_requests(username, trueName, email, createTime, phoneNumber, orgnization, password, state)" +
			"values(?,?,?,?,?,?,?,?)";
	private String selectAllSQL="select * from umt_requests order by createTime desc limit ?, ?";
	private String selectStateSQL="select * from umt_requests where state=? order by createTime desc limit ?, ?";
	private String countAllSQL="select count(*) from umt_requests";
	private String countStateSQL="select count(*) from umt_requests where state=?";
	private String removeSQL="delete from umt_requests where id=?";
	private String updateStateSQL="update umt_requests set state=?, operator=? where id=?";
	private Logger log =Logger.getLogger(RequestDAOImpl.class);
	private DatabaseUtil du;

}
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
package cn.vlabs.umt.services.user.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.mysql.jdbc.Statement;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.user.bean.OauthToken;
import cn.vlabs.umt.services.user.dao.IOauthTokenDAO;

public class OauthTokenDAO implements IOauthTokenDAO {
	private static final Logger LOG = Logger.getLogger(OauthTokenDAO.class);
	private static final String INSERT_SQL="insert into umt_oauth_token(access_token,refresh_token,create_time,access_expired,refresh_expired,uid,client_id,scope,redirect_uri) values(?,?,?,?,?,?,?,?,?)";
	private static final String QUERY_BY_ACCESS_TOKEN="select * from umt_oauth_token where access_token=?";
	private static final String QUERY_BY_REFRESH_TOKEN="select * from umt_oauth_token where refresh_token=?";
	private static final String UPDATE_BY_ID="update umt_oauth_token set access_token=?,refresh_token=?,create_time=?,access_expired=?,refresh_expired=?,uid=?,client_id=?,scope=?,redirect_uri=? where id=?";
	private static final String DELETE_BY_ID="delete from umt_oauth_token where id=? ";
	private static final String DELETE_BY_ACCESS_EXPIRED="delete from umt_oauth_token where access_expired<?";
	private static final String DELETE_BY_REFRESH_EXPIRED="delete from umt_oauth_token where refresh_expired<?";
	private static final String QUERY_BY_ID="select * from umt_oauth_token where id=?";
	private DatabaseUtil du;
	
	public OauthTokenDAO(DatabaseUtil du){
		this.du=du;
	}
	@Override
	public OauthToken getTokenByAccess(String accessToken) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(QUERY_BY_ACCESS_TOKEN);
			st.setString(1, accessToken);
			rs = st.executeQuery();
			List<OauthToken> result = getOauthToken(rs);
			if(result==null||result.isEmpty()){
				return null;
			}else{
				return result.get(0);
			}
		} catch (SQLException e) {
			LOG.error("获取token数据库错误",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	private List<OauthToken> getOauthToken(ResultSet rs) throws SQLException{
		List<OauthToken> result = new ArrayList<OauthToken>();
		while(rs.next()){
			OauthToken token = new OauthToken();
			token.setId(rs.getInt("id"));
			token.setAccessToken(rs.getString("access_token"));
			token.setRefreshToken(rs.getString("refresh_token"));
			token.setCreateTime(rs.getTimestamp("create_time"));
			token.setAccessExpired(rs.getTimestamp("access_expired"));
			token.setRefreshExpired(rs.getTimestamp("refresh_expired"));
			token.setUid(rs.getString("uid"));
			token.setClientId(rs.getString("client_id"));
			token.setScope(rs.getString("scope"));
			token.setRedirectURI(rs.getString("redirect_uri"));
			result.add(token);
		}
		return result;
	}
	@Override
	public OauthToken getTokenByRefresh(String refreshToken) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(QUERY_BY_REFRESH_TOKEN);
			st.setString(1, refreshToken);
			rs = st.executeQuery();
			List<OauthToken> result = getOauthToken(rs);
			if(result==null||result.isEmpty()){
				return null;
			}else{
				return result.get(0);
			}
		} catch (SQLException e) {
			LOG.error("获取token数据库错误",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public int save(OauthToken token) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			int i =1;
			st = conn.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS);
			st.setString(i++, token.getAccessToken());
			st.setString(i++, token.getRefreshToken());
			st.setTimestamp(i++, getTimestamp(token.getCreateTime()));
			st.setTimestamp(i++, getTimestamp(token.getAccessExpired()));
			st.setTimestamp(i++, getTimestamp(token.getRefreshExpired()));
			st.setString(i++, token.getUid());
			st.setString(i++, token.getClientId());
			st.setString(i++, token.getScope());
			st.setString(i++, token.getRedirectURI());
			st.execute();
			rs = st.getGeneratedKeys();
			if(rs.next()){
				int id = rs.getInt(1);
				token.setId(id);
				return id;
			}
		} catch (SQLException e) {
			LOG.error("获取token数据库错误",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return -1;
	}
	private Timestamp getTimestamp(Date data){
		return new Timestamp(data.getTime());
	}
	@Override
	public void update(OauthToken token) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			int i =1;
			st = conn.prepareStatement(UPDATE_BY_ID);
			st.setString(i++, token.getAccessToken());
			st.setString(i++, token.getRefreshToken());
			st.setTimestamp(i++, getTimestamp(token.getCreateTime()));
			st.setTimestamp(i++, getTimestamp(token.getAccessExpired()));
			st.setTimestamp(i++, getTimestamp(token.getRefreshExpired()));
			st.setString(i++, token.getUid());
			st.setString(i++, token.getClientId());
			st.setString(i++, token.getScope());
			st.setString(i++, token.getRedirectURI());
			st.setInt(i++, token.getId());
			st.execute();
		} catch (SQLException e) {
			LOG.error("获取token数据库错误",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void delete(OauthToken token) {
		delete(token.getId());
	}

	@Override
	public void delete(int tokenId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(DELETE_BY_ID);
			st.setInt(1, tokenId);
			st.execute();
		} catch (SQLException e) {
			LOG.error("获取token数据库错误",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void deleteBeforeAccessToken(Date accessExpired) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(DELETE_BY_ACCESS_EXPIRED);
			st.setTimestamp(1, getTimestamp(accessExpired));
			st.execute();
		} catch (SQLException e) {
			LOG.error("获取token数据库错误",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void deleteBeforeRefreshToken(Date refreshExpired) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(DELETE_BY_REFRESH_EXPIRED);
			st.setTimestamp(1, getTimestamp(refreshExpired));
			st.execute();
		} catch (SQLException e) {
			LOG.error("获取token数据库错误",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	@Override
	public OauthToken getTokenById(int id) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try {
			st = conn.prepareStatement(QUERY_BY_ID);
			st.setInt(1, id);
			rs = st.executeQuery();
			List<OauthToken> result = getOauthToken(rs);
			if(result==null||result.isEmpty()){
				return null;
			}else{
				return result.get(0);
			}
		} catch (SQLException e) {
			LOG.error("获取token数据库错误",e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

}
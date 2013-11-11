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
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.dao.ITokenDAO;

public class TokenDAO implements ITokenDAO {
	public TokenDAO(DatabaseUtil du){
		this.du=du;
	}
	@Override
	public boolean toUsed(int tokenId) {
		Connection conn = du.getConnection();
		PreparedStatement st=null;
		ResultSet rs = null;
		try{
			int index=0;
			st = conn.prepareStatement(TO_USED_SQL);
			st.setInt(++index, tokenId);
			return st.execute();
			
		}catch(SQLException e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return false;
	}
	@Override
	public boolean isValid(int tokenId, String random,int operation) {
		Connection conn = du.getConnection();
		PreparedStatement st=null;
		ResultSet rs = null;
		try{
			int index=0;
			st = conn.prepareStatement(SELECT_VALID+ BY_ID+BY_TOKEN+BY_OPERATION);
			st.setTimestamp(++index, new Timestamp(System.currentTimeMillis()));
			st.setInt(++index, tokenId);
			st.setString(++index, random);
			st.setInt(++index, operation);
			
			rs=st.executeQuery();
			return rs.next();
			
		}catch(SQLException e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return false;
	}
	@Override
	public void removeTokenUnused(int uid,int operation) {
		Connection conn = du.getConnection();
		PreparedStatement st=null;
		try{
			int index=0;
			st = conn.prepareStatement(REMOVE_SQL+BY_UID+BY_OPERATION);
			st.setInt(++index, uid);
			st.setInt(++index, operation);
			st.execute();
		}catch(SQLException e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(null, st, conn);
		}
	}
	public int createToken(Token token) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(CREATE_SQL);
			int index=0;
			st.setString(++index, token.getRandom());
			st.setInt(++index, token.getUid());
			st.setTimestamp(++index, new Timestamp(token.getCreateTime().getTime()));
			st.setInt(++index, token.getOperation());
			st.setInt(++index, token.getStatus());
			st.setTimestamp(++index,new Timestamp(token.getExpireTime().getTime()));
			st.setString(++index, token.getContent());
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
	@Override
	public Token getATokenByUidAndOperation(int uid, int type,int status) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			String sql=SELECT_SQL;
			if(uid>0){
				sql+=BY_UID;
			}
			if(type>0){
				sql+=BY_OPERATION;
			}
			if(status>0){
				sql+=BY_STATUS;
			}
			sql+=ORDER_BY_CREATE_TIME_DESC;
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
			if (rs.next()){
				return readToken(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	@Override
	public List<Token> getTokenByUidAndOperation(int uid, int type, int status) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			String sql=SELECT_SQL;
			if(uid>0){
				sql+=BY_UID;
			}
			if(type>0){
				sql+=BY_OPERATION;
			}
			if(status>0){
				sql+=BY_STATUS;
			}
			sql+=ORDER_BY_CREATE_TIME_DESC;
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
			List<Token> tokens=new ArrayList<Token>();
			while(rs.next()){
				tokens.add(readToken(rs));
			}
			return tokens;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	private Token readToken(ResultSet rs)throws SQLException{
		Token token = new Token();
		token.setId(rs.getInt("id"));
		token.setCreateTime(rs.getTimestamp("create_time"));
		token.setOperation(rs.getInt("operation"));
		token.setStatus(rs.getInt("status"));
		token.setExpireTime(rs.getTimestamp("expire_time"));
		token.setContent(rs.getString("content"));
		token.setRandom(rs.getString("token"));
		token.setUid(rs.getInt("uid"));
		return token;
	}
	public Token getToken(int tokenid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(SELECT_SQL+BY_ID);
			int index=0;
			st.setInt(++index, tokenid);
			rs = st.executeQuery();
			if (rs.next()){
				return readToken(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	public void removeToken(int tokenid) {
		Connection conn = du.getConnection();
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(REMOVE_SQL+BY_ID);
			int index=0;
			st.setInt(++index, tokenid);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(null, st, conn);
		}
	}
	
	private static final Logger LOGGER = Logger.getLogger(TokenDAO.class);
	private DatabaseUtil du;
	private static final String CREATE_SQL="insert into umt_token(`token`, `uid`, `create_time`,`operation`,`status`,`expire_time`,`content`) values(?,?,?,?,?,?,?)";
	private static final String TO_USED_SQL="update `umt_token` set `status`="+Token.STATUS_USED+" where `id`=?";
	private static final String REMOVE_SQL="update `umt_token` set `status`='"+Token.STATUS_DELETED+"' where 1=1 ";
	private static final String SELECT_SQL="select * from `umt_token` where 1=1 and `status`!='"+Token.STATUS_DELETED+"'";
	private static final String SELECT_VALID=SELECT_SQL+" and `expire_time`>=? and `status`="+Token.STATUS_UNUSED;
	private static final String BY_OPERATION=" and `operation`=? ";
	private static final String BY_UID=" and `uid`=? "; 
	private static final String BY_ID=" and `id`=? ";
	private static final String BY_STATUS=" and `status`=? ";
	private static final String BY_TOKEN=" and `token`=? ";
	private static final String ORDER_BY_CREATE_TIME_DESC=" order by `create_time` desc";
}
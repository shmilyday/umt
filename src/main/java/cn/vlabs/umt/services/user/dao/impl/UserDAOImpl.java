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
/*
c * Copyright (c) 2008-2013 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
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
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.SQLUtils;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UserField;
import cn.vlabs.umt.services.user.dao.IUserDAO;

public class UserDAOImpl implements IUserDAO {
	
	public UserDAOImpl(DatabaseUtil du) {
		this.du = du;
	}
	@Override
	public void updateValueByColumn(int[] uid,String columnName, String value) {
		String uidStr=Arrays.toString(uid);
		uidStr=uidStr.substring(1,uidStr.length()-1);
		String sql=" update umt_user set "+
					columnName+"=? "+
					" where id in ("+uidStr+") ";
		
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(sql);
			int index=0;
			st.setString(++index,value);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	@Override
	public User checkPassword(String loginName, String password) {
		String sql=" select u.* from umt_user u,umt_login_name l where l.login_name=? and u.id=l.uid and password=? order by u.id desc limit 0,1";
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(sql);
			int index=0;
			st.setString(++index,loginName);
			st.setString(++index, password);
			rs=st.executeQuery();
			if(rs.next()){
				return readUser(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	public int create(User user) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(INSERT_SQL);
			int index=0;
			st.setString(++index, user.getTrueName());
			st.setString(++index, user.getPassword());
			st.setString(++index, user.getCstnetId().toLowerCase());
			st.setString(++index, user.getType());
			st.execute();
			rs = st.getGeneratedKeys();
			rs.next();
			int uid=rs.getInt(1);
			user.setId(uid);
			String umtId="";
			if(CommonUtils.isNull(user.getUmtId())){
				umtId=String.valueOf(uid+10000000+"");
			}else{
				umtId=user.getUmtId();
			}
			user.setUmtId(umtId);
			updateValueByColumn(new int[]{uid}, "umt_id", umtId);
			if(CommonUtils.isNull(user.getCstnetId())||BindInfo.LIKE_EMAIL.equals(user.getCstnetId())){
				user.setCstnetId(BindInfo.getDummyEmail(user.getUmtId()));
				updateValueByColumn(new int[]{uid},"cstnet_id",user.getCstnetId());
			}
			return uid;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return -1;
	}

	public void remove(int userid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE_SQL);
			st.setInt(1, userid);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	public void updateWithoutPass(User user) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(UPDATE_WITH_OUT_PASSWORD_SQL);
			int index=0;
			st.setString(++index, user.getTrueName());
			st.setString(++index, user.getUmtId());
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	
	public void update(User user) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(UPDATE_ALL_SQL);
			int index=0;
			st.setString(++index, user.getTrueName());
			st.setString(++index, user.getPassword());
			st.setString(++index, user.getUmtId());

			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	
	@Override
	public void updatePassword(int uid, String password) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(UPDATE_PASSWORD);
			st.setString(1, password);
			st.setInt(2, uid);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	@Override
	public List<User> getUsersByUmtId(List<String> umtIds) {
		if(CommonUtils.isNull(umtIds)){
			return null;
		}
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			StringBuffer insql=new StringBuffer();
			for(int i=0;i<umtIds.size();i++){
				insql.append(i==0?"?":",?");
			}
			st = conn.prepareStatement(SELECT_SQL_BY_UMT_ID+" ("+insql.toString()+")");
			for(int i=0;i<umtIds.size();i++){
				st.setString(i+1, umtIds.get(i));
			}
			rs = st.executeQuery();
			List<User> users=new ArrayList<User>();
			while (rs.next()){
				users.add(readUser(rs));
			}
			return users;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	public User getUserByUid(int uid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT_SQL_BY_ID);
			st.setInt(1, uid);
			rs = st.executeQuery();
			if (rs.next()){
				return readUser(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	
	public User getUserByOpenid(String openid,String type,String url) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		boolean isUrlNull=CommonUtils.isNull(url);
		try {
			st = conn.prepareStatement(OPENID_SELECT_SQL+(isUrlNull?"":" and b.`url`=?"));
			int index=0;
			st.setString(++index, openid);
			st.setString(++index, type);
			if(!isUrlNull){
				st.setString(++index, url);
			}
			rs = st.executeQuery();
			if (rs.next()){
				return readUser(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	private User readUser(ResultSet rs) throws SQLException {
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setCstnetId(rs.getString("cstnet_id").toLowerCase());
		u.setPassword(rs.getString("password"));
		u.setTrueName(rs.getString("true_name"));
		u.setUmtId(rs.getString("umt_id"));
		u.setType(rs.getString("type"));
		String secondaryEmails=rs.getString("secondary_email");
		if(!CommonUtils.isNull(secondaryEmails)){
			u.setSecondaryEmails(secondaryEmails.split(";"));
		}
		u.setSendGEOEmailSwitch(rs.getBoolean("send_geo_email_switch"));
		u.setSecurityEmail(rs.getString("security_email"));
		u.setAccountStatus(rs.getString("account_status"));
		u.setCreateTime(rs.getTimestamp("create_time"));
		return u;
	}
	public int getUserCount() {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(COUNT_SQL);
			rs = st.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return 0;
	}
	public void remove(int[] uids) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			conn.setAutoCommit(false);
			st = conn.prepareStatement(DELETE_SQL);
			for (int uid:uids){
				st.setInt(1, uid);
				st.addBatch();
			}
			st.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	public Collection<User> getUsers(int start, int count) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<User> users = new ArrayList<User>();
		try {
			st = conn.prepareStatement(GET_USERS);
			st.setInt(1, start);
			st.setInt(2, count);
			rs = st.executeQuery();
			while (rs.next()){
				users.add(readUser(rs));
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		
		if (users.size()>0)
			return users;
		else
			return null;
		
	}
	
	
	
	public int searchCount(String query){ 
		String q="%"+SQLUtils.quote(query)+"%";
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(QUERY_COUNT);
			int index=0;
			st.setString(++index, q);
			st.setString(++index, q);
			st.setString(++index, q);
			rs = st.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return 0;
	}
	public void create(List<User> users) {
		if(!CommonUtils.isNull(users)){
			for(User user:users){
				create(user);
			}
		}
	}
	
	public Collection<User> search(String query, int start, int count, UserField field, boolean isAscendent) {
		String querySQL=QUERY_USERS;
		if (field==UserField.cstnetId){
			querySQL=querySQL.replaceAll("%order%", "cstnet_id");
		}
		if (field==UserField.trueName){
			querySQL=querySQL.replaceAll("%order%", "true_name");
		}
		if (isAscendent){
			querySQL=querySQL.replaceAll("%asc%", "asc");
		}else{
			querySQL=querySQL.replaceAll("%asc%", "desc");
		}
		String q="%"+SQLUtils.quote(query)+"%";
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		ArrayList<User> users = new ArrayList<User>();
		try {
			st = conn.prepareStatement(querySQL);
			int index=0;
			st.setString(++index, q);
			st.setString(++index, q);
			st.setString(++index, q);
			st.setInt(++index, start);
			st.setInt(++index, count);
			rs = st.executeQuery();
			while (rs.next()){
				users.add(readUser(rs));
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		if (users.size()>0){
			return users;
		}else{
			return null;
		}
	}
	@Override
	public String getLastedUmtId() {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st=conn.prepareStatement(SELECT_LAST_UMT_ID);
			rs= st.executeQuery();
			if(rs.next()){
				return rs.getString("umt_id");
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return "";
	}
	@Override
	public List<Integer> getExpectMeByCstnetId(int uid, String loginName) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		List<Integer> list=new ArrayList<Integer>();
		try {
			st=conn.prepareStatement(SELECT_UN_USED_USER);
			int index=0;
			st.setString(++index, loginName);
			st.setInt(++index, uid);
			rs= st.executeQuery();
			while(rs.next()){
				list.add(rs.getInt("id"));
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return list;
	}
	@Override
	public Collection<User> searchUmtOnly(String keyword, int offset, int size) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		Collection<User> list=new ArrayList<User>();
		try {
			st=conn.prepareStatement(QUERY_UMT_ONLY);
			int index=0;
			st.setString(++index, "%"+keyword+"%");
			st.setString(++index, "%"+keyword+"%");
			st.setString(++index, "%"+keyword+"%");
			st.setInt(++index, offset);
			int s=size;
			if(s<0){
				s=Integer.MAX_VALUE;
			}
			st.setInt(++index, s);
			rs= st.executeQuery();
			while(rs.next()){
				list.add(readUser(rs));
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return list;
	}
	@Override
	public List<User> getUsersByIds(List<String> uids) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		List<User> list=new ArrayList<User>();
		try {
			StringBuffer sb=new StringBuffer(SELECT_BASE);
			sb.append(" and id in(");
			for(int i=0;i<uids.size();i++){
				sb.append("?,");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
			st=conn.prepareStatement(sb.toString());
			int index=0;
			for(String uid:uids){
				st.setString(++index, uid);
			}
			rs= st.executeQuery();
			while(rs.next()){
				list.add(readUser(rs));
			}
			return list;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return list;
	}
	
	@Override
	public void switchGEOInfo(int uid, boolean userSwitch) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		String sql="update umt_user set send_geo_email_switch='"+userSwitch+"' where id=?";
		try {
			st=conn.prepareStatement(sql);
			int index=0;
			st.setInt(++index, uid);
			st.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	
	private DatabaseUtil du;

	private static final Logger LOGGER = Logger.getLogger(UserDAOImpl.class);
	
	private static final  String QUERY_COUNT = "select count(*) from umt_user where umt_id like ? or true_name like ? or cstnet_id like ?";
	private static final  String QUERY_USERS = "select * from umt_user where umt_id like ? or true_name like ? or cstnet_id like ? order by %order% %asc% limit ?, ?";
	private static final  String QUERY_UMT_ONLY="select * from umt_user where (umt_id like ? or true_name like ? or cstnet_id like ?)  and (`type`!='"+User.USER_TYPE_MAIL_AND_UMT+"' or `type`!='"+User.USER_TYPE_CORE_MAIL+"') limit ?, ?";
	private static final  String GET_USERS="select * from umt_user order by id limit ?, ?";
	private static final  String COUNT_SQL="select count(*) from umt_user";
	
	private static final  String INSERT_SQL = "insert into umt_user(`true_name`, `password`,`cstnet_id`,`type`,`create_time`) values(?,?,?,?,now())";
	private static final  String UPDATE_PASSWORD = "update umt_user set password=? where id=?";

	private static final  String DELETE_SQL = "delete from umt_user where id=?";

    private static final String SELECT_BASE="select * from umt_user where 1=1 ";
	private static final  String UPDATE_ALL_SQL = "update umt_user set true_name=?, password=? where umt_id=?";
	private static final  String UPDATE_WITH_OUT_PASSWORD_SQL = "update umt_user set true_name=? where umt_id=?";
	private static final  String SELECT_SQL_BY_UMT_ID="select * from `umt_user` where `umt_id` in ";
	private static final  String SELECT_SQL_BY_ID="select * from umt_user where id=? limit 0,1 ";
	private static final  String OPENID_SELECT_SQL="select * from umt_third_party_bind b,umt_user u where b.open_id=? and u.id=b.uid and b.`type`=?";
	private static final  String SELECT_LAST_UMT_ID="select umt_id from umt_user order by id desc limit 0,1";
	private static final String SELECT_UN_USED_USER="select * from umt_user where cstnet_id=? and id!=?";

}
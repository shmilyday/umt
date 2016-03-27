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
import cn.vlabs.umt.services.user.bean.AppSecret;
import cn.vlabs.umt.services.user.dao.IAppSecretDAO;
import cn.vlabs.umt.services.user.service.ITransform;

public class AppSecretDAOImpl implements IAppSecretDAO {
	private static final String TABLE = " `umt_app_secret` ";
	private static final String BY_APP_ID = " and app_id=? ";
	private static final String BY_ID = " and `id`=? ";

	private static final String BY_TYPE = " and `secret_type`=? ";

	private static final String BY_UID = " and uid=? ";
	private static final String DELETE = "delete from " + TABLE + " where 1=1 ";
	private static final String INSERT = "insert into "
			+ TABLE
			+ "(`uid`,`app_id`,`secret_type`,`secret`,`userStatus`,`userName`,`userCstnetId`,`userLdapName`, `hashedSecret`) values(?,?,?,?,?,?,?,?,?)";
	private static final Logger LOG = Logger.getLogger(AppSecretDAOImpl.class);
	private static final String SELECT = "select * from" + TABLE + "where 1=1 ";
	private static final String UPDATE_PWD = "update " + TABLE + " set userLdapName=?, secret=? , hashedSecret=? where 1=1 ";
	private DatabaseUtil du;
	public AppSecretDAOImpl(DatabaseUtil du) {
		this.du = du;
	}

	private AppSecret extract(ResultSet rs) throws SQLException {
		AppSecret secret = new AppSecret();
		secret.setId(rs.getInt("id"));
		secret.setUid(rs.getInt("uid"));
		secret.setAppId(rs.getString("app_id"));
		secret.setSecretType(rs.getString("secret_type"));
		secret.setSecret(rs.getString("secret"));
		secret.setUserStatus(rs.getString("userStatus"));
		secret.setUserName(rs.getString("userName"));
		secret.setUserCstnetId(rs.getString("userCstnetId"));
		secret.setUserLdapName(rs.getString("userLdapName"));
		secret.parse(rs.getString("hashedSecret"));
		return secret;
	}

	@Override
	public void deleteMember(int sId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE + BY_ID);
			int index = 0;
			st.setInt(++index, sId);
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void deleteMyLdapSecret(int uid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE + BY_TYPE + BY_UID);
			int index = 0;
			st.setString(++index, AppSecret.SECRET_TYPE_LDAP);
			st.setInt(++index, uid);
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void deleteMySecret(int secretId, int userId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE + BY_UID + BY_ID);
			int index = 0;
			st.setInt(++index, userId);
			st.setInt(++index, secretId);
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public AppSecret findAppSecretById(int secretId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT + BY_ID);
			int index = 0;
			st.setInt(++index, secretId);
			rs = st.executeQuery();
			if (rs.next()) {
				return extract(rs);
			}
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public List<AppSecret> findAppSecretByTypeAndAppId(String secretType,
			String appId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT + BY_TYPE + BY_APP_ID);
			int index = 0;
			st.setString(++index, secretType);
			st.setString(++index, appId);
			rs = st.executeQuery();
			List<AppSecret> appSecret = new ArrayList<AppSecret>();
			while (rs.next()) {
				appSecret.add(extract(rs));
			}
			return appSecret;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public List<AppSecret> findAppSecretByUid(int uid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT + BY_UID);
			int index = 0;
			st.setInt(++index, uid);
			rs = st.executeQuery();
			List<AppSecret> appSecret = new ArrayList<AppSecret>();
			while (rs.next()) {
				appSecret.add(extract(rs));
			}
			return appSecret;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public AppSecret findAppSecretByUidAndAppId(String appId, int userId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT + BY_UID + BY_APP_ID);
			int index = 0;
			st.setInt(++index, userId);
			st.setString(++index, appId);
			rs = st.executeQuery();
			if (rs.next()) {
				return extract(rs);
			}
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public List<AppSecret> findMyAppMember(String appId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT + BY_APP_ID);
			int index = 0;
			st.setString(++index, appId);
			rs = st.executeQuery();
			List<AppSecret> acs = new ArrayList<AppSecret>();
			while (rs.next()) {
				acs.add(extract(rs));
			}
			return acs;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public void insert(AppSecret appSecret) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(INSERT);
			int index = 0;
			st.setInt(++index, appSecret.getUid());
			st.setString(++index, appSecret.getAppId());
			st.setString(++index, appSecret.getSecretType());
			st.setString(++index, appSecret.getHashedSecret(ITransform.TYPE_SHA));
			st.setString(++index, appSecret.getUserStatus());
			st.setString(++index, appSecret.getUserName());
			st.setString(++index, appSecret.getUserCstnetId());
			st.setString(++index, appSecret.getUserLdapName());
			st.setString(++index, appSecret.getAllHashedSecret());
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public boolean isSecretUsed(String transformd, int uid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("select count(*) c from" + TABLE
					+ "where `secret`=? and uid=?");
			int index = 0;
			st.setString(++index, transformd);
			st.setInt(++index, uid);
			rs = st.executeQuery();
			if (rs.next()) {
				return rs.getInt("c") > 0;
			}
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return true;
	}

	@Override
	public void openMember(int appId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement("update " + TABLE + " set userStatus='"
					+ AppSecret.USER_STATUS_ACCEPT + "' where id=?");
			int index = 0;
			st.setInt(++index, appId);
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void updateSecret(int id, String loginName,String shaPassword, String hashedSecret) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(UPDATE_PWD + BY_ID);
			int index = 0;
			st.setString(++index, loginName);
			st.setString(++index, shaPassword);
			st.setString(++index, hashedSecret);
			st.setInt(++index, id);
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}

	}
}

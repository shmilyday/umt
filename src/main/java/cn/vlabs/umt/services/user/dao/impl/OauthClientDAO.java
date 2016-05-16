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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.duckling.common.util.CommonUtils;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.dao.IOauthClientDAO;

public class OauthClientDAO implements IOauthClientDAO {
	private static final Logger LOG = Logger.getLogger(OauthClientDAO.class);
	private static final String INSERT_SQL = "insert into umt_oauth_client(client_id,client_secret,scope,redirect_uri,status,client_name,applicant,"
			+ "application_time,applicant_phone,contact_info,description,third_party,client_website,uid,company,app_type,pwd_type,need_org_info,enable_app_pwd,compulsion_strong_pwd) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String SELECT = "select * from umt_oauth_client ";
	private static final String SELECT_WITH_CSTNETID = "select u.cstnet_id,c.* from umt_oauth_client c left join umt_user u on c.uid=u.id";
	private static final String QUERY_BY_CLIENT_ID = SELECT
			+ " where client_id=?";
	private static final String QUERY_BY_ID = SELECT + "where id=?";
	private static final String QUERY_BY_UID = SELECT + "where uid=?";
	private static final String BY_TYPE = " and `app_type`=? ";
	private static final String BY_ENABLE = " and `enable_app_pwd`=? ";

	private static final String QUERY_BY_STATUS = SELECT + " where status=?";
	private static final String DELETE_BY_ID = "delete from umt_oauth_client where id=?";
	private static final String UPDATE_BY_ID = "update umt_oauth_client set client_id=?,client_secret=?,scope=?,redirect_uri=?,status=?,client_name=?,applicant=?,"
			+ "application_time=?,applicant_phone=?,contact_info=?,description=?,third_party=?,client_website=?,company=? ,app_type=?,pwd_type=?,need_org_info=?,enable_app_pwd=?,compulsion_strong_pwd=? where id=?";
	private static final String UPDATE_DEVELOP_BY_ID = "update umt_oauth_client set redirect_uri=?,`status`=?,client_name=?,applicant=?,"
			+ "contact_info=?,description=?,client_website=?,company=?,app_type=?,enable_app_pwd=? where id=?";
	private DatabaseUtil du;

	public OauthClientDAO(DatabaseUtil du) {
		this.du = du;
	}

	@Override
	public List<OauthClientBean> findByUid(int userId, String type) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		int index = 0;
		try {
			st = conn.prepareStatement(QUERY_BY_UID + BY_TYPE,
					Statement.RETURN_GENERATED_KEYS);
			st.setInt(++index, userId);
			st.setString(++index, type);
			rs = st.executeQuery();
			return getOauthClient(rs);
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public int save(OauthClientBean bean) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			int i = 1;
			st = conn.prepareStatement(INSERT_SQL,
					Statement.RETURN_GENERATED_KEYS);
			st.setString(i++, bean.getClientId());
			st.setString(i++, bean.getClientSecret());
			st.setString(i++, bean.getScope());
			st.setString(i++, bean.getRedirectURI());
			st.setString(i++, bean.getStatus());
			st.setString(i++, bean.getClientName());
			st.setString(i++, bean.getApplicant());
			st.setTimestamp(i++, new Timestamp(bean.getApplicationTime()
					.getTime()));
			st.setString(i++, bean.getApplicantPhone());
			st.setString(i++, bean.getContactInfo());
			st.setString(i++, bean.getDescription());
			st.setString(i++, bean.getThirdParty());
			st.setString(i++, bean.getClientWebsite());
			st.setInt(i++, bean.getUid());
			st.setString(i++, bean.getCompany());
			st.setString(i++, bean.getAppType());
			st.setString(i++, bean.getPwdType());
			st.setInt(i++, bean.getNeedOrgInfo());
			st.setString(i++, bean.getEnableAppPwd());
			st.setBoolean(i++, bean.isCompulsionStrongPwd());
			st.execute();
			rs = st.getGeneratedKeys();
			if (rs.next()) {
				int id = rs.getInt(1);
				bean.setId(id);
				return id;
			}
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return -1;
	}

	@Override
	public OauthClientBean findByClientId(String id) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(QUERY_BY_CLIENT_ID);
			st.setString(1, id);
			rs = st.executeQuery();
			List<OauthClientBean> result = getOauthClient(rs);
			if (result == null || result.isEmpty()) {
				return null;
			} else {
				return result.get(0);
			}
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	private List<OauthClientBean> getOauthClient(ResultSet rs)
			throws SQLException {
		List<OauthClientBean> result = new ArrayList<OauthClientBean>();
		while (rs.next()) {
			OauthClientBean bean = new OauthClientBean();
			bean.setId(rs.getInt("id"));
			bean.setClientId(rs.getString("client_id"));
			bean.setClientSecret(rs.getString("client_secret"));
			bean.setScope(rs.getString("scope"));
			bean.setRedirectURI(rs.getString("redirect_uri"));
			bean.setStatus(rs.getString("status"));
			bean.setApplicant(rs.getString("applicant"));
			bean.setApplicantPhone(rs.getString("applicant_phone"));
			bean.setApplicationTime(rs.getTimestamp("application_time"));
			bean.setClientName(rs.getString("client_name"));
			bean.setDescription(rs.getString("description"));
			bean.setContactInfo(rs.getString("contact_info"));
			bean.setThirdParty(rs.getString("third_party"));
			bean.setClientWebsite(rs.getString("client_website"));
			bean.setUid(rs.getInt("uid"));
			bean.setCompany(rs.getString("company"));
			bean.setAppType(rs.getString("app_type"));
			bean.setPwdType(rs.getString("pwd_type"));
			bean.setLogo100m100(rs.getInt("logo_100"));
			bean.setLogo64m64(rs.getInt("logo_64"));
			bean.setLogo32m32(rs.getInt("logo_32"));
			bean.setLogo16m16(rs.getInt("logo_16"));
			bean.setDefaultLogo(rs.getInt("default_logo"));
			bean.setCompulsionStrongPwd(rs.getBoolean("compulsion_strong_pwd"));
			try {
				bean.setUserName(rs.getString("cstnet_id"));
			} catch (SQLException e) {

			}
			bean.setNeedOrgInfo(rs.getInt("need_org_info"));
			bean.setEnableAppPwd(rs.getString("enable_app_pwd"));
			result.add(bean);
		}
		return result;
	}

	@Override
	public List<OauthClientBean> findByStatus(String status) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(QUERY_BY_STATUS);
			st.setString(1, status);
			rs = st.executeQuery();
			return getOauthClient(rs);
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return Collections.emptyList();
	}

	@Override
	public OauthClientBean findById(int id) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(QUERY_BY_ID);
			st.setInt(1, id);
			rs = st.executeQuery();
			List<OauthClientBean> result = getOauthClient(rs);
			if (result == null || result.isEmpty()) {
				return null;
			} else {
				return result.get(0);
			}
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public void delete(OauthClientBean bean) {
		delete(bean.getId());
	}

	@Override
	public void delete(int id) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(DELETE_BY_ID);
			st.setInt(1, id);
			st.execute();
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void update(OauthClientBean bean) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			int i = 1;
			st = conn.prepareStatement(UPDATE_BY_ID);
			st.setString(i++, bean.getClientId());
			st.setString(i++, bean.getClientSecret());
			st.setString(i++, bean.getScope());
			st.setString(i++, bean.getRedirectURI());
			st.setString(i++, bean.getStatus());
			st.setString(i++, bean.getClientName());
			st.setString(i++, bean.getApplicant());
			st.setTimestamp(i++, new Timestamp(bean.getApplicationTime()
					.getTime()));
			st.setString(i++, bean.getApplicantPhone());
			st.setString(i++, bean.getContactInfo());
			st.setString(i++, bean.getDescription());
			st.setString(i++, bean.getThirdParty());
			st.setString(i++, bean.getClientWebsite());
			st.setString(i++, bean.getCompany());
			st.setString(i++, bean.getAppType());
			st.setString(i++, bean.getPwdType());
			st.setInt(i++, bean.getNeedOrgInfo());
			st.setString(i++, bean.getEnableAppPwd());
			st.setBoolean(i++, bean.isCompulsionStrongPwd());
			st.setInt(i++, bean.getId());
			st.execute();
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void updateDevelop(OauthClientBean bean) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			int i = 1;
			st = conn.prepareStatement(UPDATE_DEVELOP_BY_ID);
			st.setString(i++, bean.getRedirectURI());
			st.setString(i++, bean.getStatus());
			st.setString(i++, bean.getClientName());
			st.setString(i++, bean.getApplicant());
			st.setString(i++, bean.getContactInfo());
			st.setString(i++, bean.getDescription());
			st.setString(i++, bean.getClientWebsite());
			st.setString(i++, bean.getCompany());
			st.setString(i++, bean.getAppType());
			st.setString(i++, bean.getEnableAppPwd());
			st.setInt(i++, bean.getId());
			st.execute();
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}

	}

	@Override
	public List<OauthClientBean> getAll() {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(SELECT_WITH_CSTNETID);
			rs = st.executeQuery();
			return getOauthClient(rs);
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return Collections.emptyList();
	}

	@Override
	public List<OauthClientBean> searchClientByKey(String key, int offset,
			int size) {

		String sql = "select * from umt_oauth_client where `status`='"
				+ OauthClientBean.STATUS_ACCEPT + "'";
		String extendSql = " and (client_name like ? or client_website like ?) ";
		String limitSql = " limit " + offset + "," + size;
		Connection conn = du.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			if (CommonUtils.isNull(key)) {
				st = conn.prepareStatement(sql + limitSql);
			} else {
				st = conn.prepareStatement(sql + extendSql + limitSql);
				int index = 0;
				st.setString(++index, "%" + key + "%");
				st.setString(++index, "%" + key + "%");
			}
			rs = st.executeQuery();
			return getOauthClient(rs);
		} catch (SQLException e) {
			LOG.error("获取token数据库错误", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return Collections.emptyList();
	}

	@Override
	public void updateLogo(OauthClientBean bean, boolean is100Updated,
			boolean is64Updated, boolean is32Updated, boolean is16Updated) {
		if (!is100Updated && !is64Updated && !is32Updated && !is16Updated) {
			LOG.error("update none logo id ,exit!");
			return;
		}
		Connection conn = du.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "update umt_oauth_client set ";
			int index = 0;
			if (is100Updated) {
				sql += "logo_100=?,";
			}
			if (is64Updated) {
				sql += "logo_64=?,";
			}
			if (is32Updated) {
				sql += "logo_32=?,";
			}
			if (is16Updated) {
				sql += "logo_16=?,";
			}
			if (bean.getDefaultLogo() != 0) {
				sql += "default_logo=?,";
			}
			if (!CommonUtils.isNull(bean.getLogoCustom())) {
				sql += "logo_custom=?,";
			}
			sql = CommonUtils.format(sql);
			sql += " where id=" + bean.getId();
			st = conn.prepareStatement(sql);
			if (is100Updated) {
				st.setInt(++index, bean.getLogo100m100());
			}
			if (is64Updated) {
				st.setInt(++index, bean.getLogo64m64());
			}
			if (is32Updated) {
				st.setInt(++index, bean.getLogo32m32());
			}
			if (is16Updated) {
				st.setInt(++index, bean.getLogo16m16());
			}
			if (bean.getDefaultLogo() != 0) {
				st.setInt(++index, bean.getDefaultLogo());
			}
			if (!CommonUtils.isNull(bean.getLogoCustom())) {
				st.setString(++index, bean.getLogoCustom());
			}
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public void removeLogo(OauthClientBean bean, boolean is100Updated,
			boolean is64Updated, boolean is32Updated, boolean is16Updated) {
		if (!is100Updated && !is64Updated && !is32Updated && !is16Updated) {
			LOG.error("update none logo id ,exit!");
			return;
		}
		Connection conn = du.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "update umt_oauth_client set ";
			if (is100Updated) {
				sql += "logo_100=default_logo,";
			}
			if (is64Updated) {
				sql += "logo_64=default_logo,";
			}
			if (is32Updated) {
				sql += "logo_32=default_logo,";
			}
			if (is16Updated) {
				sql += "logo_16=default_logo,";
			}
			sql = CommonUtils.format(sql);
			sql += " where id=" + bean.getId();
			st = conn.prepareStatement(sql);
			st.executeUpdate();
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public List<OauthClientBean> findEnableAppAndAccepted(String type) {
		Connection conn = du.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = QUERY_BY_STATUS + BY_ENABLE;
			if (!"all".equals(type)) {
				sql += BY_TYPE;
			}
			st = conn.prepareStatement(sql);
			int index = 0;
			st.setString(++index, OauthClientBean.STATUS_ACCEPT);
			st.setString(++index, "yes");
			if (!"all".equals(type)) {
				st.setString(++index, type);
			}
			rs = st.executeQuery();
			return getOauthClient(rs);
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public List<String> getAllCallBack() {
		List<String> array = new ArrayList<String>();
		Connection conn = du.getConnection();
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			String sql = "select redirect_uri from umt_oauth_client ";
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next()) {
				array.add(rs.getString("redirect_uri"));
			}
			return array;
		} catch (SQLException e) {
			LOG.error("", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return array;
	}
}
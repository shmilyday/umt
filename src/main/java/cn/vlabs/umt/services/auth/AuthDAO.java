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
package cn.vlabs.umt.services.auth;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;

public class AuthDAO {
	private static final Logger LOG = Logger.getLogger(AuthDAO.class);
	private DatabaseUtil du;
	public AuthDAO(DatabaseUtil du) {
		this.du = du;
	}
	public void create(ThirdPartyAuth auth) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("insert into umt_third_auth(code, name,clientId, secret,serverUrl,theme,showInLogin) values(?,?,?,?,?,?,?)");
			pst.setString(1, auth.getCode());
			pst.setString(2, auth.getName());
			pst.setString(3, auth.getClientId());
			pst.setString(4, auth.getSecret());
			pst.setString(5, auth.getServerUrl());
			pst.setString(6, auth.getTheme());
			pst.setInt(7, auth.isShowInLogin()?1:0);
			pst.execute();
		} catch (SQLException e) {
			LOG.error("记录记账信息时发生错误:" + e.getMessage());
			LOG.error("详细信息:", e);
		} finally {
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	public List<ThirdPartyAuth> findAll(String sql) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			List<ThirdPartyAuth> auths = new ArrayList<ThirdPartyAuth>();
			pst = conn.prepareStatement(sql);
			rs = pst.executeQuery();
			while(rs.next()) {
				auths.add(read(rs));
			}
			return auths;
		} catch (SQLException e) {
			LOG.error("记录记账信息时发生错误:" + e.getMessage());
			LOG.error("详细信息:", e);
		} finally {
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
	
	public List<ThirdPartyAuth> findAllShowInLogin() {
		String sql = "select * from umt_third_auth where showInLogin!=0";
		return findAll(sql);
	}

	public ThirdPartyAuth findByCode(String code) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("select * from umt_third_auth where code=?");
			pst.setString(1, code);
			rs = pst.executeQuery();
			if (rs.next()) {
				return read(rs);
			}
		} catch (SQLException e) {
			LOG.error("记录记账信息时发生错误:" + e.getMessage());
			LOG.error("详细信息:", e);
		} finally {
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}

	public List<ThirdPartyAuth> getAll() {
		String sql = "select * from umt_third_auth";
		return findAll(sql);
	}
	private ThirdPartyAuth read(ResultSet rs) throws SQLException {
		ThirdPartyAuth auth = new ThirdPartyAuth();
		auth.setClientId(rs.getString("clientId"));
		auth.setCode(rs.getString("code"));
		auth.setName(rs.getString("name"));
		auth.setSecret(rs.getString("secret"));
		auth.setServerUrl(rs.getString("serverUrl"));
		auth.setTheme(rs.getString("theme"));
		auth.setShowInLogin(rs.getInt("showInLogin")!=0);
		return auth;
	}
	public void remove(String code) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn
					.prepareStatement("delete from umt_third_auth where code=?");
			pst.setString(1, code);
			pst.execute();
		} catch (SQLException e) {
			LOG.error("记录记账信息时发生错误:" + e.getMessage());
			LOG.error("详细信息:", e);
		} finally {
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	public void update(ThirdPartyAuth auth){
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement("update umt_third_auth set name=?,clientId=?, secret=?,serverUrl=?, theme=?, showInLogin=? where code=?");
			pst.setString(1, auth.getName());
			pst.setString(2, auth.getClientId());
			pst.setString(3, auth.getSecret());
			pst.setString(4, auth.getServerUrl());
			pst.setString(5, auth.getTheme());
			pst.setInt(6, auth.isShowInLogin()?1:0);
			pst.setString(7, auth.getCode());
			pst.execute();
		} catch (SQLException e) {
			LOG.error("记录记账信息时发生错误:" + e.getMessage());
			LOG.error("详细信息:", e);
		} finally {
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
}

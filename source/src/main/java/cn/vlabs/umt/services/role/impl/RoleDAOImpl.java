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
package cn.vlabs.umt.services.role.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.role.RoleDAO;
import cn.vlabs.umt.services.role.UMTRole;
import cn.vlabs.umt.services.user.bean.User;

public class RoleDAOImpl implements RoleDAO {
	public RoleDAOImpl(DatabaseUtil du) {
		this.du = du;
	}
	public Collection<User> getRoleMembers(String rolename) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(getMemberUsers);
			st.setString(1, rolename);
			rs = st.executeQuery();
			ArrayList<User> users = new ArrayList<User>();
			while (rs.next()){
				users.add(readUser(rs));
			}
			return users;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	private User readUser(ResultSet rs) throws SQLException {
		User u = new User();
		u.setId(rs.getInt("id"));
		u.setCstnetId(rs.getString("cstnet_id"));
		u.setPassword(rs.getString("password"));
		u.setTrueName(rs.getString("true_name"));
		u.setUmtId(rs.getString("umt_id"));
		return u;
	}
	public UMTRole[] getRoles(String username) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(getUserRoles);
			st.setString(1, username);
			rs = st.executeQuery();
			ArrayList<UMTRole> roles = new ArrayList<UMTRole>();
			while (rs.next()){
				roles.add(readRole(rs));
			}
			return roles.toArray(new UMTRole[0]);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	private UMTRole readRole(ResultSet rs) throws SQLException {
		UMTRole role = new UMTRole(rs.getString("rolename"), rs.getString("description"));
		role.setId(rs.getInt("id"));
		return role;
	}


	public UMTRole getRole(String rolename) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(getRoleSQL);
			st.setString(1, rolename);
			rs = st.executeQuery();
			if (rs.next()){
				return readRole(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	public void addMember(int roleid, int userid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(addMemberSQL);
			st.setInt(1, roleid);
			st.setInt(2, userid);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	
	public void removeMember(int roleid, int userid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st = null;
		try {
			st = conn.prepareStatement(removeMemberSQL);
			st.setInt(1, roleid);
			st.setInt(2, userid);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		} finally {
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	private String getUserRoles= "select umt_role.* from umt_role, umt_role_member, umt_user where" +
				" umt_user.cstnet_id=? and umt_user.id=umt_role_member.userid and umt_role.id=umt_role_member.roleid";
	
	private String getMemberUsers="select umt_user.* from umt_role, umt_role_member, umt_user where " +
				" umt_role.rolename=? and umt_role.id=umt_role_member.roleid and umt_role_member.userid=umt_user.id";
	private String getRoleSQL="select * from umt_role where rolename=?";
	private String removeMemberSQL="delete from umt_role_member where roleid=? and userid=?";
	private String addMemberSQL="insert into umt_role_member(roleid, userid) values(?, ?)";
	private static final Logger LOGGER = Logger.getLogger(RoleDAOImpl.class);
	private DatabaseUtil du;
}
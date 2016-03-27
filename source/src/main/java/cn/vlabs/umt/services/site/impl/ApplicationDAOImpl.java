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
package cn.vlabs.umt.services.site.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.site.Application;
import cn.vlabs.umt.services.site.ApplicationDAO;

public class ApplicationDAOImpl implements ApplicationDAO {
	public ApplicationDAOImpl(DatabaseUtil du){
		this.du=du;
	}
	public void changePublicKey(int appid, int keyid) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(UPDATE_KEY);
			pst.setInt(1, keyid);
			pst.setInt(2, appid);
			pst.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	public int createApplication(Application app) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(CREATE_APP);
			pst.setString(1, app.getName());
			pst.setString(2, app.getUrl());
			pst.setInt(3, app.getKeyid());
			pst.setString(4, app.getServerType());
			pst.setString(5, app.getDescription());
			pst.setInt(6, app.isAllowOperate()?1:0);
			pst.execute();
			rs = pst.getGeneratedKeys();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return -1;
	}

	public void deleteApplication(String name) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(DELETE_BY_NAME);
			pst.setString(1, name);
			pst.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}

	public void deleteApplication(int appid) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(DELETE_BY_ID);
			pst.setInt(1, appid);
			pst.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}

	public Application getApplication(String name) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(GET_APPLICATION);
			pst.setString(1, name);
			rs = pst.executeQuery();
			if (rs.next()){
				return readApplication(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
	private Application readApplication(ResultSet rs) throws SQLException {
		Application app = new Application();
		app.setId(rs.getInt("id"));
		app.setKeyid(rs.getInt("keyid"));
		app.setName(rs.getString("name"));
		app.setServerType(rs.getString("serverType"));
		app.setUrl(rs.getString("url"));
		app.setDescription(rs.getString("description"));
		app.setAllowOperate(rs.getInt("allowOperate")!=0);
		return app;
	}

	public void updateApplcation(Application app) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(UPDATE_APPLICATION);
			pst.setString(1, app.getUrl());
			pst.setString(2, app.getServerType());
			pst.setInt(3, app.isAllowOperate()?1:0);
			pst.setString(4, app.getDescription());
			pst.setInt(5, app.getId());
			pst.execute();
		} catch (SQLException e) {
			LOGGER.error(e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	
	public int getApplicationCount() {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(GET_APP_COUNT);
			rs = pst.executeQuery();
			rs.next();
			return rs.getInt(1);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return 0;
	}
	public Application getApplication(int appid) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		try {
			pst = conn.prepareStatement(GET_APPLICATION_BYID);
			pst.setInt(1, appid);
			rs = pst.executeQuery();
			if (rs.next()){
				return readApplication(rs);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
	public Collection<Application> getApplications(int start, int count) {
		Connection conn = du.getConnection();
		PreparedStatement pst=null;
		ResultSet rs = null;
		ArrayList<Application> apps = new ArrayList<Application>();
		try {
			pst = conn.prepareStatement(GET_APP_IN_RANGE);
			pst.setInt(1, start);
			pst.setInt(2, count);
			rs = pst.executeQuery();
			while (rs.next()){
				apps.add(readApplication(rs));
			}
			if (apps.size()>0){
				return apps;
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
	private static final Logger LOGGER = Logger.getLogger(ApplicationDAOImpl.class);
	private static final String GET_APPLICATION_BYID="select * from umt_application where id=?";
	private static final String UPDATE_APPLICATION="update umt_application set url=?, serverType=?, allowOperate=?, description=? where id=?";
	private static final String GET_APPLICATION="select * from umt_application where name=?";
	private static final String DELETE_BY_NAME="delete from umt_application where name=?";
	private static final String DELETE_BY_ID="delete from umt_application where id=?";
	private static final String CREATE_APP="insert into umt_application(name, url, keyid, serverType, description, allowOperate) values(?,?,?,?,?,?)";
	private static final String UPDATE_KEY="update umt_application set keyid=? where id=?";
	private static final String GET_APP_COUNT="select count(*) from umt_application";
	private static final String GET_APP_IN_RANGE="select * from umt_application order by id limit ?, ?;";
	private DatabaseUtil du;
}
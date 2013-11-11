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
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.user.dao.IAppAccessDAO;


/**
 * @author lvly
 * @since 2013-3-12
 */
public class AppAccessDAO implements IAppAccessDAO{
	private DatabaseUtil du;
	
	public AppAccessDAO(DatabaseUtil du){
		this.du=du;
	}
	private static final Logger LOGGER=Logger.getLogger(AppAccessDAO.class);
	public static final String TABLE_NAME=" `umt_app_access` ";
	public static final String WHERE=" where 1=1 ";
	public static final String INSERT="insert into "+TABLE_NAME+"(`uid`,`app_name`) values(?,?)";
	public static final String SELECT="select * from "+TABLE_NAME+WHERE;
	public static final String BY_UID=" and `uid`=? ";
	public static final String BY_APP_NAME=" and `app_name`=? ";
	
	@Override
	public boolean isAccessed(int uid, String appName) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(SELECT+BY_UID+BY_APP_NAME);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, appName);
			rs=st.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return false;
	}
	@Override
	public void createAppAccess(int uid, String appName) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(INSERT);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, appName);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public List<String> getMyAppAccesses(int uid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		List<String> result=new ArrayList<String>();
		try{
			st = conn.prepareStatement(SELECT+BY_UID);
			int index=0;
			st.setInt(++index, uid);
			rs=st.executeQuery();
			while(rs.next()){
				result.add(rs.getString("app_name"));
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return result;
	}
	
}
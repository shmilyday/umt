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

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.bean.AssociateUser;
import cn.vlabs.umt.services.user.dao.IAssociateUserDAO;

/**
 * @author lvly
 * @since 2013-3-12
 */
public class AssociateUserDAO implements IAssociateUserDAO{
private DatabaseUtil du;
	
	public AssociateUserDAO(DatabaseUtil du){
		this.du=du;
	}
	private static final Logger LOGGER=Logger.getLogger(AssociateUserDAO.class);
	public static final String TABLE_NAME=" `umt_associate` ";
	public static final String WHERE=" where 1=1 ";
	public static final String INSERT="insert into "+TABLE_NAME+"(`uid`,`associate_uid`,`app_list`) values(?,?,?)";
	public static final String SELECT="select * from "+TABLE_NAME+WHERE;
	public static final String UPDATE="update "+TABLE_NAME+" set `associate_uid`=?,`app_list`=? "+WHERE;
	public static final String BY_UID=" and `uid`=? ";
	public static final String BY_ID=" and `id`=? ";
	

	@Override
	public void addAssociateUser(int uid, int associateUid, String appList) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(INSERT);
			int index=0;
			st.setInt(++index, uid);
			st.setInt(++index, associateUid);
			st.setString(++index, appList);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	private  AssociateUser readAssociateUser(ResultSet rs){
		AssociateUser ass=null;
		try{
			ass=new AssociateUser();
			String appList=rs.getString("app_list");
			if(!CommonUtils.isNull(appList)){
				ass.setAppList(appList.split(","));
			}
			ass.setAssociateUid(rs.getInt("associate_uid"));
			ass.setId(rs.getInt("id"));
			ass.setUid(rs.getInt("uid"));
			
		}catch(SQLException e){
			LOGGER.error(e.getMessage(),e);
		}
		return ass;
	}

	@Override
	public AssociateUser getAssociate(int uid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(SELECT+BY_UID);
			int index=0;
			st.setInt(++index, uid);
			rs=st.executeQuery();
			return readAssociateUser(rs);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}

	@Override
	public void updateAssociateUser(int id, int associateUid, String appList) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(SELECT+BY_UID);
			int index=0;
			st.setInt(++index, associateUid);
			st.setString(++index, appList);
			st.setInt(++index, id);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	@Override
	public boolean isAssociated(int uid) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(SELECT+" `associate_uid`=? or `uid`=? ");
			int index=0;
			st.setInt(++index, uid);
			st.setInt(++index, uid);
			rs=st.executeQuery();
			return rs.next();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return true;
	}
}
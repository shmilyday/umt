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
import cn.vlabs.umt.services.user.bean.AccessIP;
import cn.vlabs.umt.services.user.dao.IAccessIPDAO;

/**
 * @author lvly
 * @since 2013-3-15
 */
public class AccessIPDAO implements IAccessIPDAO {
	private static final Logger LOGGER = Logger.getLogger(AccessIPDAO.class);
	private DatabaseUtil du;
	public static final String SELECT="select * from `umt_access_ips` where 1=1 ";
	public static final String DELETE="delete from `umt_access_ips` where id=? ";
	public static final String INSERT="insert into `umt_access_ips`(`uid`,`ip`,`scope`,`remark`) values(?,?,?,?) ";
	public static final String BY_IP="and `ip`=? ";
	public static final String BY_SCOPE=" and `scope`=? ";
	public AccessIPDAO(DatabaseUtil du) {
		this.du = du;
	}

	@Override
	public void addAccessIp(int uid, String ip, String scope, String remark) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(INSERT);
			int index=0;
			st.setInt(++index, uid);
			st.setString(++index, ip);
			st.setString(++index, scope);
			st.setString(++index, remark );
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}

	@Override
	public List<AccessIP> getAllAccessIps() {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(SELECT);
			rs=st.executeQuery();
			List<AccessIP> result=new ArrayList<AccessIP>();
			while(rs.next()){
				result.add(readIP(rs));
			}
			return result;
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
		return null;
	}
	private AccessIP readIP(ResultSet set)throws SQLException{
		AccessIP ip=new AccessIP();
		ip.setId(set.getInt("id"));
		ip.setIp(set.getString("ip"));
		ip.setUid(set.getInt("uid"));
		ip.setRemark(set.getString("remark"));
		ip.setScope(set.getString("scope"));
		return ip;
	}

	@Override
	public boolean canAccess(String ip) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(SELECT+BY_IP);
			st.setString(1, ip);
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
	public boolean canAccess(String ip,String scope) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(SELECT+BY_IP+BY_SCOPE);
			st.setString(1, ip);
			st.setString(2, scope);
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
	public void deleteIp(int ipId) {
		Connection conn = du.getConnection();
		ResultSet rs = null;
		PreparedStatement st=null;
		try{
			st = conn.prepareStatement(DELETE);
			st.setInt(1, ipId);
			st.execute();
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(),e);
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
}
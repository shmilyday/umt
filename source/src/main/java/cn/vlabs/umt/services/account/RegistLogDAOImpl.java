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
package cn.vlabs.umt.services.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import net.duckling.cloudy.common.CommonUtils;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.domain.RegistLog;

public class RegistLogDAOImpl implements IRegistLogDAO{
	private static final Logger LOGGER=Logger.getLogger(RegistLogDAOImpl.class);
	private DatabaseUtil du;
	public RegistLogDAOImpl(DatabaseUtil du){
		this.du=du;
	}
	@Override
	public boolean canRegist(String ip) {
		String sql="select count(*) c from umt_regist_log where ip=? and FROM_UNIXTIME(UNIX_TIMESTAMP(occur_time), '%Y-%m-%d' )=FROM_UNIXTIME(UNIX_TIMESTAMP(now()), '%Y-%m-%d' )";
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(sql);
			int index=0;
			pst.setString(++index, CommonUtils.trim(ip));
			rs=pst.executeQuery();
			if(rs.next()){
				return rs.getInt("c")<3;
			}
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return true;
	}
	@Override
	public void log(RegistLog log) {
		String sql="insert into umt_regist_log(`ip`,`user_name`,`user_agent`) value(?,?,?)";
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(sql);
			int index=0;
			pst.setString(++index, log.getIp());
			pst.setString(++index, log.getUserName());
			pst.setString(++index, log.getUserAgent());
			pst.execute();
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		
	}
}

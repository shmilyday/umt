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
/**
 * 
 */
package cn.vlabs.umt.services.account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.domain.OauthLog;

/**
 * @author lvly
 * @since 2013-11-14
 */
public class OauthLogDAOImpl implements IOauthLogDAO{
	private static final Logger LOGGER=Logger.getLogger(OauthLogDAOImpl.class);
	private DatabaseUtil du;
	
	public OauthLogDAOImpl(DatabaseUtil du){
		this.du=du;
	}
	private static final String INSERT_SQL="insert into umt_oauth_log(`client_id`,`client_name`,`uid`,`result`,`desc`,`action`,`occur_time`,`ip`) values(?,?,?,?,?,?,?,?)";
	
	
	@Override
	public void addOauthLogs(List<OauthLog> logs) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		
		try{
			
			conn.setAutoCommit(false);
			pst=conn.prepareStatement(INSERT_SQL);
			for(OauthLog log:logs){
				int index=0;
				pst.setString(++index, log.getClientId());
				pst.setString(++index, log.getClientName());
				pst.setInt(++index, log.getUid());
				pst.setString(++index, log.getResult()+"");
				pst.setString(++index, log.getDesc());
				pst.setString(++index,log.getAction());
				pst.setTimestamp(++index, new Timestamp(log.getOccurTime().getTime()));
				pst.setString(++index,log.getIp());
				pst.addBatch();
			}
			pst.executeBatch();
			conn.commit();
			conn.setAutoCommit(true);
		}catch(SQLException q){
			LOGGER.error(q.getMessage(),q);
		}finally{
			DatabaseUtil.closeAll(null, pst, conn);
		}
		
	}
	@Override
	public void addOauthLog(OauthLog log) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		try{
			int index=0;
			pst=conn.prepareStatement(INSERT_SQL);
			pst.setString(++index, log.getClientId());
			pst.setString(++index, log.getClientName());
			pst.setInt(++index, log.getUid());
			pst.setString(++index, log.getResult()+"");
			pst.setString(++index, log.getDesc());
			pst.setString(++index,log.getAction());
			pst.setTimestamp(++index, new Timestamp(log.getOccurTime().getTime()));
			pst.setString(++index,log.getIp());
			pst.execute();
		}catch(SQLException q){
			LOGGER.error(q.getMessage(),q);
		}finally{
			DatabaseUtil.closeAll(null, pst, conn);
		}
	}
}

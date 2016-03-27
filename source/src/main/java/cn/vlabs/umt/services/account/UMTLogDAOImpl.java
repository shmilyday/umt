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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.ticket.impl.TicketDAOImpl;

public class UMTLogDAOImpl implements IUMTLogDAO{
	public UMTLogDAOImpl(DatabaseUtil du,Config config){
		this.du=du;
		this.config=config;
	}
	private Config config;
	public Collection<StatisticBean> getLoginCount(Date begin, Date end) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<StatisticBean> stats = new ArrayList<StatisticBean>();
		try{
			pst = conn.prepareStatement(STATISTIC_SQL);
			pst.setString(1, IAccountService.LOGIN_EVENT);
			pst.setTimestamp(2, new Timestamp(begin.getTime()));
			pst.setTimestamp(3, new Timestamp(end.getTime()));
			rs = pst.executeQuery();
			while (rs.next()){
				StatisticBean bean = new StatisticBean();
				bean.setAppname(rs.getString(1));
				bean.setCount(rs.getInt(2));
				stats.add(bean);
			}
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return stats;
	}
	
	@Override
	public UMTLog getLogSecondFromId(int uid, int logId) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement((SELECT_SQL+BY_UID+" and id<? order by id desc limit 1").replace("{0}", config.getBooleanProp("is.myself.log.spilit", false)?"_"+uid%hashLength:""));
			int index=0;
			pst.setInt(++index, uid);
			pst.setInt(++index, logId);
			rs=pst.executeQuery();
			if(rs.next()){
				return read(rs);
			}
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
	
	@Override
	public UMTLog getLogById(int uid, int logId) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement((SELECT_SQL+BY_ID+BY_UID).replace("{0}", config.getBooleanProp("is.myself.log.spilit", false)?"_"+uid%hashLength:""));
			int index=0;
			pst.setInt(++index, logId);
			pst.setInt(++index, uid);
			rs=pst.executeQuery();
			if(rs.next()){
				return read(rs);
			}
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
	public void log(UMTLog umtLog){
		
	/*	if(config.getBooleanProp("system.readonly", false)){
			return;
		}*/
		
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(LOG_SQL.replace("{0}", config.getBooleanProp("is.myself.log.spilit", false)?"_"+umtLog.getUid()%hashLength:""));
			int index=0;
			pst.setString(++index, umtLog.getEventType());
			pst.setString(++index, umtLog.getAppName());
			pst.setString(++index, umtLog.getAppUrl());
			pst.setInt(++index, umtLog.getUid());
			pst.setString(++index, umtLog.getUserIp());
			pst.setTimestamp(++index, new Timestamp(umtLog.getOccurTime().getTime()));
			pst.setString(++index, umtLog.getBrowserType());
			pst.setString(++index, umtLog.getRemark());
			pst.setString(++index, umtLog.getCountry());
			pst.setString(++index, umtLog.getProvince());
			pst.setString(++index, umtLog.getCity());
			pst.setString(++index, umtLog.isCstnetUnit()?"true":"false");
			pst.setString(++index, umtLog.getUnitName());
			pst.setString(++index,umtLog.isFromDip()?"true":"false");
			pst.execute();
			rs = pst.getGeneratedKeys();
			rs.next();
			umtLog.setId(rs.getInt(1));
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}

	private int hashLength=10;
	private static final String STATISTIC_SQL="select appname, count(id) from umt_log where eventType=? and occurTime>? and occurTime<? group by appname;";
	private static final String LOG_SQL="insert into umt_log{0}(eventType, appname, appurl, uid, ipaddress, occurTime, browserType, remark,country,province,city,is_cstnet_unit,unit_name,from_dip) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	private static final String SELECT_SQL="select * from umt_log{0} where 1=1";
	private static final String UPDATE_SQL="update umt_log{0} set send_warn_email=? where 1=1 ";
	private static final String BY_EVENT_TYPE=" and eventType=? ";
	private static final String BY_UID=" and uid=? ";
	private static final String BY_ID=" and id=? ";
	private static final String ORDER_BY_OCCUR_TIME=" order by id desc ";
	private static final String TOP_TEN="limit 0,? ";
	private static final Logger LOGGER = Logger.getLogger(TicketDAOImpl.class);
	private DatabaseUtil du;
	
	
	@Override
	public List<UMTLog> getLastLogByEventType(int uid, String eventType){
		return getLastLogByEventTypeLimit(uid, eventType,10);	
	}
	private UMTLog read(ResultSet rs) throws SQLException{
		UMTLog umtLog=new UMTLog();
		umtLog.setId(rs.getInt("id"));
		umtLog.setAppName(rs.getString("appname"));
		umtLog.setAppUrl(rs.getString("appurl"));
		umtLog.setBrowserType(rs.getString("browserType"));
		umtLog.setEventType(rs.getString("eventType"));
		umtLog.setOccurTime(rs.getTimestamp("occurTime"));
		umtLog.setRemark(rs.getString("remark"));
		umtLog.setUserIp(rs.getString("ipaddress"));
		umtLog.setUid(rs.getInt("uid"));
		umtLog.setCountry(rs.getString("country"));
		umtLog.setProvince(rs.getString("province"));
		umtLog.setCity(rs.getString("city"));
		umtLog.setSendWarnEmail(rs.getBoolean("send_warn_email"));
		umtLog.setCstnetUnit(rs.getBoolean("is_cstnet_unit"));
		umtLog.setUnitName(rs.getString("unit_name"));
		umtLog.setFromDip(rs.getBoolean("from_dip"));
		return umtLog;
	}
	@Override
	public List<UMTLog> getLastLogByEventTypeLimit(int uid, String eventType,
			int size) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(SELECT_SQL.replace("{0}", config.getBooleanProp("is.myself.log.spilit", false)?"_"+uid%hashLength:"")+BY_UID+BY_EVENT_TYPE+ORDER_BY_OCCUR_TIME+TOP_TEN);
			int index=0;
			pst.setInt(++index, uid);
			pst.setString(++index, eventType);
			pst.setInt(++index, size);
			rs=pst.executeQuery();
			List<UMTLog> logs=new ArrayList<UMTLog>();
			while(rs.next()){
				
				logs.add(read(rs));
			}
			return logs;
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		
		return null;
	}
	@Override
	public void updateSendWarnMail(int logId, int uid) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(UPDATE_SQL.replace("{0}", config.getBooleanProp("is.myself.log.spilit", false)?"_"+uid%hashLength:"")+BY_ID+BY_UID);
			int index=0;
			pst.setString(++index, "true");
			pst.setInt(++index, logId);
			pst.setInt(++index, uid);
			pst.executeUpdate();
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	
	@Override
	public void updateGEOInfo(UMTLog log) {
		String sql="update umt_log{0} set `country`=?,`province`=?,`city`=?,`unit_name`=?,`is_cstnet_unit`=?,`from_dip`=? where id=?";
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(sql.replace("{0}", config.getBooleanProp("is.myself.log.spilit", false)?"_"+log.getUid()%hashLength:""));
			int index=0;
			pst.setString(++index, log.getCountry());
			pst.setString(++index, log.getProvince());
			pst.setString(++index, log.getCity());
			pst.setString(++index, log.getUnitName());
			pst.setBoolean(++index, log.isCstnetUnit());
			pst.setBoolean(++index, log.isFromDip());
			pst.setInt(++index, log.getId());
			pst.execute();
			
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
		
}
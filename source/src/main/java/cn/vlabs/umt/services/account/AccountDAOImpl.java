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
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.ticket.impl.TicketDAOImpl;

public class AccountDAOImpl implements IAccountDAO{
	public AccountDAOImpl(DatabaseUtil du){
		this.du=du;
	}
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
			LOGGER.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return stats;
	}

	public void log(String eventType, String appname, String appurl, int uid, String userip, Date logintime, String browserType, String remark) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(LOG_SQL);
			int index=0;
			pst.setString(++index, eventType);
			pst.setString(++index, appname);
			pst.setString(++index, appurl);
			pst.setInt(++index, uid);
			pst.setString(++index, userip);
			pst.setTimestamp(++index, new Timestamp(logintime.getTime()));
			pst.setString(++index, browserType);
			pst.setString(++index, remark);
			pst.execute();
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	private static final String STATISTIC_SQL="select appname, count(id) from umt_log where eventType=? and occurTime>? and occurTime<? group by appname;";
	private static final String LOG_SQL="insert into umt_log(eventType, appname, appurl, uid, ipaddress, occurTime, browserType, remark) values(?,?,?,?,?,?,?,?)";
	private static final String SELECT_SQL="select * from umt_log where 1=1";
	private static final String BY_EVENT_TYPE=" and eventType=? ";
	private static final String BY_UID=" and uid=? ";
	private static final String ORDER_BY_OCCUR_TIME=" order by id desc ";
	private static final String TOP_TEN="limit 0,10 ";
	private static final Logger LOGGER = Logger.getLogger(TicketDAOImpl.class);
	private DatabaseUtil du;
	
	@Override
	public List<UMTLog> getLastLogByEventType(int uid, String eventType) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(SELECT_SQL+BY_UID+BY_EVENT_TYPE+ORDER_BY_OCCUR_TIME+TOP_TEN);
			int index=0;
			pst.setInt(++index, uid);
			pst.setString(++index, eventType);
			rs=pst.executeQuery();
			List<UMTLog> logs=new ArrayList<UMTLog>();
			while(rs.next()){
				UMTLog umtLog=new UMTLog();
				umtLog.setAppName(rs.getString("appname"));
				umtLog.setAppUrl(rs.getString("appurl"));
				umtLog.setBrowserType(rs.getString("browserType"));
				umtLog.setEventType(rs.getString("eventType"));
				umtLog.setOccurTime(rs.getTimestamp("occurTime"));
				umtLog.setRemark(rs.getString("remark"));
				umtLog.setUserIp(rs.getString("ipaddress"));
				umtLog.setUid(rs.getInt("uid"));
				logs.add(umtLog);
			}
			return logs;
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
}
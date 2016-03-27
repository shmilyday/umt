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

public class AccountDAOImpl implements IAccountDAO{
	public AccountDAOImpl(DatabaseUtil du,Config config){
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
	


	private int hashLength=10;
	private static final String STATISTIC_SQL="select appname, count(id) from umt_log where eventType=? and occurTime>? and occurTime<? group by appname;";
	private static final Logger LOGGER = Logger.getLogger(TicketDAOImpl.class);
	private DatabaseUtil du;
	
	
	@Override
	public List<UMTLog> getMyPreference(int uid) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement("select * from umt_common_use_geo where uid=?");
			int index=0;
			pst.setInt(++index, uid);
			rs=pst.executeQuery();
			List<UMTLog> geos=new ArrayList<UMTLog>();
			while(rs.next()){
				UMTLog g=new UMTLog();
				g.setId(rs.getInt("id"));
				g.setCountry(rs.getString("country"));
				g.setProvince(rs.getString("province"));
				g.setCity(rs.getString("city"));
				g.setUid(rs.getInt("uid"));
				geos.add(g);
			}
			return geos;
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}
	@Override
	public void removeCommonGEO(int id, int uid) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement("delete from umt_common_use_geo where uid=? and id=?");
			int index=0;
			pst.setInt(++index, uid);
			pst.setInt(++index, id);
			pst.execute();
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	@Override
	public List<UMTLog> readCommonGEO(int uid) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			String sql= "select distinct uid, country,province,city from umt_log{0} where uid=? and eventType='"+UMTLog.EVENT_TYPE_LOG_IN+"' and country is not null and province is not null and city is not null order by id";
			pst = conn.prepareStatement(sql.replace("{0}", config.getBooleanProp("is.myself.log.spilit", false)?"_"+uid%hashLength:""));
			int index=0;
			pst.setInt(++index, uid);
			rs=pst.executeQuery();
			List<UMTLog> logs=new ArrayList<UMTLog>();
			while(rs.next()){
				UMTLog g=new UMTLog();
				g.setCountry(rs.getString("country"));
				g.setProvince(rs.getString("province"));
				g.setCity(rs.getString("city"));
				g.setUid(rs.getInt("uid"));
				logs.add(g);
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
	public void addCommonGEO(UMTLog umtLog) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement("insert into umt_common_use_geo(`uid`,`country`,`province`,`city`) values(?,?,?,?)");
			int index=0;
			pst.setInt(++index, umtLog.getUid());
			pst.setString(++index, umtLog.getCountry());
			pst.setString(++index,umtLog.getProvince());
			pst.setString(++index, umtLog.getCity());
			pst.execute();
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		
	}
	@Override
	public int countCommonGEO(int uid) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement("select count(*) c from  umt_common_use_geo where `uid`=? ");
			int index=0;
			pst.setInt(++index, uid);
			rs=pst.executeQuery();
			rs.next();
			return rs.getInt("c");
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return 0;
	}
	@Override
	public boolean isExitsCommonGEO(UMTLog umtLog) {
		Connection conn = du.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement("select count(*) c from  umt_common_use_geo where `uid`=? and `country`=? and `province`=? and `city`=?");
			int index=0;
			pst.setInt(++index, umtLog.getUid());
			pst.setString(++index, umtLog.getCountry());
			pst.setString(++index,umtLog.getProvince());
			pst.setString(++index, umtLog.getCity());
			rs=pst.executeQuery();
			rs.next();
			return rs.getInt("c")>0;
		} catch (SQLException e) {
			LOGGER.error("记录记账信息时发生错误:"+e.getMessage());
			LOGGER.error("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return false;
	}
		
}
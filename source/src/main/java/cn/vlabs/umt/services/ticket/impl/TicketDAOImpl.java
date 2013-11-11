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
package cn.vlabs.umt.services.ticket.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.datasource.DatabaseUtil;
import cn.vlabs.umt.services.ticket.InternalTicket;
import cn.vlabs.umt.services.ticket.TicketDAO;

public class TicketDAOImpl implements TicketDAO {
	public TicketDAOImpl(DatabaseUtil dbutil){
		this.dbutil=dbutil;
	}
	public InternalTicket load(long tickid) {
		Connection conn = dbutil.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(LOAD_SQL);
			pst.setLong(1, tickid);
			rs = pst.executeQuery();
			if (rs.next()){
				return readTicket(rs);
			}
		} catch (SQLException e) {
			log.error("读取Ticket时发生错误:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return null;
	}

	public void remove(long ticketid) {
		Connection conn = dbutil.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(DELETE_SQL);
			pst.setLong(1, ticketid);
			pst.execute();
		} catch (SQLException e) {
			log.error("删除Ticket时发生错误:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}

	public long save(InternalTicket ticket) {
		Connection conn = dbutil.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(CREATE_SQL);
			pst.setInt(1, ticket.getType());
			pst.setTimestamp(2, new Timestamp(ticket.getCreateTime().getTime()));
			pst.setString(3, ticket.getRandom());
			pst.setString(4, ticket.getSessionid());
			pst.setString(5, ticket.getExtra());
			pst.setString(6, ticket.getUserIp());
			pst.execute();
			rs = pst.getGeneratedKeys();
			rs.next();
			return rs.getLong(1);
		} catch (SQLException e) {
			log.error("创建Ticket时发生错误:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
		return 0;
	}
	
	private InternalTicket readTicket(ResultSet rs) throws SQLException{
		InternalTicket ticket = new InternalTicket();
		ticket.setId(rs.getLong("id"));
		ticket.setCreateTime(rs.getTimestamp("createTime"));
		ticket.setRandom(rs.getString("random"));
		ticket.setSessionid(rs.getString("sessionid"));
		ticket.setExtra(rs.getString("extra"));
		ticket.setType(rs.getInt("type"));
		ticket.setUserIp(rs.getString("userip"));
		return ticket;
		
	}
	public void removeBefore(Date createTime) {
		Connection conn = dbutil.getConnection();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try{
			pst = conn.prepareStatement(REMOVE_BEFORE);
			pst.setTimestamp(1, new Timestamp(createTime.getTime()));
			pst.execute();
		} catch (SQLException e) {
			log.error("删除Ticket时发生错误:"+e.getMessage());
			log.debug("详细信息:", e);
		}finally{
			DatabaseUtil.closeAll(rs, pst, conn);
		}
	}
	private DatabaseUtil dbutil;
	private String CREATE_SQL="insert umt_ticket(type, createTime, random, sessionid, extra, userip) values(?,?,?,?,?,?)";
	private String DELETE_SQL="delete from umt_ticket where id=?";
	private String LOAD_SQL="select * from umt_ticket where id=?";
	private String REMOVE_BEFORE="delete from umt_ticket where createTime<?";
	private Logger log = Logger.getLogger(TicketDAOImpl.class);
}
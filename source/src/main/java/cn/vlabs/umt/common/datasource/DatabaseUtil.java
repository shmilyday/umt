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
package cn.vlabs.umt.common.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;

import cn.vlabs.umt.common.util.Config;


public class DatabaseUtil {
	public DatabaseUtil(Config config){
		BasicDataSource ds = new BasicDataSource();
		
		ds.setMaxActive(config.getInt("database.maxconn", 10));
		ds.setMaxIdle(config.getInt("database.maxidle", 3));
		ds.setMaxWait(100);
		
		ds.setUsername(config.getStringProp("database.username", null));
		ds.setPassword(config.getStringProp("database.password", null));
		ds.setDriverClassName(config.getStringProp("database.driver", null));
		ds.setUrl(config.getStringProp("database.conn-url", null));
		ds.setTimeBetweenEvictionRunsMillis(3600000);
		ds.setMinEvictableIdleTimeMillis(1200000);
		datasource=ds;
	}

	/**
	 * 关闭连接池
	 * 
	 */
	public void close() {
		if (datasource != null) {
			try {
				datasource.close();
			} catch (SQLException e) {
				
			}
		}
	}

	/**
	 * 获取一个配置好的数据库连接。
	 * 
	 * @return java.sql.Connection对象。
	 */
	public Connection getConnection() {
		try {
			return datasource.getConnection();
		} catch (SQLException e) {
			throw new DAORuntimeException(e);
		}
	}
	/**
	 * 关闭一个Statement对象，辅助函数，避免重复编码。
	 * @param st Statement对象
	 */
	public static void closeStatement(Statement st){
		if (st!=null){
			try {
				st.close();
			} catch (SQLException e) {
				log.error(st, e);
			}
		}
	}
	/**
	 * 关闭一个数据库连接，辅助函数，避免重复编码。
	 * @param conn 数据库连接
	 */
	public static void closeConnection(Connection conn){
		if (conn!=null){
			try {
				conn.close();
			} catch (SQLException e) {
				log.error(conn, e);
			}
		}
	}
	/**
	 * 关闭一个结果集，辅助函数，避免重复编码。
	 * @param rs 数据库结果集
	 */
	public static void closeResultSet(ResultSet rs){
		if (rs!=null){
			try {
				rs.close();
			} catch (SQLException e) {
				log.error(rs, e);
			}
		}
	}
	/**
	 * 集成调用，一次性关闭所有的内容。
	 * @param rs
	 * @param st
	 * @param conn
	 */
	public static void closeAll(ResultSet rs, Statement st, Connection conn) {
		closeResultSet(rs);
		closeStatement(st);
		closeConnection(conn);
	}
	
	private BasicDataSource datasource;
	private static final Logger log=Logger.getLogger(DatabaseUtil.class);
}
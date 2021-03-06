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

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.ui.PathMapper;


public class CreateTable {
	public CreateTable(PathMapper mapper, DatabaseUtil du){
		this.mapper=mapper;
		this.du=du;
	}
	public void createTable(){
		String sqlFile = mapper.getRealPath("/WEB-INF/conf/duckling_v7.sql");
		FileInputStream in;
		try {
			in = new FileInputStream(sqlFile);
			SQLFileReader reader = new SQLFileReader();
			execSQL(reader.loadSql(in));
		} catch (Exception e) {
			LOGGER.error("无法创建数据库表:"+e.getMessage());
		}
	}
	
	public boolean isTableExist(){
		String sql="select count(*) from umt_user";
		Connection conn = du.getConnection();
		Statement st = null;
		ResultSet rs = null;
		try{
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			return rs.next();
		}catch (SQLException e){
			LOGGER.debug("判断数据库是否存在时，发生错误", e);
			return false;
		}finally{
			DatabaseUtil.closeAll(rs, st, conn);
		}
	}
	
	private void execSQL(List<String> list) throws SQLException{
		if (list!=null){
			Connection conn = du.getConnection();
			Statement st = conn.createStatement();
			for (String sql:list){
				st.execute(sql);
			}
			DatabaseUtil.closeAll(null, st, conn);
		}
	}
	
	private PathMapper mapper;
	private DatabaseUtil du;
	private static final Logger LOGGER = Logger.getLogger(CreateTable.class);
}
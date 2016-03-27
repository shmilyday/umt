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
package cn.vlabs.umt.common.datasource;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;


public class CreateOauthTokenTable {
	public CreateOauthTokenTable(DatabaseUtil du){
		this.du=du;
	}
	public void createTable(ServletContext context){
		String sqlFile = context.getRealPath("/WEB-INF/conf/duckling_v7_oauth_token.sql");
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
		return isTableExist("umt_oauth_token");
	}
	
	  private boolean isTableExist(String tablename) {
	        boolean found = false;
	        PreparedStatement pst = null;
	        final String checkSql = "show tables like '"+tablename+"'";
	        try {
	        	Connection con = du.getConnection();
	            pst = con.prepareStatement("");
	            ResultSet rs = null;
	            rs = pst.executeQuery(checkSql);
	            if (rs.next()) {
	                found = true;
	            }
	        } catch (SQLException e) {
	            return false;
	        } finally {
	            if (pst != null) {
	                try {
	                    pst.close();
	                } catch (SQLException e) {
	                }
	            }
	        }
	        return found;
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
	
	private DatabaseUtil du;
	private static final Logger LOGGER = Logger.getLogger(CreateOauthTokenTable.class);
}
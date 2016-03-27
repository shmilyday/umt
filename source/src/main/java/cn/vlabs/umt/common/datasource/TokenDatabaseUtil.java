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

import org.apache.commons.dbcp.BasicDataSource;

import cn.vlabs.umt.common.util.Config;

public class TokenDatabaseUtil extends DatabaseUtil {

	public TokenDatabaseUtil(Config config) {
		super(config);
	}
	
	public BasicDataSource getDataScource(Config config){
		BasicDataSource ds = new BasicDataSource();
		ds.setMaxActive(config.getInt("database.maxconn", 10));
		ds.setMaxIdle(config.getInt("database.maxidle", 3));
		ds.setMaxWait(100);
		
		ds.setUsername(config.getStringProp("database.token.username", null));
		ds.setPassword(config.getStringProp("database.token.password", null));
		ds.setDriverClassName(config.getStringProp("database.driver", null));
		ds.setUrl(config.getStringProp("database.token.conn-url", null));
		ds.setTimeBetweenEvictionRunsMillis(3600000);
		ds.setMinEvictableIdleTimeMillis(1200000);
		return ds;
	}
	
	

}

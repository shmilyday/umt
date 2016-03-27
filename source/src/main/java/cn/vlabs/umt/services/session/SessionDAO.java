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
package cn.vlabs.umt.services.session;

import java.util.Collection;
import java.util.Date;

import cn.vlabs.umt.services.session.impl.LoginRecord;


public interface SessionDAO {
	/**
	 * 创建登录记录
	 * @param gsession
	 * @param username
	 * @param appname
	 * @param appurl
	 * @param lastupdate
	 * @param appsessionid
	 */
	void createAppSession(String username, String appname, String userip, String logouturl, String appType, Date lastupdate, String appsessionid);
	/**
	 * 删除所有的登录记录
	 * @param username
	 */
	void removeAllSession(String username, String userip);
	/**
	 * 删除用户在应用上的登录记录。
	 * @param username
	 * @param appname
	 */
	void removeSession(String username, String appname, String userip);
	/**
	 * 刷新登录记录
	 * @param username
	 * @param appname
	 * @param lastUpdate
	 */
	void refreshSession( String sessionid, String appname, Date lastUpdate);
	/**
	 * 删除超时的全局Session
	 * @param time
	 */
	void removeTimeOutSession(Date time);
	/**
	 * 获取用户在某个客户端上的登录信息
	 * @param username 用户名
	 * @return 所有的登录记录
	 */
	Collection<LoginRecord> getAllAppSession(String username, String userip);
	/**
	 * 查询用户所有的登录Session.
	 * @param username 用户名
	 * @return 所有的登录记录
	 */
	Collection<LoginRecordVO> getAllAppSession(String username);
	/**
	 * 查询某个ID
	 * @param id
	 * @return
	 */
	LoginRecord getLoginRecord(int id);
	/**
	 * 删除某个Session
	 * @param id
	 */
	void removeSession(int id);
}
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


public interface SessionService {
	/**
	 * 登录系统
	 * @param ticket
	 */
	void login(String appname, String username, String userip, String logoutURL, String appType, String sessionid);
	/**
	 * 退出系统
	 * @param appname
	 * @param username
	 */
	void logout(String appname, String username, String userip, String sid);
	/**
	 * 刷新登录时间
	 * @param username
	 * @param appname
	 */
	void refresh(String sessionid, String appname);
	/**
	 * 清除过期Session
	 *
	 */
	void cleanSession();
	/**
	 * 查询用户当前的登录状况
	 * @param username
	 */
	 Collection<LoginRecordVO> getAllSessions(String username);
	 /**
	  * 让某个登录记录退出
	  * @param id
	  */
	void logout(int id);
}
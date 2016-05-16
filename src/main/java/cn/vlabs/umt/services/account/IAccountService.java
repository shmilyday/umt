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

import java.util.Collection;
import java.util.Date;
import java.util.List;

import cn.vlabs.umt.domain.UMTLog;

public interface IAccountService {
	static final String BEAN_ID="AccountService";
	static final String LOGIN_EVENT="login";
	static final String LOGOUT_EVENT="logout";
	/**
	 * 记录登录事件
	 * @param appname		应用名称
	 * @param appurl		应用的URL
	 * @param username		用户名
	 * @param userip		用户的IP地址
	 * @param logintime		登录时间
	 * @param browserType 	浏览器类型
	 * @param remark		备注信息
	 */
	void login(String appname, String appurl, int uid, String userip, Date logintime, String browserType,  String remark);
	/**
	 * 记录登出事件
	 * @param appname		应用名称
	 * @param appurl		应用的URL
	 * @param username		用户名
	 * @param userip		用户的IP地址
	 * @param logintime		登录时间
	 * @param browserType 	浏览器类型
	 * @param remark		备注信息
	 */
	void logout(String appname, String appurl, int uid, String userip, Date logintime, String broserType,  String remark);
	/**
	 * 获得登录统计
	 * @param begin 统计开始时间
	 * @param end	统计结束时间
	 * @return	按照应用分类的统计结果。
	 */
	Collection<StatisticBean> getLoginCount(Date begin, Date end);
	
	/**
	 * 获得日志信息，取最近十条
	 * @param uid 用户id
	 * @param eventType 出发事件，想减UMTLog
	 **/
	List<UMTLog> getTopTenLogByEventType(int uid,String eventType);
	
	UMTLog getLastLogByEventType(int uid,String eventType);
	
	/**
	 * 记录日志
	 * @param eventType 出发事件，请看UMTLog.EVENT_TYPE_XXX
	 * @param username 登陆用户账户
	 * @param userip 操作ip
	 * @param browserType 浏览器类型
	 * */
	void log(String eventType,int uid, String userip, String browserType);
	
	List<UMTLog> getMyPreCommonUseGeos(int uid);
	void removeCommonGEO(int id, int uid);
	List<UMTLog> readCommonGEO(int uid);
	void addCommonGEO(UMTLog umtLog);
	int countCommonGEO(int uid);
	boolean isExitsCommonGEO(UMTLog umtLog);
}
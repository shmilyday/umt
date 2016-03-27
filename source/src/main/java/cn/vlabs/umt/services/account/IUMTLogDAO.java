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

public interface IUMTLogDAO {
	/**
	 * 记录一个记账事件
	 */
	public void log(UMTLog umtLog);
	/**
	 * 获得登录统计
	 * @param begin 统计开始时间
	 * @param end	统计结束时间
	 * @return	按照应用分类的统计结果。
	 */
	Collection<StatisticBean> getLoginCount(Date begin, Date end);
	/**
	 * 获得日志信息，取最近十条
	 * @param username 用户登录名
	 * @param eventType 出发事件，想减UMTLog
	 **/
	List<UMTLog> getLastLogByEventType(int uid,String eventType);
	
	List<UMTLog> getLastLogByEventTypeLimit(int uid,String eventType,int size);
	
	void updateSendWarnMail(int logId,int uid);
	UMTLog getLogById(int uid, int logId);
	UMTLog getLogSecondFromId(int uid, int logId);
	//更新GEO信息，先插入日志，后更新的方法做
	public void updateGEOInfo(UMTLog log);
}
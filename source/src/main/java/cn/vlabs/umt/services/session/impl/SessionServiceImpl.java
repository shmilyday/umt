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
package cn.vlabs.umt.services.session.impl;

import java.util.Collection;
import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import cn.vlabs.umt.common.schedule.ScheduleService;
import cn.vlabs.umt.services.session.LoginRecordVO;
import cn.vlabs.umt.services.session.SessionDAO;
import cn.vlabs.umt.services.session.SessionService;

public class SessionServiceImpl implements SessionService {
	public SessionServiceImpl(SessionDAO sd, ScheduleService scheduler){
		this.sd=sd;
		this.scheduler=scheduler;
	}
	public void setTimeOut(int timeout){
		this.timeOut=timeout;
	}
	public void setUp(){
		scheduler.schedule(cleanInterval, "SessionMinuteTrigger", "SessionCleanup", CleanupJob.class, "service", this);
	}
	public void tearDown(){
		scheduler.removeJob("SessionCleanup", "SessionMinuteTrigger");
	}
	public void setInterval(int interval){
		cleanInterval=interval;
	}
	public void cleanSession() {
		//对于自然超时的情况我们不做记录
		Date time = DateUtils.addMinutes(new Date(), -timeOut);
		sd.removeTimeOutSession(time);
	}
	

	public void login(String appname, String username, String userip, String logoutURL,
			String appType, String sessionid) {
		if (appname!=null && sessionid!=null){
			synchronized(sd){
				sd.removeSession(username, appname, sessionid);
				sd.createAppSession(username, appname,userip, logoutURL, appType, new Date(), sessionid);
			}
		}
	}

	public void logout(String appname, String username, String userip, String sid) {
		if (appname!=null){
			Date lastupdate = DateUtils.addMinutes(new Date(), timeOut);
			Collection<LoginRecord> records =null;
			synchronized(sd){
				records = sd.getAllAppSession(username, userip);
				sd.removeAllSession(username, userip);
			}
			for (LoginRecord record:records){
				if (!isSameSession(appname, sid, record)){
					record.logout(lastupdate);
				}
			}
		}
	}
	
	private boolean isSameSession(String appname, String sid, LoginRecord record) {
		return appname.equals(record.getAppname())&&sid.equals(record.getAppSessionid());
	}

	public void refresh(String sessionid, String appname) {
		sd.refreshSession(sessionid, appname, new Date());
	}
	
	public Collection<LoginRecordVO> getAllSessions(String username) {
		return sd.getAllAppSession(username);
	}
	
	public void logout(int id) {
		LoginRecord record = sd.getLoginRecord(id);
		if (record!=null){
			sd.removeSession(id);
			Date now = new Date();
			Date deadline =DateUtils.addMinutes(now, timeOut);
			record.logout(deadline);
		}
	}

	//全局Session失效时间，缺省为35分钟，单位：分钟
	private int timeOut=35;
	//清理间隔
	private ScheduleService scheduler;
	private int cleanInterval=10;
	private SessionDAO sd;
}
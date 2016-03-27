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

import net.duckling.cloudy.common.CommonUtils;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.job.JobThread;
import cn.vlabs.umt.common.job.impl.DiffrentRegisterJob;
import cn.vlabs.umt.common.job.impl.UMTLogGEOJob;
import cn.vlabs.umt.common.mail.MessageSender;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;


public class AccountServiceImpl implements IAccountService {
	private static final Logger LOGGER=Logger.getLogger(AccountServiceImpl.class);
	private MessageSender mailSender;
	private UserService userService;
	public AccountServiceImpl(IAccountDAO ad,IUMTLogDAO umtLogDao,MessageSender mailSender,UserService userService){
		this.ad=ad;
		this.mailSender=mailSender;
		this.userService=userService;
		this.umtLogDao=umtLogDao;
	}
	public Collection<StatisticBean> getLoginCount(Date begin, Date end) {
		return umtLogDao.getLoginCount(begin, end);
	}

	public void login(String appname, String appurl, int uid,
			String userip, Date logintime, String browserType, String remark) {
		UMTLog umtLog=new UMTLog();
		umtLog.setEventType(IAccountService.LOGIN_EVENT);
		umtLog.setAppName(appname);
		umtLog.setAppUrl(appurl);
		umtLog.setUid(uid);
		umtLog.setUserIp(userip);
		umtLog.setOccurTime(logintime);
		umtLog.setBrowserType(browserType);
		umtLog.setRemark(remark);
		umtLogDao.log(umtLog);
		JobThread.addJobThread(new DiffrentRegisterJob(mailSender, userService, ad,umtLogDao, uid,umtLog.getId()));
	}

	public void logout(String appname, String appurl, int uid,
			String userip, Date logintime, String browserType, String remark) {
		UMTLog umtLog=new UMTLog();
		umtLog.setEventType(IAccountService.LOGOUT_EVENT);
		umtLog.setAppName(appname);
		umtLog.setAppUrl(appurl);
		umtLog.setUid(uid);
		umtLog.setUserIp(userip);
		umtLog.setOccurTime(logintime);
		umtLog.setBrowserType(browserType);
		umtLog.setRemark(remark);
		umtLogDao.log(umtLog);
		JobThread.addJobThread(new UMTLogGEOJob(umtLogDao, umtLog.getId(), uid));
	}
	/**
	 * 获得日志信息，取最近一条
	 * @param username 用户登录名
	 * @param eventType 出发事件，想减UMTLog
	 **/
	public List<UMTLog> getTopTenLogByEventType(int uid,String eventType){
		return umtLogDao.getLastLogByEventType(uid, eventType);
	}
	@Override
	public void log(String eventType,int uid, String userip, String browserType) {
		UMTLog umtLog=new UMTLog();
		umtLog.setEventType(eventType);
		umtLog.setAppName(User.USER_TYPE_UMT);
		umtLog.setAppUrl(null);
		umtLog.setUid(uid);
		umtLog.setUserIp(userip);
		umtLog.setOccurTime(new Date());
		umtLog.setBrowserType(browserType);
		umtLog.setRemark("");
		umtLogDao.log(umtLog);
		switch(eventType){
		case UMTLog.EVENT_TYPE_LOG_IN:{
			JobThread.addJobThread(new DiffrentRegisterJob(mailSender, userService, ad,umtLogDao, uid,umtLog.getId()));
			return;
		}default:{
			JobThread.addJobThread(new UMTLogGEOJob(umtLogDao, umtLog.getId(), uid));
			return;
		}
		}
	}
	@Override
	public UMTLog getLastLogByEventType(int uid, String eventType) {
		return CommonUtils.first(umtLogDao.getLastLogByEventTypeLimit(uid, eventType, 1));
	}
	@Override
	public List<UMTLog> getMyPreCommonUseGeos(int uid) {
		return ad.getMyPreference(uid);
	}
	@Override
	public void removeCommonGEO(int id, int uid) {
		ad.removeCommonGEO(id,uid);
	}
	@Override
	public List<UMTLog> readCommonGEO(int uid) {
		return ad.readCommonGEO(uid);
	}
	@Override
	public void addCommonGEO(UMTLog umtLog) {
		ad.addCommonGEO(umtLog);
	}
	@Override
	public boolean isExitsCommonGEO(UMTLog umtLog) {
		return ad.isExitsCommonGEO(umtLog);
	}
	@Override
	public int countCommonGEO(int uid) {
		return ad.countCommonGEO(uid);
	}
	private IAccountDAO ad;
	private IUMTLogDAO umtLogDao;
}
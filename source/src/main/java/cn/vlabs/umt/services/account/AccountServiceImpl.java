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
package cn.vlabs.umt.services.account;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.user.bean.User;

public class AccountServiceImpl implements IAccountService {
	public AccountServiceImpl(IAccountDAO ad){
		this.ad=ad;
	}
	public Collection<StatisticBean> getLoginCount(Date begin, Date end) {
		return ad.getLoginCount(begin, end);
	}

	public void login(String appname, String appurl, int uid,
			String userip, Date logintime, String browserType, String remark) {
		ad.log(IAccountService.LOGIN_EVENT, appname, appurl, uid, userip, logintime, browserType, remark);
	}

	public void logout(String appname, String appurl, int uid,
			String userip, Date logintime, String browserType, String remark) {
		ad.log(IAccountService.LOGOUT_EVENT, appname, appurl, uid, userip, logintime, browserType, remark);
	}
	/**
	 * 获得日志信息，取最近一条
	 * @param username 用户登录名
	 * @param eventType 出发事件，想减UMTLog
	 **/
	public List<UMTLog> getTopTenLogByEventType(int uid,String eventType){
		return ad.getLastLogByEventType(uid, eventType);
	}
	@Override
	public void log(String eventType,int uid, String userip, String browserType) {
		ad.log(eventType, User.USER_TYPE_UMT, null, uid, userip, new Date(), browserType, "");
	}
	
	private IAccountDAO ad;
}
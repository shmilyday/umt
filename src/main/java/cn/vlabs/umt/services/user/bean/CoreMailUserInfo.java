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
/**
 * 
 */
package cn.vlabs.umt.services.user.bean;

import java.text.SimpleDateFormat;
import java.util.Date;

import cn.vlabs.umt.common.util.CommonUtils;

/**
 * @author lvly
 * @since 2013-12-19
 */
public class CoreMailUserInfo {
	private String email;
	private String status;
	public static final String STATUS_NORMAL="0";
	public static final String STATUS_STOP="1";
	public static final String STATUS_LOCK="4";
	private String expireTime;
	private String trueName;
	
	public User getUser(){
		User user=new User();
		user.setCstnetId(email);
		user.setTrueName(trueName);
		user.setType(User.USER_TYPE_CORE_MAIL);
		return user;
	}
	public boolean isExpired(){
		if(CommonUtils.isNull(expireTime)){
			return false;
		}
		expireTime=expireTime.substring(0,10);
		String today=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		return expireTime.compareTo(today)<=0;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(String expireTime) {
		this.expireTime = expireTime;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

}

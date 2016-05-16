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
package cn.vlabs.umt.ui.controller;

import cn.vlabs.umt.common.util.CommonUtils;
public class AppRegistEmailForm {

	private String registURL;
	private String loginURL;
	private String appname;

	public String getRegistURL() {
		return CommonUtils.trim(registURL);
	}

	public void setRegistURL(String registURL) {
		this.registURL = registURL;
	}

	public String getLoginURL() {
		return CommonUtils.trim(loginURL);
	}

	public void setLoginURL(String loginURL) {
		this.loginURL = loginURL;
	}

	public String getAppname() {
		return CommonUtils.trim(appname);
	}

	public void setAppname(String appName) {
		this.appname = appName;
	}

}
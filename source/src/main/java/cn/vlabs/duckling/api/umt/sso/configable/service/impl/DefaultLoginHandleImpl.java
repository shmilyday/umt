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
package cn.vlabs.duckling.api.umt.sso.configable.service.impl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.vlabs.commons.principal.UserPrincipalV7;
import cn.vlabs.duckling.api.umt.sso.UserContext;
import cn.vlabs.duckling.api.umt.sso.configable.msg.Message;
import cn.vlabs.duckling.api.umt.sso.configable.service.ILoginHandle;

/**
 * 默认扩展类
 * @author lvly
 * @since 2013-2-5
 */
public class DefaultLoginHandleImpl implements ILoginHandle {
	private static final Logger LOGGER=Logger.getLogger(DefaultLoginHandleImpl.class);


	@Override
	public void onLogOut(HttpServletRequest request, HttpServletResponse response, UserPrincipalV7 context) {
		LOGGER.warn("onLogout called! but it not implement");
	}

	@Override
	public void onLoginSuccess(HttpServletRequest request, HttpServletResponse response, UserPrincipalV7 up) {
		LOGGER.warn("onLogSuccess called! but it not implement");
	}

	@Override
	public void onLoginFail(HttpServletRequest request, HttpServletResponse response, Message message) {
		LOGGER.warn("onLogFail called! but it not implement");
	}

}
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
package cn.vlabs.umt.ui.rest;

import org.apache.log4j.Logger;

import cn.vlabs.commons.principal.AppPrincipal;
import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.ServiceWithInputStream;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.user.service.IAccessIPService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;

public class LoginService extends ServiceWithInputStream {

	private static final Logger LOGGER =Logger.getLogger(LoginService.class);
	private IAccessIPService ipService;
	public Object doAction(RestSession session, Object arg)
			throws ServiceException { 
		if(ipService==null){
			init();
		}
		if(ipService.canAccessScope_B(RequestUtil.getRemoteIP(getRequest()))){
			AppPrincipal ap = new AppPrincipal(getRequest().getRemoteAddr());
			LoginUtil.savePrincipal(session, ap);
			return true;
		}else{
			LOGGER.warn("Forbidden Access:"+getRequest().getRemoteAddr());
		}
		return false;
	}
	
	public void init(){
		ipService=(IAccessIPService) ServiceFactory.getBean(getRequest(),IAccessIPService.BEAN_ID);
	}
	
}
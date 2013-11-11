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
package cn.vlabs.umt.ui.rest;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.protocal.Envelope;
import cn.vlabs.rest.server.Predefined;
import cn.vlabs.rest.server.filter.Filter;
import cn.vlabs.rest.server.filter.RequestContext;
import cn.vlabs.rest.server.filter.RestContext;
import cn.vlabs.umt.services.user.service.IAccessIPService;
import cn.vlabs.umt.ui.UMTContext;

public class APIFilter implements Filter {
	private static final Logger LOGGER =Logger.getLogger(APIFilter.class);
	private IAccessIPService ipService;
	
	public void destroy() {
		ipService=null;
	}

	public Envelope doFilter(Method method, Object[] args, RequestContext context,
			RestSession session) {
		if (!ipService.canAccess(context.getRemoteAddr())){
			LOGGER.warn(context.getRemoteAddr());
			return Predefined.AUTHORIZE_FAILED;
		}
		
		return null;
	}

	public void init(RestContext context) {
		BeanFactory factory = UMTContext.getFactory();
		this.ipService=(IAccessIPService)factory.getBean(IAccessIPService.BEAN_ID);
	}
}
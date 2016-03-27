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
package cn.vlabs.umt.ui.rest.uaf;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.cnic.uaf.common.api.IVerifyUserService;
import cn.cnic.uaf.common.trans.UnifiedUser;
import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.ui.UMTContext;

public class UafVerifyUserServiceImpl implements IVerifyUserService {
	   private static final Logger log = Logger.getLogger(UafVerifyUserServiceImpl.class);
	    private LoginService loginService;
		@Override
		public void init(ServletContext context) {
		    BeanFactory factory = UMTContext.getFactory();
	        loginService = (LoginService)factory.getBean("LoginService");
		}

		@Override
		public void destroy() {

		}

		@Override
		public UnifiedUser verify(String userName, String passwd) {
		    log.info("verify user begin!");
			UserPrincipal up = loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(userName, passwd)).getUserPrincipal();
			UnifiedUser uu = UserConvert.convert2UnifiedUser(up);
			log.info("verify user end!");
			return uu;
		}
}
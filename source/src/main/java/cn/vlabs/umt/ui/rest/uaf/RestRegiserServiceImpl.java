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
package cn.vlabs.umt.ui.rest.uaf;

import javax.servlet.ServletContext;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.cnic.uaf.common.api.IRegisterService;
import cn.cnic.uaf.common.trans.UnifiedUser;
import cn.vlabs.duckling.api.umt.rmi.exception.ErrorCode;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.ui.UMTContext;

public class RestRegiserServiceImpl implements IRegisterService {
	private static final Logger LOG=Logger.getLogger(RestRegiserServiceImpl.class);

	private UserService service;
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(ServletContext arg0) {
		BeanFactory factory = UMTContext.getFactory();
		service = (UserService) factory.getBean("UserService");
		//loginService = (LoginService) factory.getBean("LoginService");

	}
	
	public boolean checkEmail(String email){
		boolean flag = true;
		try{
			if (service.getUserByLoginName(email) != null) {
				flag = false;
				throw new ServiceException(ErrorCode.USER_EXIST, "用户已存在");
			}
		}catch(ServiceException e){
			LOG.error(e);
		}
		return flag;
	}
	
	public boolean checkUserName(String userName){
		return true;
	}
	
	public String getAccount(String email){
		return "";
	}
	
	@Override
	public boolean register(UnifiedUser user){

		try {
			service.create(toUser(user),LoginNameInfo.STATUS_ACTIVE);
		} catch (InvalidUserNameException e) {
			return false;
		}
		return true;
	}

	private User toUser(UnifiedUser user) {
		User u = new User();
		u.setPassword(user.getExtInfo().get("password"));
		u.setTrueName(user.getNickName());
		u.setCstnetId(user.getUserName());
		return u;
	}
}
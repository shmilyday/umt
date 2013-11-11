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
package cn.vlabs.umt.validate.validator.impl;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.validate.validator.Validator;

/**
 * 用户名是否存在
 * @author lvly
 * @since 2013-1-21
 */
public class UsernameExistsValidator implements Validator{
	private BeanFactory beanFactory;
	/**
	 * 构造方法
	 * @param beanFactory，主要用来，获取service
	 * */
	public UsernameExistsValidator(BeanFactory beanFactory){
		this.beanFactory= beanFactory;
	}
	private UserService getUserService() {
		return (UserService) beanFactory.getBean("UserService");
	}

	@Override
	/**
	 * @param username 邮箱
	 * @return !isExist
	 * */
	public boolean validate(String username) {
		if (username != null) {
			username = username.toLowerCase();
		}
		UserService us = getUserService();
		boolean isEx=us.isUsed(username);;
		if(!isEx){
			isEx=ICoreMailClient.getInstance().isUserExt(username);
		}
		return !isEx;
	}

}
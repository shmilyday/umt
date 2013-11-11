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
package cn.vlabs.umt.services.user.utils;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.service.IAppAccessService;
import cn.vlabs.umt.services.user.service.IAssociateUserService;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.ui.Attributes;

/**
 * service 提供工厂类
 * @author lvly
 * @since 2013-2-4
 */
public final class ServiceFactory {
	private ServiceFactory(){}
	/**
	 * 获取BeanFactory
	 * */
	public static BeanFactory getBeanFactory(HttpServletRequest request){
		return (BeanFactory)request.getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
	public static Object getBean(HttpServletRequest request,String beanId){
		return getBeanFactory(request).getBean(beanId);
	}
	
	/**
	 * 获取userService
	 * */
	public static UserService getUserService(HttpServletRequest request){
		return (UserService)getBeanFactory(request).getBean(UserService.BEAN_ID);
	}

	/**
	 * 获取登陆验证服务类
	 * */
	public static LoginService getLoginService(HttpServletRequest request){
		return (LoginService) getBeanFactory(request).getBean(LoginService.BEAN_ID);
	}
	/**
	 * 获得Token管理服务类
	 * */
	public static ITokenService getTokenService(HttpServletRequest request){
		return (ITokenService)getBeanFactory(request).getBean(ITokenService.BEAN_ID);
	}
	/**
	 * 获得用户名相关服务类
	 * */
	public static IUserLoginNameService getLoginNameService(HttpServletRequest request){
		return (IUserLoginNameService)getBeanFactory(request).getBean(IUserLoginNameService.BEAN_ID);
	}
	
	/**
	 * 获取appAccessService
	 * */
	public static IAppAccessService getAppAccessService(HttpServletRequest request){
		return (IAppAccessService)getBeanFactory(request).getBean(IAppAccessService.BEAN_ID);
	}
	/**
	 * 获取用户关联
	 * */
	public static IAssociateUserService getAssociateUserService(HttpServletRequest request){
		return (IAssociateUserService)getBeanFactory(request).getBean(IAssociateUserService.BEAN_ID);
	}
	/**
	 * 获取配置文件
	 * */
	public static Config getConfig(HttpServletRequest request){
		return (Config)getBeanFactory(request).getBean(Config.BEAN_ID);
	}
	/**
	 * 获得本项目的绝对地址
	 * */
	public static String getWebUrl(HttpServletRequest request){
		return getConfig(request).getStringProp("umt.this.base.url", "");
	}
	/**
	 * 获取日志服务类
	 * */
	public static IAccountService getLogService(HttpServletRequest request){
		return (IAccountService)getBeanFactory(request).getBean(IAccountService.BEAN_ID);
	}

}
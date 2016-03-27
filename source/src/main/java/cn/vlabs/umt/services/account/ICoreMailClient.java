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
package cn.vlabs.umt.services.account;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.duckling.api.umt.rmi.userv7.SearchField;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.user.bean.CoreMailUserInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.validate.validator.ValidatorFactory;

/**
 * @author lvly
 * @since 2013-10-14
 */
public abstract class ICoreMailClient {
	public static final String USEABLE_BEAN_ID="useableCoreMailClient";
	public static final String UN_USEABLE_BEAN_ID="unuseableCoreMailClient";
	public static boolean useable;
	public static Config config;
	
	public static final List<String> BLACK_LIST=new ArrayList<String>();
	static{
		BLACK_LIST.add("@sina.com");
		BLACK_LIST.add("@163.com");
		BLACK_LIST.add("@qq.com");
		BLACK_LIST.add("@126.com");
		BLACK_LIST.add("@vip.sina.com");
		BLACK_LIST.add("@sina.cn");
		BLACK_LIST.add("@hotmail.com");
		BLACK_LIST.add("@gmail.com");
		BLACK_LIST.add("@sohu.com");
		BLACK_LIST.add("@yahoo.cn");
		BLACK_LIST.add("@139.com");
		BLACK_LIST.add("@wo.com.cn");
		BLACK_LIST.add("@189.cn");
		BLACK_LIST.add("@21cn.com");
		BLACK_LIST.add("@umt.root");
		BLACK_LIST.add("@root.umt");
	}
	public static boolean isBlack(String email){
		if(CommonUtils.isNull(email)){
			return true;
		}
		if(!ValidatorFactory.getEmailRegixValidator().validate(email)){
			return true;
		}
		for(String domain:BLACK_LIST){
			if(email.endsWith(domain)){
				return true;
			}
		}
		return false;
	}
	public static void init(BeanFactory factory){
		if(config==null){
			config=(Config)factory.getBean("Config");
			useable=config.getBooleanProp("umt.coremail.enable", false);
		}
	}
	
	public static ICoreMailClient getInstance(){
		BeanFactory factory=UMTContext.getFactory();
		init(factory);
		if(useable){
			return (ICoreMailClient)factory.getBean(USEABLE_BEAN_ID);
		}else{
			return (ICoreMailClient)factory.getBean(UN_USEABLE_BEAN_ID);
		}
		
	}
	/**
	 * 不理会用户传过来的@domain信息直接用配置文件配置的信息
	 * @param userName 用户输入的账户信息，比如username@shi.com 或者 username
	 * */
	public String formatEmail(String userName){
		if(CommonUtils.isNull(userName)){
			return "";
		}
		if(userName.contains("@")){
			return userName;
		}else{
			String domain="@"+config.getStringProp("umt.coremail.api.email.domain", "escience.cn");
			return getUserName(userName)+domain;
		}
	}
	public String getUserName(String email){
		if(email.contains("@")){
			return email.substring(0,email.indexOf("@"));
		}
		return email;
	}
	/**
	 * 邮件系统验证用户是否存在
	 * @param usreName 用户邮箱
	 * */
	public abstract boolean isUserExt(String userName) ;
	/**
	 * 更改用户的对应密码，注意，请先用authenticate验证，就密码是否相同
	 * 
	 * @param userName
	 *            用户账户
	 * @param newPassword
	 *            ，明文密码
	 * @return 是否更改成功
	 * */
	public abstract boolean changePassword(String userName, String newPassword) ;


	/**
	 * 验证邮箱用户名密码，是否正确
	 * 
	 * @param userName
	 *            用户名
	 * @param passWord
	 *            用户密码
	 * */
	public abstract CoreMailAuthenticateResult authenticate(String userName, String passWord);

	

	/**
	 * 获得用户信息,请调用这个方法前，用authenticate(userName,passWord)验证改用户是否有效
	 * 
	 * @param userName
	 * @return 用户信息
	 * */
	public abstract CoreMailUserInfo getCoreMailUserInfo(String userName);
	/**
	 * 获取次关键字有多少结果
	 * @param keyword
	 * */
	public abstract int getSize(String domain,String keyword,SearchField field);
	
	/**
	 * 根据关键字搜索
	 * @param keyword 关键字
	 * @param field 搜索字段	
	 * @param offset 偏移
	 * @param size	-1为无限制
	 * @return
	 */
	public abstract List<User> searchByKeyword(String keyword,String domain,SearchField field,int offset,int size);
	
	
	
	/**
	 * 检查CoreMail域名是否已存在
	 * */
	public abstract boolean domainExist(String domainName);
	
	/**
	 * 创建用户
	 * @param username
	 * @param password
	 */
	public abstract boolean createUser(String username,String trueName, String password);
	
}
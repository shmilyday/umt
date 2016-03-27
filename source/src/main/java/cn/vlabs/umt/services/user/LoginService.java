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
package cn.vlabs.umt.services.user;

import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.OauthCredential;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;



/**
 * 提供登录服务
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 7, 2009 11:35:42 AM
 */
public interface LoginService {
	static final String BEAN_ID="LoginService";
	/**
	 * 验证用户输入的密码是否为umt的密码
	 * */
	boolean umtPasswrdRight(UsernamePasswordCredential cred);
	
	/**
	 * 验证用户输入的密码是否为coreMail
	 * */
	boolean coreMailPasswordRight(UsernamePasswordCredential cred);
	
	/**
	 * 验证用户身份的有效性
	 * @param cred 身份验证的方式
	 * @return 密码是否正确
	 */
	boolean passwordRight(Credential cred);
	
	/**
	 * Oauth独立应用密码是否有效
	 * @param cred 身份验证方式,
	 * @return 密码是否正确
	 * 
	 * */
	boolean oauthPasswordRight(OauthCredential cred);
	
	
	/**
	 * 验证用户名是否正确，且返回用神马密码登陆的信息,
	 * @author lvly
	 * @since 2013-2-4 
	 * @param cred 
	 * @param LoginInfo 返回值永远不会是空
	 * */
	LoginInfo loginAndReturnPasswordType(Credential cred);
	
}
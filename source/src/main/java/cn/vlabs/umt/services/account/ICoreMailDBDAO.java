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

import cn.vlabs.umt.common.datasource.CoreMailDBException;
import cn.vlabs.umt.services.user.bean.CoreMailUserInfo;

public interface ICoreMailDBDAO {
	/**
	 * 去coremail从库验证账号名密码
	 * @param userName 用户名
	 * @param password 密码
	 * @return 命中的用户信息,如果密码错误,则返回空
	 * @throws CoreMailDBException 访问数据库过程中如果抛错,抛出
	 * */
	CoreMailUserInfo authticate(String userName,String password) throws CoreMailDBException;

	/**
	 * 去coremail从库验证域名是否存在
	 * @param domain 域名，已trim
	 * @return 是否存在
	 * @throws CoreMailDBException  抛出此异常代表数据库不可用，请使用coremailAPI
	 * */
	boolean isDomainExits(String domain)throws CoreMailDBException;
	
}

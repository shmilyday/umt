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
package cn.vlabs.umt.services.auth;

import java.util.List;


public interface IAuthService {
	/**
	 * 寻找SiteName对应的Oauth认证模块
	 * @param siteName 认证站点的名称
	 * @return	OAuth认证模块
	 */
	ThirdPartyAuth find(String siteName);
	/**
	 * 判断对应的siteName是否为已注册的认证站点
	 * @param type
	 * @return
	 */
	boolean existAuth(String siteName);
	/**
	 * 查询所有的OAuth认证信息
	 * @return
	 */
	List<ThirdPartyAuth> getAll();
	/**
	 * 创建第三方认证服务
	 * @param auth
	 */
	void create(ThirdPartyAuth auth);
	/**
	 * 更新第三方认证服务配置
	 * @param auth
	 */
	void update(ThirdPartyAuth auth);
	/**
	 * 删除第三方认证服务配置
	 * @param code
	 */
	void remove(String code);
	/**
	 * 查找需要显示在登录界面的第三方认证
	 * @return
	 */
	List<ThirdPartyAuth> findShowInLogin();
	
}

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
package cn.vlabs.umt.services.user.dao;

import java.util.List;

import cn.vlabs.umt.services.user.bean.AppSecret;

public interface IAppSecretDAO {
	List<AppSecret> findAppSecretByUid(int uid);

	AppSecret findAppSecretByUidAndAppId(String appId, int userId);

	//更新密码
	void updateSecret(int id,String loginName, String shaPassword, String updateSecret);

	void insert(AppSecret appSecret);

	void deleteMySecret(int secretId, int userId);

	List<AppSecret> findAppSecretByTypeAndAppId(String secretTypeLdap, String string);

	void deleteMyLdapSecret(int uid);

	List<AppSecret> findMyAppMember(String appId);

	void openMember(int parseInt);

	AppSecret findAppSecretById(int secretId);

	void deleteMember(int sId);
	
	boolean isSecretUsed(String transformd,int uid);
	
	

}

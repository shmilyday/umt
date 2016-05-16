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
package cn.vlabs.umt.services.user.service;

import java.util.List;

import cn.vlabs.umt.services.user.bean.AppSecret;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.bean.User;

public interface IAppSecretService {
	String BEAN_ID = "appSecretService";

	List<AppSecret> findAppSecretByUid(int uid);

	AppSecret findAppSecretByUidAndAppId(String appId, int userId);
	
	/**
	 * 存在则更新密码,不存在则创建
	 * */
	void deleteMySecret(int secretId, int userId);

	boolean updateOrInsertIfNotExist(AppSecret appSecret,LdapBean lb);
	/**
	 * 更新LDAP应用的用户密码
	 * @param ldapApp		Ldap应用信息
	 * @param user			用户信息
	 * @param newPassword	新密码
	 * @return
	 */
	boolean updateLdapPasswordOrInsertIfNotExist(LdapBean ldapApp, User user, String newPassword);

	void removeAllLdapSecret(int uid, String ldapUid);
	
	List<AppSecret> findMyAppMember(String appId);

	void openMember(LdapBean bean,AppSecret as,String loginName);

	AppSecret findAppSecretById(int sId);

	void deleteMember(String rdn, String loginName, int sId);

	boolean isAppSecretUsed(String secret,int uid);

	void sendToMember(LdapBean bean, User userByUid, String string);

	boolean update(AppSecret appSecret, LdapBean lb);

	boolean updateWifiPasswordOrInsertIfNotExist(LdapBean ldapApp, User user, String newPassword);

	boolean updateWifiPassword(LdapBean ldapApp, User user, String newPassword);
	
}

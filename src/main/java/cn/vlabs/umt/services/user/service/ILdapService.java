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
import java.util.Locale;

import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.bean.User;

public interface ILdapService {
	String BEAN_ID="LdapService";
	
	void addLdapApp(LdapBean bean);

	boolean isRdnUsed(String rdn);
	
	List<LdapBean> searchMyLdapApp(int uid);

	LdapBean getLdapBeanById(int parseInt);

	void updateByAppAdmin(LdapBean bean);

	void updateByAdmin(LdapBean bean);
	
	List<LdapBean> findAllApp();

	void removeLdapApp(int beanId, int uid);
	void removeLdapApp(int beanId);
	
	void removeSoAccount(int beanId,String ldapUid);
	
	void sendMailToSuperAdmin(Locale locale,LdapBean bean,User u) throws MailException;

	List<LdapBean> findEnableAndAccepted(String viewType);
	/**
	 * 查询用户管理的WIFI应用
	 * @param userId
	 * @return
	 */
	List<LdapBean> searchMyWifiApps(int userId);

	LdapBean findAvailableWifi(String cstnetId);
	
}

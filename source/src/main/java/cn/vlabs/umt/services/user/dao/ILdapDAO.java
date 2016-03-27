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

import cn.vlabs.umt.services.user.bean.LdapBean;

public interface ILdapDAO {
	void addLdapApp(LdapBean bean);

	boolean isRdnUsed(String rdn);

	List<LdapBean> searchMyLdapApp(int uid);

	LdapBean getLdapBeanById(int id);

	void updateLdapApp(LdapBean bean);

	List<LdapBean> findEnableAndAccepted(String viewType);

	List<LdapBean> findAllApp();

	void removeLapApp(int beanId);

	void updateLdapAppPasswd(LdapBean lb);

	List<LdapBean> searchMyWifiApp(int userId);

	List<LdapBean> findAvailableWifi(String scope);
}

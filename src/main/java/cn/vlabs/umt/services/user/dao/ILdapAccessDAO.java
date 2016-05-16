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
import cn.vlabs.umt.services.user.bean.LdapBean;


public interface ILdapAccessDAO {


	void removeApp(String rdn);

	void addAccount(LdapBean bean, AppSecret as, String loginName);


	boolean updateSecret(String rdn,String orginLoginName, AppSecret secret);


	void removeSoAccount(String rdn, String loginName);


	List<String> searchDn(String ldapUid);
	
	void removeByDn(String dn);


	void addApp(LdapBean bean);

}

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

import cn.vlabs.umt.services.user.bean.OauthClientBean;

public interface IOauthClientDAO {
	public static final String BEAN_ID = "oauthClientDAO";
	int save(OauthClientBean bean);
	OauthClientBean findByClientId(String id);
	List<OauthClientBean> findByStatus(String id);
	OauthClientBean findById(int id);
	void delete(OauthClientBean bean);
	void delete(int id);
	void update(OauthClientBean bean);
	List<OauthClientBean> getAll();
	List<OauthClientBean> findByUid(int userId, String type);
	/**
	 * 用户更改时更新，避免管理员可操作字段被覆盖
	 * @param bean
	 */
	void updateDevelop(OauthClientBean bean);
	List<OauthClientBean> searchClientByKey(String key,int offset,int size); 
	void updateLogo(OauthClientBean bean, boolean is100Updated,
			boolean is64Updated, boolean is32Updated, boolean is16Updated);
	void removeLogo(OauthClientBean bean, boolean is100Updated,
			boolean is64Updated, boolean is32Updated, boolean is16Updated);
	List<OauthClientBean> findEnableAppAndAccepted(String type);
	List<String> getAllCallBack();
}
/*
 * Copyright (c) 2008-2013 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
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
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.User;

public interface IOauthClientService {
	public static final String BEAN_ID = "oauthClientService";
	
	int save(OauthClientBean bean, boolean needCache);
	OauthClientBean findByClientId(String clientId);
	/**
	 *通过client id获取已经通过审核的OauthClientBean
	 * @param clientId
	 * @return
	 */
	OauthClientBean findAcceptByClientId(String clientId);
	List<OauthClientBean> findByStatus(String id);
	OauthClientBean findById(int id);
	void delete(OauthClientBean bean);
	void delete(int id);
	void deleteFromCache(OauthClientBean bean);
	void update(OauthClientBean bean);
	List<OauthClientBean> getAll();
	List<OauthClientBean> findByUid(int userId);
	
	/**
	 * 新增时，给管理员发邮件
	 * @param locale
	 * @param bean
	 * @param u
	 * @throws MailException 
	 */
	void sendAddMailtoAmin(Locale locale,OauthClientBean bean,User u) throws MailException;
	/**
	 * 更新时，给管理员发邮件
	 * @param bean
	 * @throws MailException 
	 */
	void sendUpdateMailtoAmin(Locale locale,OauthClientBean beforeBean,OauthClientBean afterBean,User u) throws MailException;
	/**
	 * 用户更改时更新，避免管理员可操作字段被覆盖
	 * @param beanAfter
	 */
	void updateDevelop(OauthClientBean beanAfter);
	
	/**
	 * 管理员更改状态的时候，发送邮件给开发者
	 * */
	void sendAdminToDevelop(Locale locale,OauthClientBean bean,User user) ;
}
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Locale;

import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.services.user.bean.LogoUploadResult;
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
	 * @
	 * */
	void sendAdminToDevelop(Locale locale,OauthClientBean bean,User user) ;
	List<OauthClientBean> searchClientByKey(String key, int offset, int size);
	/**
	 * 
	 * 根据尺寸,上传图标
	 * @param tmpFile,备份
	 * @param target "100p","64p","32p","16p"
	 * @return msg 如果成功返回空
	 * @throws FileNotFoundException 
	 * @throws IOException 
	 * */
	LogoUploadResult uploadLogo(OauthClientBean bean,File tmpFile,String fileName, String target) throws FileNotFoundException, IOException;
	void uploadLogoDefault(OauthClientBean bean,File tmpFile) throws FileNotFoundException;
	void removeLogo(OauthClientBean bean, boolean is100Updated,boolean is64Updated, boolean is32Updated, boolean is16Updated) ;
	void updateDefaultLogoChange(OauthClientBean bean,File file) throws FileNotFoundException;
	List<OauthClientBean> findByUid(int userId, String type);
	List<OauthClientBean> findEnableAppAndAccepted(String type);
	
	List<String> getAllCallBack();
}
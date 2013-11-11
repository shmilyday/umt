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
package cn.vlabs.umt.services.user.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;

import net.duckling.falcon.api.cache.ICacheService;
import cn.vlabs.umt.common.mail.EmailTemplate;
import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.common.mail.MessageSender;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.dao.IOauthClientDAO;
import cn.vlabs.umt.services.user.service.IOauthClientService;

public class OauthClientService implements IOauthClientService {
	private static final Logger LOGGER=Logger.getLogger(OauthClientService.class); 
	private ICacheService cacheService;
	private IOauthClientDAO oauthClientDAO;
	private MessageSender emailSender;
	private RoleService roleService; 
	
	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}

	public void setEmailSender(MessageSender email) {
		this.emailSender = email;
	}

	public void setCacheService(ICacheService cacheService) {
		this.cacheService = cacheService;
	}

	public void setOauthClientDAO(IOauthClientDAO oauthClientDAO) {
		this.oauthClientDAO = oauthClientDAO;
	}

	@Override
	public int save(OauthClientBean bean,boolean needCache) {
		int i=oauthClientDAO.save(bean);
		if(i>0&&needCache){
			addToCache(bean);
		}
		return bean.getId();
	}

	private void addToCache(OauthClientBean bean){
		cacheService.set(getClientKey(bean), bean);
	}
	private void removeFromCache(OauthClientBean bean){
		cacheService.remove(getClientKey(bean.getClientId()));
	}
	
	private String getClientKey(OauthClientBean bean){
		return getClientKey(bean.getClientId());
	}
	private String getClientKey(String clientId) {
		return clientId+"-oauth_client_id";
	}

	@Override
	public OauthClientBean findByClientId(String clientId) {
		Object o = cacheService.get(getClientKey(clientId));
		if(o==null){
			OauthClientBean bean = oauthClientDAO.findByClientId(clientId);
			if(bean!=null){
				addToCache(bean);
			}
			return bean;
		}else{
			return (OauthClientBean)o;
		}
	}

	@Override
	public List<OauthClientBean> findByStatus(String id) {
		return oauthClientDAO.findByStatus(id);
	}
	@Override
	public List<OauthClientBean> findByUid(int userId) {
		return oauthClientDAO.findByUid(userId);
	}

	@Override
	public OauthClientBean findById(int id) {
		return oauthClientDAO.findById(id);
	}

	@Override
	public void delete(OauthClientBean bean) {
		removeFromCache(bean);
		oauthClientDAO.delete(bean);
	}

	@Override
	public void delete(int id) {
		OauthClientBean bean = oauthClientDAO.findById(id);
		if(bean!=null){
			delete(bean);
		}
	}

	@Override
	public void update(OauthClientBean bean) {
		removeFromCache(bean);
		oauthClientDAO.update(bean);
		addToCache(bean);
	}

	@Override
	public List<OauthClientBean> getAll() {
		return oauthClientDAO.getAll();
	}

	@Override
	public void deleteFromCache(OauthClientBean bean) {
		removeFromCache(bean);
	}
	@Override
	public void sendAddMailtoAmin(Locale locale,OauthClientBean bean,User u) throws MailException {

		Collection<User> users=roleService.getRoleMembers("admin");
		Properties pro=new Properties();
		pro.setProperty("trueName", u.getTrueName());
		pro.setProperty("email", u.getCstnetId());
		pro.setProperty("appName", bean.getClientName());
		pro.setProperty("webSite", bean.getClientWebsite());
		pro.setProperty("returnUri", bean.getRedirectURI());
		pro.setProperty("description",bean.getDescription());
		pro.setProperty("applicant", bean.getApplicant());
		pro.setProperty("company", bean.getCompany());
		pro.setProperty("contactInfo", bean.getContactInfo());
		pro.setProperty("appType", bean.getAppTypeDesc());
		
		emailSender.send(locale, getEmailList(users),EmailTemplate.NOTICE_ADMIN_OAUTH_ADD, pro);
		
	}
	private String[] getEmailList(Collection<User> col){
		String[] users=new String[col.size()];
		int index=0;
		for(User user:col){
			users[index++]=user.getCstnetId();
		}
		return users;
	}
	
	@Override
	public void sendUpdateMailtoAmin(Locale locale, OauthClientBean beforeBean, OauthClientBean afterBean, User u) throws MailException {
		Collection<User> users=roleService.getRoleMembers("admin");
		Properties pro=new Properties();
		pro.setProperty("trueName", u.getTrueName());
		pro.setProperty("email", u.getCstnetId());
		pro.setProperty("orgAppName", beforeBean.getClientName());
		pro.setProperty("orgwebSite", beforeBean.getClientWebsite());
		pro.setProperty("orgreturnUri", beforeBean.getRedirectURI());
		pro.setProperty("orgdescription",beforeBean.getDescription());
		pro.setProperty("orgapplicant", beforeBean.getApplicant());
		pro.setProperty("orgcompany", beforeBean.getCompany());
		pro.setProperty("orgcontactInfo",beforeBean.getContactInfo());
		pro.setProperty("orgappType", beforeBean.getAppTypeDesc());
		
		pro.setProperty("afterAppName", afterBean.getClientName());
		pro.setProperty("afterwebSite", afterBean.getClientWebsite());
		pro.setProperty("afterreturnUri", afterBean.getRedirectURI());
		pro.setProperty("afterdescription",afterBean.getDescription());
		pro.setProperty("afterapplicant", afterBean.getApplicant());
		pro.setProperty("aftercompany", afterBean.getCompany());
		pro.setProperty("aftercontactInfo",afterBean.getContactInfo());
		pro.setProperty("afterappType", afterBean.getAppTypeDesc());
		
		emailSender.send(locale, getEmailList(users),EmailTemplate.NOTICE_ADMIN_OAUTH_UPDATE, pro);
	}
	
	@Override
	public void sendAdminToDevelop(Locale locale, OauthClientBean bean,User user){
		Properties pro=new Properties();
		pro.setProperty("appName", CommonUtils.killNull(bean.getClientName()));
		pro.setProperty("webSite", CommonUtils.killNull(bean.getClientWebsite()));
		pro.setProperty("returnUri", CommonUtils.killNull(bean.getRedirectURI()));
		pro.setProperty("description",CommonUtils.killNull(bean.getDescription()));
		pro.setProperty("applicant", CommonUtils.killNull(bean.getApplicant()));
		pro.setProperty("company", CommonUtils.killNull(bean.getCompany()));
		pro.setProperty("status",CommonUtils.killNull(bean.getStatusDisplay()));
		pro.setProperty("contact", CommonUtils.killNull(bean.getContactInfo()));
		pro.setProperty("appType", CommonUtils.killNull(bean.getAppTypeDesc()));
		try {
			if(user!=null){
				emailSender.send(locale, user.getCstnetId(), EmailTemplate.NOTICE_USER_OAUTH_UPDATE, pro);
			}
		} catch (MailException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	@Override
	public void updateDevelop(OauthClientBean bean) {
		removeFromCache(bean);
		oauthClientDAO.updateDevelop(bean);
		addToCache(findById(bean.getId()));
	}

	@Override
	public OauthClientBean findAcceptByClientId(String clientId) {
		OauthClientBean result = findByClientId(clientId);
		if(result!=null&&result.isAccept()){
			return result;
		}
		return null;
	}

}
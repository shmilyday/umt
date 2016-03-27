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
package cn.vlabs.umt.services.user.service.impl;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;

import cn.vlabs.umt.common.EmailUtil;
import cn.vlabs.umt.common.mail.EmailTemplate;
import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.common.mail.MessageSender;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RandomUtil;
import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.user.bean.AppSecret;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.dao.IAppSecretDAO;
import cn.vlabs.umt.services.user.dao.ILdapAccessDAO;
import cn.vlabs.umt.services.user.dao.ILdapDAO;
import cn.vlabs.umt.services.user.dao.IUserDAO;
import cn.vlabs.umt.services.user.dao.IUserLoginNameDAO;
import cn.vlabs.umt.services.user.service.ILdapService;
import cn.vlabs.umt.services.user.service.ITransform;

public class LdapServiceImpl implements ILdapService {
	private static final Logger LOG=Logger.getLogger(LdapServiceImpl.class);
	private ILdapDAO ldapDAO;
	private ILdapAccessDAO ldapAccessDAO;
	private IAppSecretDAO appSecretDAO;
	private IUserLoginNameDAO userLoginNameDAO;
	private IUserDAO userDAO;
	private MessageSender emailSender;
	private RoleService roleService; 
	private ITransform transform;
	
	@Override
	public void sendMailToSuperAdmin(Locale locale, LdapBean bean, User u) throws MailException {
		Collection<User> users=roleService.getRoleMembers("admin");
		Properties pro=new Properties();
		pro.setProperty("trueName", CommonUtils.killNull(u.getTrueName()));
		pro.setProperty("email", CommonUtils.killNull(u.getCstnetId()));
		pro.setProperty("appName", CommonUtils.killNull(bean.getClientName()));
		pro.setProperty("rdn", CommonUtils.killNull(bean.getRdn()));
		pro.setProperty("priv", CommonUtils.killNull(bean.getPrivDisplay()));
		pro.setProperty("description",CommonUtils.killNull(bean.getDescription()));
		pro.setProperty("applicant", CommonUtils.killNull(bean.getApplicant()));
		pro.setProperty("company", CommonUtils.killNull(bean.getCompany()));
		pro.setProperty("contactInfo", CommonUtils.killNull(bean.getContactInfo()));
		if(bean.isWifiApp()){
			emailSender.send(locale, getEmailList(users),EmailTemplate.NOTICE_ADMIN_WIFI_ADD, pro);
		}else{
			emailSender.send(locale, getEmailList(users),EmailTemplate.NOTICE_ADMIN_LDAP_ADD, pro);
		}
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
	public void addLdapApp(LdapBean bean) {
		if (CommonUtils.isNull(bean.getAppStatus())) {
			bean.setAppStatus(LdapBean.APP_STATUS_APPLY);
		}
		bean.setLdapPassword(RandomUtil.random(20));
		ldapDAO.addLdapApp(bean);
	}

	@Override
	public boolean isRdnUsed(String rdn) {
		return ldapDAO.isRdnUsed(rdn);
	}

	@Override
	public List<LdapBean> searchMyLdapApp(int uid) {
		return ldapDAO.searchMyLdapApp(uid);
	}

	@Override
	public LdapBean getLdapBeanById(int id) {
		return ldapDAO.getLdapBeanById(id);
	}
	private String setIfPasswordNotExits(LdapBean lb){
		if(CommonUtils.isNull(lb.getLdapPassword())){
			lb.setLdapPassword(RandomUtil.random(20));
			ldapDAO.updateLdapAppPasswd(lb);
		}
		return transform.transform(lb.getLdapPassword());
	}
	
	
	public void setTransform(ITransform transform) {
		this.transform = transform;
	}
	private void sendToAppAdmin(LdapBean bean,String sendTo){
		Properties pro=new Properties();
		pro.setProperty("trueName",CommonUtils.killNull(bean.getUserName()));
		pro.setProperty("email", CommonUtils.killNull(bean.getUserCstnetId()));
		pro.setProperty("appName", CommonUtils.killNull(bean.getClientName()));
		pro.setProperty("rdn", CommonUtils.killNull(bean.getRdn()));
		pro.setProperty("status", CommonUtils.killNull(bean.getAppStatusDisplay()));
		pro.setProperty("priv", CommonUtils.killNull(bean.getPrivDisplay()));
		pro.setProperty("description",CommonUtils.killNull(bean.getDescription()));
		pro.setProperty("applicant", CommonUtils.killNull(bean.getApplicant()));
		pro.setProperty("company", CommonUtils.killNull(bean.getCompany()));
		pro.setProperty("contactInfo", CommonUtils.killNull(bean.getContactInfo()));
		try {
			emailSender.send(new Locale("zh_CN"), new String[]{sendTo},EmailTemplate.NOTICE_USER_LDAP_UPDATE, pro);
		} catch (MailException e) {
			LOG.error(e.getMessage(),e);
		}
	}
	@Override
	public void updateByAdmin(LdapBean bean) {
		LdapBean orgBean = getLdapBeanById(bean.getId());
		bean.setLdapPassword(setIfPasswordNotExits(orgBean));
		orgBean.setLdapPassword(bean.getLdapPassword());
		/**
		 * 更新LDAP应用权限需要更新 该应用下面的人
		 * 首先根据rdn清空ldap节点 然后重新根据rdn建立节点，此时该ldap应用中没有任何用户
		 * 下一步需要根据权限将人加入到ldap
		 * 如果更新后应用权限为公开 则将该应用申请人全部加入到ldap
		 * 如果更新后应用权限为需审核 则过滤当前申请用户只将已审核的加入到ldap
		 * 如果更新后应用权限为关闭  不做处理 相当于清空了当前ldap的应用
		 */
		//清空并重建应用ldap节点
		ldapAccessDAO.removeApp(orgBean.getRdn());
		ldapAccessDAO.addApp(orgBean);
		
		if(LdapBean.APP_STATUS_ACCEPT.equals(bean.getAppStatus())&&!LdapBean.PRIV_CLOSED.equals(bean.getPriv())){
			bean.setRdn(orgBean.getRdn());
			//根据当前应用全新将人重新放入ldap应用
			
			rebuildAppSecret(bean);
		}
		ldapDAO.updateLdapApp(bean);
		if(!orgBean.getAppStatus().equals(bean.getAppStatus())){
			String sendTo=orgBean.getUserCstnetId();
			BeanUtils.copyProperties(bean, orgBean);
			sendToAppAdmin(orgBean,sendTo);
		}
	}
	@Override
	public void updateByAppAdmin(LdapBean bean) {
		LdapBean orgBean = getLdapBeanById(bean.getId());
		orgBean.setLdapPassword(setIfPasswordNotExits(orgBean));
		if(orgBean.getUid()!=bean.getUid()){
			return;
		}
		ldapAccessDAO.removeApp(orgBean.getRdn());
		ldapAccessDAO.addApp(orgBean);
		if (CommonUtils.isNull(bean.getAppStatus())) {
			bean.setAppStatus(orgBean.getAppStatus());
		}
		ldapDAO.updateLdapApp(bean);
		//不允许,直接删掉所有Ldap数据
		if(LdapBean.APP_STATUS_ACCEPT.equals(orgBean.getAppStatus())&&!LdapBean.PRIV_CLOSED.equals(bean.getPriv())){
			bean.setRdn(orgBean.getRdn());
			rebuildAppSecret(bean);
		}
	}

	private void removeByPriv(List<AppSecret> secret, String priv){
		if(CommonUtils.isNull(secret)){
			return;
		}
		for(Iterator<AppSecret> it=secret.iterator();it.hasNext();){
			AppSecret as=it.next();
			switch(priv){
				case LdapBean.PRIV_CLOSED:{
					it.remove();
					break;
				}
				case LdapBean.PRIV_NEED_APPLY:{
					if(AppSecret.USER_STATUS_APPLY.equals(as.getUserStatus())){
						it.remove();
					}
					break;
				}
				case LdapBean.PRIV_OPEN:{
					break;
				}
			}
		}
	}
	
private void rebuildAppSecret(LdapBean bean) {
		if(AppSecret.SECRET_TYPE_WIFI.equals(bean.getType())){
			rebuildWifiAppSecret(bean);
			return;
		}
		rebuildLdapAppSecret(bean);
	}

	private void rebuildLdapAppSecret(LdapBean bean) {
		
		List<AppSecret> secrets = appSecretDAO.findAppSecretByTypeAndAppId(AppSecret.SECRET_TYPE_LDAP, bean.getId() + "");
		removeByPriv(secrets,bean.getPriv());
		if(CommonUtils.isNull(secrets)){
			return;
		}
		
		List<String> uids = CommonUtils.extractSthField(secrets, "uid");
		
		List<LoginNameInfo> loginNameInfo = userLoginNameDAO.getLoginNameInfos(
				uids, LoginNameInfo.LOGINNAME_TYPE_LDAP);
		Map<String, LoginNameInfo> loginNameMap = CommonUtils
				.extractSthFieldToMap(loginNameInfo, "uid");
		List<User> users = userDAO.getUsersByIds(uids);
		Map<String, User> userInfos = CommonUtils.extractSthFieldToMap(users,
				"id");
		for (AppSecret ac : secrets) {
			LoginNameInfo lni = loginNameMap.get(ac.getUid() + "");
			User u = userInfos.get(ac.getUid() + "");
			if (ac == null || u == null || lni == null) {
				continue;
			}
			ldapAccessDAO.addAccount(bean, ac, lni.getLoginName());
		}
	}
	
	private void rebuildWifiAppSecret(LdapBean bean) {
		
		List<AppSecret> secrets = appSecretDAO.findAppSecretByTypeAndAppId(AppSecret.SECRET_TYPE_WIFI, bean.getId() + "");
		removeByPriv(secrets,bean.getPriv());
		if(CommonUtils.isNull(secrets)){
			return;
		}
		for (AppSecret ac : secrets) {
			ldapAccessDAO.addAccount(bean, ac, ac.getUserLdapName());
		}
	}
	
	

	public void setLdapDAO(ILdapDAO ldapDAO) {
		this.ldapDAO = ldapDAO;
	}

	public void setLdapAccessDAO(ILdapAccessDAO ldapAccessDAO) {
		this.ldapAccessDAO = ldapAccessDAO;
	}

	public void setAppSecretDAO(IAppSecretDAO appSecretDAO) {
		this.appSecretDAO = appSecretDAO;
	}

	public void setUserLoginNameDAO(IUserLoginNameDAO userLoginNameDAO) {
		this.userLoginNameDAO = userLoginNameDAO;
	}

	public void setUserDAO(IUserDAO userDAO) {
		this.userDAO = userDAO;
	}

	@Override
	public List<LdapBean> findEnableAndAccepted(String viewType) {
		return ldapDAO.findEnableAndAccepted(viewType);
	}
	
	@Override
	public List<LdapBean> findAllApp() {
		return ldapDAO.findAllApp();
	}

	@Override
	public void removeLdapApp(int beanId) {
		LdapBean orgBean = getLdapBeanById(beanId);
		ldapDAO.removeLapApp(beanId);
		ldapAccessDAO.removeApp(orgBean.getRdn());
	}

	@Override
	public void removeLdapApp(int beanId, int uid) {
		LdapBean orgBean = getLdapBeanById(beanId);
		if (orgBean.getUid() == uid) {
			ldapDAO.removeLapApp(beanId);
			ldapAccessDAO.removeApp(orgBean.getRdn());
		}

	}

	@Override
	public void removeSoAccount(int beanId, String loginName) {
		LdapBean lb = getLdapBeanById(beanId);
		ldapAccessDAO.removeSoAccount(lb.getRdn(), loginName);
	}

	public void setEmailSender(MessageSender emailSender) {
		this.emailSender = emailSender;
	}

	public void setRoleService(RoleService roleService) {
		this.roleService = roleService;
	}
	@Override
	public List<LdapBean> searchMyWifiApps(int userId) {
		return this.ldapDAO.searchMyWifiApp(userId);
	}
	
	@Override
	public LdapBean findAvailableWifi(String cstnetId) {
		if (cstnetId==null){
			return null;
		}
		String scope =EmailUtil.extractDomain(cstnetId);
		List<LdapBean> apps = ldapDAO.findAvailableWifi(scope);
		if (apps.size()>0){
			return apps.get(0);
		}else{
			return null;
		}
	}

}

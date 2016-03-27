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

import java.util.List;
import java.util.Locale;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.ldap.NameAlreadyBoundException;

import cn.vlabs.umt.common.EmailUtil;
import cn.vlabs.umt.common.mail.EmailTemplate;
import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.common.mail.MessageSender;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.user.bean.AppSecret;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.dao.IAppSecretDAO;
import cn.vlabs.umt.services.user.dao.ILdapAccessDAO;
import cn.vlabs.umt.services.user.service.IAppSecretService;
import cn.vlabs.umt.services.user.service.ITransform;

public class AppSecretServiceImpl implements IAppSecretService {
	private Logger LOG = Logger.getLogger(AppSecretServiceImpl.class);
	private IAppSecretDAO secretDAO;
	private ITransform transform;
	private ILdapAccessDAO ldapAccessDAO;
	private MessageSender emailSender;
	private Config config;

	public void setConfig(Config config) {
		this.config = config;
	}

	public void setEmailSender(MessageSender emailSender) {
		this.emailSender = emailSender;
	}

	public void sendUserApplyMailToAppAdmin(LdapBean bean, AppSecret as) {
		if (bean == null) {
			return;
		}
		Properties pro = new Properties();
		pro.setProperty("userName", CommonUtils.killNull(as.getUserName()));
		pro.setProperty("email", CommonUtils.killNull(as.getUserCstnetId()));
		pro.setProperty("appName", CommonUtils.killNull(bean.getClientName()));
		pro.setProperty("id", CommonUtils.killNull(bean.getId()));
		pro.setProperty("baseUrl", CommonUtils.killNull(config.getStringProp(
				"umt.this.base.url", "")));
		try {
			emailSender.send(new Locale("zh_CN"), bean.getUserCstnetId(),
					EmailTemplate.NOTICE_LDAP_ADD_APPLY_MEMBER, pro);
		} catch (MailException e) {
			LOG.error(e.getMessage(), e);
		}

	}
	public void sendUserAddMailToAppAdmin(LdapBean bean, AppSecret as) {
		if (bean == null) {
			return;
		}
		Properties pro = new Properties();
		pro.setProperty("userName", CommonUtils.killNull(as.getUserName()));
		pro.setProperty("email", CommonUtils.killNull(as.getUserCstnetId()));
		pro.setProperty("appName", CommonUtils.killNull(bean.getClientName()));
		pro.setProperty("id", CommonUtils.killNull(bean.getId()));
		pro.setProperty("baseUrl", CommonUtils.killNull(config.getStringProp(
				"umt.this.base.url", "")));
		try {
			emailSender.send(new Locale("zh_CN"), bean.getUserCstnetId(),
					EmailTemplate.NOTICE_LDAP_HAS_ADD_MEMBER, pro);
		} catch (MailException e) {
			LOG.error(e.getMessage(), e);
		}
		
	}

	@Override
	public List<AppSecret> findAppSecretByUid(int uid) {
		return secretDAO.findAppSecretByUid(uid);
	}

	@Override
	public AppSecret findAppSecretByUidAndAppId(String appId, int userId) {
		return secretDAO.findAppSecretByUidAndAppId(appId, userId);
	}

	@Override
	public boolean updateOrInsertIfNotExist(AppSecret appSecret, LdapBean lb) {
		AppSecret orgSecret = secretDAO.findAppSecretByUidAndAppId(
				appSecret.getAppId(), appSecret.getUid());
		// 已存在，直接更新
		if (orgSecret != null) {
			return update(appSecret,lb);
		} else {
			return insert(appSecret,lb);
		}
	}
	
	@Override
	public boolean update(AppSecret appSecret, LdapBean lb){
		String loginName = appSecret.getUserLdapName();
		AppSecret orgSecret = secretDAO.findAppSecretByUidAndAppId(appSecret.getAppId(), appSecret.getUid());
		if(orgSecret==null||orgSecret.getId()<=0){
			return false;
		}
		
		String shaHash = appSecret.getHashedSecret(ITransform.TYPE_SHA);
		if (appSecret.isLDAPSecret() && orgSecret.isUserAccept()) {
			ldapAccessDAO.updateSecret(lb.getRdn(), orgSecret.getUserLdapName(),appSecret);
		}
		secretDAO.updateSecret(orgSecret.getId(), loginName, shaHash, appSecret.getAllHashedSecret());
		
		return true;
	}
	
	private boolean insert(AppSecret appSecret, LdapBean lb){
		String loginName = appSecret.getUserLdapName();
		if (appSecret.isLDAPSecret()) {
			// 如果无须管理员审核，直接往LDAP里面插入，即时生效
			if (appSecret.isUserAccept()) {
				try{
					ldapAccessDAO.addAccount(lb, appSecret, loginName);
				}catch (NameAlreadyBoundException e){
					ldapAccessDAO.updateSecret(lb.getRdn(), appSecret.getUserLdapName(),appSecret);
				}
				sendUserAddMailToAppAdmin(lb, appSecret);
			} else if (appSecret.isUserApply()) {
				// 如果需要管理员审核，给管理员发一封邮件
				sendUserApplyMailToAppAdmin(lb, appSecret);
			}
		}
		secretDAO.insert(appSecret);
		return true;
	}
	

	@Override
	public void removeAllLdapSecret(int uid, String ldapUid) {
		secretDAO.deleteMyLdapSecret(uid);
		List<String> myDns = ldapAccessDAO.searchDn(ldapUid);
		for (String dn : myDns) {
			ldapAccessDAO.removeByDn(dn);
		}
	}

	@Override
	public List<AppSecret> findMyAppMember(String appId) {
		return secretDAO.findMyAppMember(appId);
	}

	@Override
	public void deleteMySecret(int secretId, int userId) {
		secretDAO.deleteMySecret(secretId, userId);
	}

	@Override
	public void openMember(LdapBean bean, AppSecret as, String loginName) {
		secretDAO.openMember(as.getId());
		ldapAccessDAO.addAccount(bean, as, loginName);
	}

	@Override
	public void deleteMember(String rdn, String loginName, int sId) {
		secretDAO.deleteMember(sId);
		ldapAccessDAO.removeSoAccount(rdn, loginName);
	}

	@Override
	public void sendToMember(LdapBean bean, User u, String string) {
		if (bean == null) {
			return;
		}
		Properties pro = new Properties();
		pro.setProperty("userName", CommonUtils.killNull(bean.getUserName()));
		pro.setProperty("email", CommonUtils.killNull(bean.getUserCstnetId()));
		pro.setProperty("appName", CommonUtils.killNull(bean.getClientName()));
		pro.setProperty("targetUserAccount", CommonUtils.killNull(u.getCstnetId()));
		if ("delete".equals(string)) {
			try {
				emailSender.send(new Locale("zh_CN"), u.getCstnetId(),bean.isWifiApp()?EmailTemplate.NOTICE_WIFI_DELETE_MEMBER:EmailTemplate.NOTICE_LDAP_DELETE_MEMBER, pro);
			} catch (MailException e) {
				LOG.error(e.getMessage(), e);
			}
		} else if ("pass".equals(string)) {
			try {
				emailSender.send(new Locale("zh_CN"), u.getCstnetId(),bean.isWifiApp()?EmailTemplate.NOTICE_WIFI_PASS_MEMBER:EmailTemplate.NOTICE_LDAP_PASS_MEMBER, pro);
			} catch (MailException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	@Override
	public AppSecret findAppSecretById(int sId) {
		return secretDAO.findAppSecretById(sId);
	}

	public void setSecretDAO(IAppSecretDAO secretDAO) {
		this.secretDAO = secretDAO;
	}

	public void setTransform(ITransform transform) {
		this.transform = transform;
	}

	public void setLdapAccessDAO(ILdapAccessDAO ldapAccessDAO) {
		this.ldapAccessDAO = ldapAccessDAO;
	}

	@Override
	public boolean isAppSecretUsed(String secret, int uid) {
		String transformedStr = transform.transform(secret.trim());
		return secretDAO.isSecretUsed(transformedStr, uid);
	}

	private AppSecret buildOneAppSecret(LdapBean ldapApp, User user, String newPassword, String secretType) {
		String loginName=EmailUtil.extractName(user.getCstnetId());
		
		AppSecret appSecret = new AppSecret();
		appSecret.setAppId(Integer.toString(ldapApp.getId()));
		appSecret.setSecretType(secretType);
		appSecret.setUserStatus(LdapBean.PRIV_NEED_APPLY.equals(ldapApp
				.getPriv())
				? AppSecret.USER_STATUS_APPLY
				: AppSecret.USER_STATUS_ACCEPT);
		appSecret.setRawPassword(newPassword);
		appSecret.setUid(user.getId());
		appSecret.setUserName(user.getTrueName());
		appSecret.setUserCstnetId(user.getCstnetId());
		appSecret.setUserLdapName(loginName);
		return appSecret;
	}
	
	@Override
	public boolean updateLdapPasswordOrInsertIfNotExist(LdapBean ldapApp, User user,
			String newPassword) {
		AppSecret appSecret = buildOneAppSecret(ldapApp, user, newPassword, AppSecret.SECRET_TYPE_LDAP);
		return updateOrInsertIfNotExist(appSecret, ldapApp);
	}
	
	@Override
	public boolean updateWifiPasswordOrInsertIfNotExist(LdapBean ldapApp, User user,
			String newPassword) {
		AppSecret appSecret = buildOneAppSecret(ldapApp, user, newPassword, AppSecret.SECRET_TYPE_WIFI);
		return updateOrInsertIfNotExist(appSecret, ldapApp);
	}
	@Override
	public boolean updateWifiPassword(LdapBean ldapApp, User user,
			String newPassword) {
		AppSecret appSecret = buildOneAppSecret(ldapApp, user, newPassword, AppSecret.SECRET_TYPE_WIFI);
		return update(appSecret,ldapApp);
	}

}

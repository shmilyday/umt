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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import java.util.Set;

import net.duckling.falcon.api.cache.ICacheService;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

import cn.vlabs.umt.common.mail.EmailTemplate;
import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.common.mail.MessageSender;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.EmailFormatChecker;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.CoreMailUserInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UserField;
import cn.vlabs.umt.services.user.dao.IBindThirdPartyDAO;
import cn.vlabs.umt.services.user.dao.IUserDAO;
import cn.vlabs.umt.services.user.dao.IUserLoginNameDAO;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.services.user.exception.UserNotFound;
import cn.vlabs.umt.services.user.service.IDomainService;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.service.ITransform;

public class UserServiceImpl implements UserService {
	
	@Override
	public void sendWarnUCUser(String username, String baseUrl){
		Properties prop = new Properties();
		String mergeUrl;
		try {
			mergeUrl = URLEncoder.encode(baseUrl+"/user/merge.do?act=show","UTF-8");
		} catch (UnsupportedEncodingException e) {
			mergeUrl="";
		}
		String fullUrl=RequestUtil.addParam(baseUrl+"/login", "WebServerURL", mergeUrl);
		fullUrl=RequestUtil.addParam(fullUrl, "appname", "mirror");
		prop.setProperty("fullUrl", fullUrl);
		try {
			email.send(new Locale("zh","cn"), username, EmailTemplate.MERGE_USER, prop);
		}catch (MailException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	@Override
	public User upgradeCoreMailUser(String loginName) {
		CoreMailUserInfo coreMailUserInfo=coreMailClient.getCoreMailUserInfo(loginName);
		if(coreMailUserInfo==null){
			return null;
		}
		User user=coreMailUserInfo.getUser();
		if(user!=null){
			user.setId(ud.create(user));
			if(user.getId()>0){
				loginNameDAO.createLoginName(user.getCstnetId(), user.getId(), LoginNameInfo.LOGINNAME_TYPE_PRIMARY,LoginNameInfo.STATUS_ACTIVE);
			}
			return user;
		}
		return null;
	}
	
	@Override
	public boolean isActivePrimaryEmail(String email){
		return loginNameDAO.isUsed(email);
	}
	@Override
	public String getTempSecurityEmail(int uid){
		Token tempSecurityToken=tokenService.getATokenByUidAndOperation(uid, Token.OPERATION_ACTIVATION_SECURITY_EMAIL, Token.STATUS_UNUSED);
		return tempSecurityToken==null?null:tempSecurityToken.getContent();
	}
	@Override
	public String getSecurityEmail(int uid) {
		Token securityToken=tokenService.getATokenByUidAndOperation(uid, Token.OPERATION_ACTIVATION_SECURITY_EMAIL, Token.STATUS_USED);
		return securityToken==null?null:securityToken.getContent();
	}
	@Override
	public void sendActivicationSecurityMail(Locale locale,int uid,String securityEmail, String baseUrl) {
		Properties prop = new Properties();
		Token token =tokenService.createToken(uid, Token.OPERATION_ACTIVATION_SECURITY_EMAIL,securityEmail);
		String activationUrl=RequestUtil.addParam(baseUrl+"/security/activation.do", "act", "activeSecurityEmail");
		activationUrl=RequestUtil.addParam(activationUrl, "random", token.getRandom());
		activationUrl=RequestUtil.addParam(activationUrl, "tokenid", token.getId()+"");
		prop.setProperty("activationUrl", activationUrl);
		try {
			email.send(locale, securityEmail, EmailTemplate.ACTIVATION_SECURITY_EMAIL, prop);
		}catch (MailException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information", e);
		}
	}
	@Override
	public void updateValueByColumn(int uid,String columnName, String value) {
		ud.updateValueByColumn(new int[]{uid},columnName,value);
	}
	@Override
	public void updateValueByColumn(int[] uid, String columnName, String value) {
		ud.updateValueByColumn(uid,columnName,value);
	}
	@Override
	public void sendComfirmToOldMail(Locale locale, int uid, String oldloginName, String baseUrl) {
		Properties prop = new Properties();
		tokenService.removeTokensUnsed(uid, Token.OPERATION_COMFIRM_PRIMARY_EMAIL);
		Token token = tokenService.createToken(uid, Token.OPERATION_COMFIRM_PRIMARY_EMAIL);
		String activationUrl=RequestUtil.addParam(baseUrl+"/primary/activation.do", "act", "confirmChangeLoginEmail");
		activationUrl=RequestUtil.addParam(activationUrl, "random", token.getRandom());
		activationUrl=RequestUtil.addParam(activationUrl, "tokenid", token.getId()+"");
		prop.setProperty("activationUrl", activationUrl);
		try {
			email.send(locale, oldloginName, EmailTemplate.CONFIRM_CHANGE_PRIMARY_EMAIL, prop);
		}catch (MailException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information", e);
		}
	}
	private boolean checkRefrequentlyLoginMainAndSecurity(int uid){
		String timeKey="login.mail.security.time."+uid;
		String countKey="login.mail.security.count."+uid;
		Long lastTime=(Long)cacheService.get(timeKey);
		//第一次
		if(lastTime==null){
			cacheService.set(timeKey,System.currentTimeMillis());
			cacheService.set(countKey, 1);
			return true;
		}
		//已过期
		if((System.currentTimeMillis()-lastTime)>3600*1000l){
			cacheService.set(timeKey,System.currentTimeMillis());
			cacheService.set(countKey, 1);
			return true;
		}
		Integer count=(Integer)cacheService.get(countKey);
		if(count==null||count==0){
			cacheService.set(countKey, 1);
			return true;
		}
		//超过允许次数
		if(count>=5){
			return false;
		}
		//可以发送邮件
		cacheService.set(countKey, ++count);
		return true;
	}
	@Override
	public boolean sendActivateionLoginMailAndSecurity(Locale locale, int uid, String loginName, String baseUrl,int loginNameInfoId) {
		if(!checkRefrequentlyLoginMainAndSecurity(uid)){
			return false;
		}
		Properties prop = new Properties();
		tokenService.removeTokensUnsed(uid,  Token.OPERATION_ACTIVATION_PRIMARY_AND_SECURITY);
		Token token = tokenService.createToken(uid, Token.OPERATION_ACTIVATION_PRIMARY_AND_SECURITY,loginName);
		String activationUrl=RequestUtil.addParam(baseUrl+"/primary/activation.do", "act", "activeLoginEmailAndSecurity");
		activationUrl=RequestUtil.addParam(activationUrl, "random", token.getRandom());
		activationUrl=RequestUtil.addParam(activationUrl, "tokenid", token.getId()+"");
		activationUrl=RequestUtil.addParam(activationUrl, "loginNameInfoId", loginNameInfoId+"");
		prop.setProperty("activationUrl", activationUrl);
		try {
			email.send(locale, loginName, EmailTemplate.ACTIVATION_LOGIN_EMAIL, prop);
		}catch (MailException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information", e);
		}
		return true;
		
	}
	@Override
	public void sendActivicationLoginMail(Locale locale,int uid,String toEmail,String baseUrl,boolean changeLoginName,int loginNameInfoId){
		Properties prop = new Properties();
		tokenService.removeTokensUnsed(uid,  Token.OPERATION_ACTIVATION_PRIMARY_EMAIL);
		Token token = tokenService.createToken(uid, Token.OPERATION_ACTIVATION_PRIMARY_EMAIL,toEmail);
		String activationUrl=RequestUtil.addParam(baseUrl+"/primary/activation.do", "act", "activeLoginEmail");
		activationUrl=RequestUtil.addParam(activationUrl, "random", token.getRandom());
		activationUrl=RequestUtil.addParam(activationUrl, "tokenid", token.getId()+"");
		activationUrl=RequestUtil.addParam(activationUrl, "loginNameInfoId", loginNameInfoId+"");
		if(changeLoginName){
			activationUrl=RequestUtil.addParam(activationUrl, "changeLoginName", changeLoginName+"");
		}
		prop.setProperty("activationUrl", activationUrl);
		try {
			email.send(locale, toEmail, EmailTemplate.ACTIVATION_LOGIN_EMAIL, prop);
		}catch (MailException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information", e);
		}
	}
	@Override
	public void sendActivicationSecondaryEmail(Locale locale, int uid, String toEmail, String baseUrl,
			int loginNameId,boolean isChange) {
		Properties prop = new Properties();
		Token token = tokenService.createToken(uid, Token.OPERATION_ACTIVATION_SECONDARY_EMAIL,toEmail);
		String activationUrl=RequestUtil.addParam(baseUrl+"/secondary/activation.do", "act", "activeSecondaryEmail");
		activationUrl=RequestUtil.addParam(activationUrl, "random", token.getRandom());
		activationUrl=RequestUtil.addParam(activationUrl, "tokenid", token.getId()+"");
		activationUrl=RequestUtil.addParam(activationUrl, "loginNameInfoId", loginNameId+"");
		activationUrl=RequestUtil.addParam(activationUrl, "changeLoginName", isChange+"");
		prop.setProperty("activationUrl", activationUrl);
		try {
			email.send(locale, toEmail, EmailTemplate.ACTIVATION_SECONDARY_EMAIL, prop);
		}catch (MailException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information", e);
		}
		
	}
	
	public UserServiceImpl(ITokenService tokenService, IUserDAO ud,
			MessageSender sender,IBindThirdPartyDAO bindDAO,IUserLoginNameDAO loginNameDAO,IDomainService domainService,ICacheService cacheService) {
		this.tokenService = tokenService;
		this.ud = ud;
		this.email = sender;
		this.coreMailClient=ICoreMailClient.getInstance();
		this.bindDAO=bindDAO;
		this.loginNameDAO=loginNameDAO;
		this.domainService=domainService;
		this.cacheService=cacheService;
	}

	public synchronized int create(User user,String status) throws InvalidUserNameException {
		if (transform != null) {
			user.setPassword(transform.transform(user.getPassword()));
		}
		if(User.USER_TYPE_CORE_MAIL.equals(user.getType())){
			user.setCstnetId(coreMailClient.formatEmail(user.getCstnetId()));
		}else{
			if(isUsed(user.getCstnetId())!=USER_NAME_UNUSED){
				throw new InvalidUserNameException(user);
			}
		}
		if(!User.USER_TYPE_CORE_MAIL.equals(user.getType())&&LoginNameInfo.STATUS_ACTIVE.equals(status)){
			user.setSecurityEmail(user.getCstnetId());
		}
	    user.setCstnetId(user.getCstnetId().trim());
		if (EmailFormatChecker.isValidEmail(user.getCstnetId())){
			user.setId(ud.create(user));
			loginNameDAO.createLoginName(user.getCstnetId(), user.getId(),LoginNameInfo.LOGINNAME_TYPE_PRIMARY,status);
			return user.getId();
		}else{
			throw new InvalidUserNameException(user);
		}
	}

	public void removeByUid(int userid) {
		ud.remove(userid);
	}
	
	public void update(User user, boolean withPassword){
		if (transform != null){
			user.setPassword(transform.transform(user.getPassword()));
		}
		if(withPassword){
			ud.update(user);
		}else{
			ud.updateWithoutPass(user);
		}
	}
	public void update(User user) {
		if (StringUtils.isEmpty(user.getPassword())) {
			ud.updateWithoutPass(user);
		} else {
			if (transform != null)
			{
				user.setPassword(transform.transform(user.getPassword()));
			}
			ud.update(user);
		}
	}
	@Override
	public void updatePassword(int uid, String password) {
		if (transform != null){
			password = transform.transform(password);
		}
		ud.updatePassword(uid, password);
	}
	@Override
	public void updateCoreMailPassword(String username, String password) {
		coreMailClient.changePassword(username, password);
	}
	@Override
	public void sendChangeMail(Locale locale,int uid, String loginName, String baseURL) throws UserNotFound {
		User user =getUserByUid(uid);
		if(user==null){
			throw new UserNotFound("loginName="+loginName);
		}
		tokenService.removeTokensUnsed(user.getId(), Token.OPERATION_CHANGE_PASSWORD);
		Token token=tokenService.createToken(user.getId(), Token.OPERATION_CHANGE_PASSWORD);
		Properties params = new Properties();
		params.setProperty("UserName", user.getTrueName()==null?user.getUmtId():user.getTrueName());
		params.setProperty("ChangeURL", baseURL + "/changepass?tokenid="
				+ token.getId() + "&random=" + token.getRandom());
		params.setProperty("CancelURL", baseURL
				+ "/changepass?act=cancel&tokenid=" + token.getId() + "&random="
				+ token.getRandom());
		Date now = new Date();
		params.setProperty("CurrentDate", DateFormatUtils.format(now,
				"yyyy-MM-dd"));
		Date threeDaysAfter = DateUtils.addDays(now, 1);
		params.setProperty("InvalidteTime", DateFormatUtils.format(
				threeDaysAfter, "yyyy-MM-dd"));

		try {
			email.send(locale, loginName, EmailTemplate.TARGET_PASSWORD, params);
		} catch (MailException e) {
			LOGGER.error(e.getMessage());
			LOGGER.debug("information:", e);
		}
	}
	
	public void removeToken(int tokenid) {
		tokenService.toUsed(tokenid);
	}
	public void setTransform(ITransform transform) {
		this.transform = transform;
	}
	
	public User getUserByUmtId(String umtId){
		List<String> umtIds=new ArrayList<String>();
		umtIds.add(umtId);
		return CommonUtils.first(ud.getUsersByUmtId(umtIds));
	}
	@Override
	public List<User> getUsersByUmtId(List<String> umtId) {
		return ud.getUsersByUmtId(umtId);
	}
	@Override
	public void removeUserExpectMe(String loginName, int uid) {
		List<Integer> uids=ud.getExpectMeByCstnetId(uid,loginName);
		int[] uidsArray=list2array(uids);
		remove(uidsArray);
	}
	private int[] list2array(List<Integer> list){
		if(CommonUtils.isNull(list)){
			return new int[0];
		}
		int[] result=new int[list.size()];
		int index=0;
		for(int i:list){
			result[index++]=i;
		}
		return result;
	}
	public User getUserByOpenid(String openid,String type,String url) {
		return ud.getUserByOpenid(openid,type,url);
	}

	public int getUserCount() {
		return ud.getUserCount();
	}

	public Collection<User> getUsers(int start, int count) {
		return ud.getUsers(start, count);
	}

	private ITransform transform;

	private ITokenService tokenService;

	private MessageSender email;

	private IUserDAO ud;
	
	private IBindThirdPartyDAO bindDAO;
	
	private IUserLoginNameDAO loginNameDAO;
	private IDomainService domainService;
	
	private ICacheService cacheService;
	
	@Override
	public User getUserByUid(int uid) {
		return ud.getUserByUid(uid);
	}
	/**
	 * CoreMail用户验证服务类
	 * */
	private ICoreMailClient coreMailClient;
	
	public void setCoreMailService(ICoreMailClient client){
		this.coreMailClient=client;
	}

	private static final Logger LOGGER = Logger.getLogger(UserServiceImpl.class);

	public void remove(int[] uids) {
		if(uids==null||uids.length==0){
			return;
		}
		ud.remove(uids);
		loginNameDAO.removeLoginNamesByUid(uids);
		bindDAO.deleteBindByUid(uids);
	}
	

	public Set<String> isExist(String... usernames) {
		if (CommonUtils.isNull(usernames)){
			return null;
		}
		String[] exists= loginNameDAO.isUsed(usernames);
		HashSet<String> set = new HashSet<String>();
		if (exists!=null){
			for (String username:exists){
				set.add(username.toLowerCase());
			}
		}
		for(String username:usernames){
			if(coreMailClient.isUserExt(username)){
				set.add(username);
			}
		}
		return set;
	}

	public Collection<User> search(String query,int  start, int count) {
		return ud.search(query, start, count, UserField.cstnetId, true);
	}

	public Collection<User> search(String query, int start, int count, UserField orderBy, boolean isAscendent) {
		return ud.search(query, start, count, orderBy, isAscendent);
	}
	
	public int searchCount(String query){
		return ud.searchCount(query);
	}

	public void create(List<User> users) {
		ud.create(users);
	}
	@Override
	public void bindThirdParty(BindInfo bindInfo) {
		bindDAO.bindThirdParty(bindInfo);
	}
	@Override
	public List<BindInfo> getBindInfosByUid(int uid) {
		return bindDAO.getBindInfosByUid(uid);
	}
	@Override
	public void deleteBindById(int bindId) {
		bindDAO.deleteBindById(bindId);
	}
	@Override
	public void deleteBindByUid(int uid) {
		bindDAO.deleteBindByUid(new int[]{uid});
	}
	@Override
	public User getUserByLoginName(String loginName) {
		return loginNameDAO.getUserByLoginName(loginName);
	}
	@Override
	public String getLastedUmtId() {
		return ud.getLastedUmtId();
	}
	@Override
	public int isUsed(String loginName) {	
		if(CommonUtils.isNull(loginName)){
			return USER_NAME_VALIDATE_ERROR;
		}
		loginName= loginName.toLowerCase();
		boolean result=loginNameDAO.isUsed(loginName);
		if(result){
			return USER_NAME_USED;
		}
		//如果coreMail那边也没有,那就得验证域名是否可用了
		result=coreMailClient.isUserExt(loginName);
		if(result){
			return USER_NAME_USED;
		}
		result=domainService.checkDomain(loginName);
		if(result){
			return USER_NAME_DOMAIN_NOT_ALLOWD;
		}
		return USER_NAME_UNUSED;
		
		
	}
	@Override
	public Collection<User> searchUmtOnly(String keyword, int offset, int size) {
		return ud.searchUmtOnly(keyword,offset,size);
	}
	@Override
	public void switchGEOInfo(User user) {
		ud.switchGEOInfo(user.getId(),user.isSendGEOEmailSwitch());
	}
}
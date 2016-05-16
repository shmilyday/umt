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

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.account.CoreMailAuthenticateResult;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.user.Credential;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.bean.AppSecret;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.CookieCredential;
import cn.vlabs.umt.services.user.bean.CoreMailUserInfo;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.OauthCredential;
import cn.vlabs.umt.services.user.bean.ThirdPartyCredential;
import cn.vlabs.umt.services.user.bean.TokenLoginCredential;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.dao.IUserDAO;
import cn.vlabs.umt.services.user.dao.IUserLoginNameDAO;
import cn.vlabs.umt.services.user.service.IAppSecretService;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.services.user.service.ITransform;

public class PasswordLogin implements LoginService {
	private static final Logger LOGGER=Logger.getLogger(PasswordLogin.class);
	private static Pattern smallLetterPattern = Pattern.compile("^[a-z]{8,50}$");
	private static Pattern bigLetterPattern=Pattern.compile("^[A-Z]{8,50}$");
	private static Pattern numberPattern=Pattern.compile("^[0-9]{8,50}$");
	
	private boolean isPasswordWeak(String password){
		if(CommonUtils.isNull(password)){
			return true;
		}
		password=CommonUtils.trim(password);
		if(password.length()>50||password.length()<8){
			return true;
		}
		Matcher sMatcher = smallLetterPattern.matcher(password);
		if(sMatcher.find()){
			return true;
		}
		Matcher bMatcher = bigLetterPattern.matcher(password);
		if(bMatcher.find()){
			return true;
		}
		Matcher nMatcher = numberPattern.matcher(password);
		if(nMatcher.find()){
			return true;
		}
		return false;
	}
	
	@Override
	public boolean passwordRight(Credential cred) {
		return loginAndReturnPasswordType(cred).getUser()!=null;
	}
	public PasswordLogin(){
		coreMailClient=ICoreMailClient.getInstance();
	}
	private LoginInfo doWebLoginCredential(TokenLoginCredential cred){
		LoginInfo info=new LoginInfo();
		if(cred==null||cred.getToken()==null){
			return info;
		}
		User umtUser=userDAO.getUserByUid(cred.getToken().getUid());
		if(umtUser==null){
			return info;
		}
		info.setPasswordType(cred.getToken().getPasswordType());
		/*info.setPasswordType(LoginInfo.TYPE_WEB_LOGIN);*/
		info.setUser(umtUser);
		info.setLoginNameInfo(loginNameDAO.getALoginNameInfo(umtUser.getId(),umtUser.getCstnetId()));
		return info;
	}
	public LoginInfo loginOauthAndReturnPasswordType(OauthCredential cred) {
		return null;
	}
	
	private LoginInfo doThirdPartyPasswordCredential(ThirdPartyCredential cred){
		LoginInfo info=new LoginInfo();
		if(cred==null){
			return info;
		}
		User umtUser=loginNameDAO.getUserByLoginName(cred.getUsername());
		if(umtUser==null){
			return info;
		}
		if(BindInfo.TYPE_SINA.equals(cred.getAuthBy())){
			info.setPasswordType(LoginInfo.TYPE_THIRD_PARTY_SINA);
		}else if(BindInfo.TYPE_QQ.equals(cred.getAuthBy())){
			info.setPasswordType(LoginInfo.TYPE_THIRD_PARTY_QQ);
		}else if(BindInfo.TYPE_CASHQ_SSO.equals(cred.getAuthBy())){
			info.setPasswordType(LoginInfo.TYPE_THIRD_PARTY_CAS_HQ);
		}else if(BindInfo.TYPE_UAF.equals(cred.getAuthBy())){
			info.setPasswordType(LoginInfo.TYPE_THIRD_PARTY_UAF);
		}else if(BindInfo.TYPE_CAS_GEO.equals(cred.getAuthBy())){
			info.setPasswordType(LoginInfo.TYPE_THIRD_PARTY_CAS_GEO);
		}else{
			info.setPasswordType(cred.getAuthBy());
		}
		info.setUser(umtUser);
		info.setLoginNameInfo(loginNameDAO.getALoginNameInfo(umtUser.getId(),cred.getUsername()));
		return info;
	}
	@Override
	public boolean coreMailPasswordRight(UsernamePasswordCredential cred) {
		return coreMailClient.authenticate(cred.getUsername(),cred.getPassword()).isSuccess();
	}
	@Override
	public boolean umtPasswrdRight(UsernamePasswordCredential cred) {
		boolean flag= false;
		User umtUser=loginNameDAO.getUserByLoginName(cred.getUsername());
		if(umtUser!=null&& transform.transform(cred.getPassword()).equals(umtUser.getPassword())){
			flag=true;
		}
		return flag;
	}
	private boolean validateCoreMailUser(CoreMailUserInfo userInfo,LoginInfo info){
		if(userInfo.isExpired()){
			info.setValidateResult(LoginInfo.VALIDATE_RESULT_USER_EXPIRED);
			return false;
		}
		if(CoreMailUserInfo.STATUS_LOCK.equals(userInfo.getStatus())){
			info.setValidateResult(LoginInfo.VALIDATE_RESULT_USER_LOCKED);
			return false;
		}
		if(CoreMailUserInfo.STATUS_STOP.equals(userInfo.getStatus())){
			info.setValidateResult(LoginInfo.VALIDATE_RESULT_USER_STOP);
			return false;
		}
		return true;
	}
	@Override
	public boolean oauthPasswordRight(OauthCredential cred) {
		return !CommonUtils.isNull(doOauthCredential(cred).getPasswordType());
	}
	private LoginInfo doOauthCredential(OauthCredential cred){
		LoginInfo info=new LoginInfo();
		if(cred==null){
			return info;
		}
		OauthClientBean ocb=oauthClientService.findByClientId(cred.getClientId());
		if(ocb==null||!"yes".equals(ocb.getEnableAppPwd())){
			LOGGER.info("oauth:the clientId ["+cred.getClientId()+"] for oauth, disable app secret or not found");
			return info;
		}
		User user=loginNameDAO.getUserByLoginName(cred.getUserName());
		if(user==null){
			LOGGER.info("oauth:can't found user["+cred.getUserName()+"]");
			return info;
		}
		if(User.ACCOUNT_STATUS_LOCKED.equals( user.getAccountStatus())){
			LOGGER.info("oauth: user["+cred.getUserName()+"]is locked!!!");
			info.setValidateResult(LoginInfo.VALIDATE_RESULT_USER_LOCKED);
			return info;
		}
		AppSecret appSecret=appSecretService.findAppSecretByUidAndAppId(cred.getClientId(),user.getId());
		if(appSecret==null){
			LOGGER.info("oauth:this user"+cred.getUserName()+" not have secret!");
			return info;
		}
		if(!transform.transform(CommonUtils.trim(cred.getPassword())).equals(appSecret.getSecret())){
			LOGGER.info("oauth:passwordError["+cred.getUserName()+"]");
			return info;
		}
		info.setLoginNameInfo(loginNameDAO.getALoginNameInfo(user.getId(),cred.getUserName()));
		info.setPasswordType("oauth."+cred.getClientId());
		info.setValidateResult("true");
		info.setUser(user);
		return info;
	}
	
	private LoginInfo doUsernamePasswordCredential(UsernamePasswordCredential cred){
		LoginInfo info=new LoginInfo();
		if(cred==null){
			return info;
		}
		LOGGER.info("validate user["+cred.getUsername()+"] start!");
		UsernamePasswordCredential ucred=(UsernamePasswordCredential)cred;
		String password = transform.transform(ucred.getPassword());
		User umtUser=loginNameDAO.getUserByLoginName(cred.getUsername());
		boolean isUmtUser=(umtUser!=null);
		//如果是umt用户且已经锁定，直接滚粗
		if(isUmtUser&&(User.ACCOUNT_STATUS_LOCKED.equals(umtUser.getAccountStatus()))){
			LOGGER.info("user ["+cred.getUsername()+"] is locked! but he want login");
			info.setValidateResult(LoginInfo.VALIDATE_RESULT_USER_LOCKED); 
			return info;
		}
		boolean isUmtPasswordRight=password.equals(isUmtUser?umtUser.getPassword():"");
		CoreMailAuthenticateResult coreMailResult=coreMailClient.authenticate(isUmtUser?umtUser.getCstnetId():ucred.getUsername(),ucred.getPassword());
		boolean isMailUser=coreMailResult.isValidUserName();
		boolean isMailPasswordRight=(isMailUser&&coreMailResult.isSuccess());
		LoginNameInfo loginNameInfo=null;
		if(isUmtUser){
			loginNameInfo=loginNameDAO.getALoginNameInfo(umtUser.getId(), ucred.getUsername());
		}
		//如果用户欲登陆的信息是，辅助账号，且是未验证的，那就没有必要继续下去了
		if(isUmtUser&&loginNameInfo.isTmpAndSecondary()){
			return info;
		}
		//mail验证通过，但是以前umt未存在的用户
		if(isMailPasswordRight&&(!isUmtUser)){
			if(!validateCoreMailUser(coreMailResult.getCoreMailInfo(),info)){
				return info;
			}
			if(loginNameDAO.isUsed(ucred.getUsername())){
				User tmpUser=loginNameDAO.getUserByLoginName(ucred.getUsername());
				info.setUser(tmpUser);
				info.setLoginNameInfo(loginNameDAO.getALoginNameInfo(tmpUser.getId(),tmpUser.getCstnetId()));
			}else{
				User user=coreMailResult.getCoreMailInfo().getUser();
				user.setId(userDAO.create(user));
				if(user.getId()>0){
					int infoId=loginNameDAO.createLoginName(user.getCstnetId(), user.getId(), LoginNameInfo.LOGINNAME_TYPE_PRIMARY,LoginNameInfo.STATUS_ACTIVE);
					info.setLoginNameInfo(loginNameDAO.getLoginNameInfoById(infoId));
					info.setUser(user);
				}
			}
			info.setPasswordType(LoginInfo.TYPE_CORE_MAIL);
			info.setWeak(isPasswordWeak(cred.getPassword()));
			info.setValidateResult(LoginInfo.VALIDATE_RESULT_SUCCESS); 
			return info;
		}
		//即是umt用户，也是mail用户，但是用mail密码登陆成功
		else if(isMailPasswordRight&&(isUmtUser)){
			if(!validateCoreMailUser(coreMailResult.getCoreMailInfo(),info)){
				return info;
			}
			if(!User.USER_TYPE_CORE_MAIL.equals(umtUser.getType())){
				umtUser.setType(User.USER_TYPE_MAIL_AND_UMT);
				userDAO.updateValueByColumn(new int[]{umtUser.getId()}, "type",User.USER_TYPE_MAIL_AND_UMT );
			}
			info.setUser(umtUser);
			info.setPasswordType(LoginInfo.TYPE_CORE_MAIL);
			info.setLoginNameInfo(loginNameInfo);
			info.setWeak(isPasswordWeak(cred.getPassword()));
			info.setValidateResult(LoginInfo.VALIDATE_RESULT_SUCCESS); 
			return info;
		}
		//umt验证通过，但是mail未存在的用户
		else if(isUmtPasswordRight&&(!isMailUser)){
			info.setUser(umtUser);
			info.setPasswordType(LoginInfo.TYPE_UMT);
			info.setLoginNameInfo(loginNameInfo);
			info.setValidateResult(LoginInfo.VALIDATE_RESULT_SUCCESS); 
			return info;
		}
		//即是umt用户，也是mail用户，但是用umt密码登陆成功
		else if(isUmtPasswordRight&&(isMailUser)){
			if(User.USER_TYPE_CORE_MAIL.equals(umtUser.getType())&&!CommonUtils.isNull(umtUser.getPassword())){
				//他authby为coreMail，且密码不为空，代表用户已经过合并
				return info;
			}
			if(User.USER_TYPE_UMT.equals(umtUser.getType())){
				umtUser.setType(User.USER_TYPE_MAIL_AND_UMT);
				userDAO.updateValueByColumn(new int[]{umtUser.getId()}, "type",User.USER_TYPE_MAIL_AND_UMT);
			}
			info.setUser(umtUser);
			info.setLoginNameInfo(loginNameInfo);
			info.setPasswordType(LoginInfo.TYPE_UMT);
			info.setValidateResult(LoginInfo.VALIDATE_RESULT_SUCCESS); 
			return info;
		}else{
			return info;
		}
	}
	@Override
	public LoginInfo loginAndReturnPasswordType(Credential cred) {
		if(cred==null){
			return new LoginInfo();
		}
		if (cred instanceof UsernamePasswordCredential){
			return doUsernamePasswordCredential((UsernamePasswordCredential)cred);
		}else if(cred instanceof ThirdPartyCredential){
			return doThirdPartyPasswordCredential((ThirdPartyCredential)cred);
		}else if(cred instanceof CookieCredential){
			return doCookieCredential((CookieCredential)cred);
		}else if(cred instanceof TokenLoginCredential){
			return doWebLoginCredential((TokenLoginCredential)cred);
		}else if(cred instanceof OauthCredential){
			return doOauthCredential((OauthCredential)cred);
		}
		return new LoginInfo();
	}
	/**
	 * @param cred
	 * @return
	 */
	private LoginInfo doCookieCredential(CookieCredential cred) {
		LoginInfo info=new LoginInfo();
		String value=cred.getCookieValue();
		String ip=cred.getIp();
		if(CommonUtils.isNull(value)||CommonUtils.isNull(ip)){
			LOGGER.debug("cookie log is error:["+value+","+ip+"]");
			return info;
		}
		String[] infos=value.split("/");
		if(CommonUtils.isNull(infos)||(infos.length!=3&&infos.length!=4)){
			LOGGER.debug("cookie value is error:["+Arrays.toString(infos)+"]");
			return info;
		}
		String cookieIp=infos[1];
		if(!ip.equals(cookieIp)){
			LOGGER.debug("cookie ip error! expect:"+ip+",but is:"+cookieIp);
			return info;
		}
		String cstnetId=infos[0];
		User umtUser=loginNameDAO.getUserByLoginName(cstnetId);
		
		if(umtUser==null){
			LOGGER.error("cookie error,umtUser is null by loginname="+cstnetId);
			return info;
		}
		info.setUser(umtUser);
		info.setPasswordType(infos[2]);
		info.setLoginNameInfo(loginNameDAO.getALoginNameInfo(umtUser.getId(), cstnetId));
		return info;
		
	}

	public void setTransform(ITransform transform){
		this.transform=transform;
	}
	private ITransform transform;
	private IUserDAO userDAO;
	private IUserLoginNameDAO loginNameDAO;
	private IOauthClientService oauthClientService;
	private IAppSecretService appSecretService;
	
	public void setAppSecretService(IAppSecretService appSecretService) {
		this.appSecretService = appSecretService;
	}
	/**
	 * CoreMail用户验证服务类
	 **/
	private ICoreMailClient coreMailClient;
	
	
	public void setOauthClientService(IOauthClientService oauthClientService) {
		this.oauthClientService = oauthClientService;
	}
	public void setLoginNameDAO(IUserLoginNameDAO loginNameDAO) {
		this.loginNameDAO = loginNameDAO;
	}
	public void setUserDAO(IUserDAO userDAO){
		this.userDAO=userDAO;
	}
	
}
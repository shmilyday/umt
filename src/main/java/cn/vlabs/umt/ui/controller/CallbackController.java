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
package cn.vlabs.umt.ui.controller;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.EmailFormatChecker;
import cn.vlabs.umt.oauth.AccessToken;
import cn.vlabs.umt.oauth.UMTOauthConnectException;
import cn.vlabs.umt.oauth.UserInfo;
import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;
import cn.vlabs.umt.services.auth.IAuthService;
import cn.vlabs.umt.services.auth.ThirdPartyAuth;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.Credential;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.ui.Attributes;

@Controller
@RequestMapping("/callback/{type}")
public class CallbackController {
	private static Logger LOG=Logger.getLogger(CallbackController.class);
	@Autowired
	private IAuthService authService;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;
	private String getSiteInfoParam(HttpServletRequest request){
		StringBuffer result=new StringBuffer();
		Map<String,String> siteInfo=SessionUtils.getSiteInfo(request);
		if(siteInfo!=null){
			for (String param:Attributes.SSO_PARAMS){
				if (siteInfo.get(param)!=null){
					result.append("&").append(param).append("=").append(siteInfo.get(param));
				}
			}
		}
		return result.toString();
	}
	/**
	 * 判断用户名密码是否正确
	 * */
	private boolean isValidUser(String username,String password){
		Credential credential = new UsernamePasswordCredential(username, password);
		LoginInfo prins = loginService.loginAndReturnPasswordType(credential);
		return prins.getUser()!=null;
	}
	
	@RequestMapping(params="act=bindUser")
	public String bindUser(HttpServletRequest request){
		//Exist same user
		String loginName=request.getParameter("loginName");
		String password=request.getParameter("loginPassword");
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		if(isValidUser(loginName,password)){
			//的确拥有该账户
			User user=userService.getUserByLoginName(loginName);
			int uid=user.getId();
			userService.bindThirdParty(new BindInfo(uid,screenName,openId,SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_TYPE),SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_URL )));
			return "redirect:/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request);
		}else{
			//账户名或者密码不存在
			request.setAttribute("hidden", false);
			request.setAttribute("loginPassword_error","login.password.wrong");
			ThirdPartyAuth auth= authService.find(type);
			request.setAttribute("thirdParty", auth);
			return "/inputPassword";
		}
	}
	
	@RequestMapping
	public String service(@PathVariable("type")String type, HttpServletRequest request){
		String code=request.getParameter("code");
		if (code==null){
			return "redirect:/index.jsp";
		}
		ThirdPartyAuth auth= authService.find(type);
		if (auth==null){
			LOG.error("Login type "+type+" is not supported.");
			return "redirect:/index.jsp";
		}
		
		try {
			AccessToken token = auth.createOauth().getAccessTokenByRequest(request);
			UserInfo userinfo = token.getUserInfo();
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_USER, userinfo.getTrueName());
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_ACCESS_TOKEN, token);
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_CODE, code);
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_TYPE, type);
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_OPEN_ID, userinfo.getCstnetId());
			
			if (isUserBinded(type, userinfo.getCstnetId())){
				return "redirect:/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request);
			}else{
				if (isValidEmail(userinfo.getCstnetId())){
					Set<String> usernames=userService.isExist(userinfo.getCstnetId());
					if (usernames.size()==0){
						String thirdpartyUrl = SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_URL);
						createAndBind(type, userinfo, thirdpartyUrl);
						return "redirect:/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request);
					}else{
						//Exist same user
						request.setAttribute("thirdParty", auth);
						return "inputPassword";
					}
				}else{
					//Not valid email
					request.setAttribute("thirdParty", auth);
					return "bindUser";
				}
			}

		} catch (UMTOauthConnectException | OAuthProblemException e) {
			LOG.error("access token can't get,because code["+code+"] is unkown or used!");
			LOG.error(e.getMessage(),e);
		}
		return "redirect:/index.jsp";
	}
	private void createAndBind(String siteName, UserInfo userinfo,
			String thirdpartyUrl) {
		User localUser;
		localUser=new User();
		localUser.setType(siteName);
		localUser.setCstnetId(userinfo.getCstnetId());
		localUser.setPassword(Integer.toString(new Random().nextInt(1000000)));
		localUser.setTrueName(userinfo.getTrueName());
		localUser.setSecurityEmail(userinfo.getSecurityEmail());
		try {
			int uid = userService.create(localUser, LoginNameInfo.STATUS_TEMP);
			BindInfo bindInfo = new BindInfo(uid,userinfo.getTrueName(),userinfo.getCstnetId(),siteName,thirdpartyUrl);
			userService.bindThirdParty(bindInfo);
		} catch (InvalidUserNameException e) {
			LOG.error("Error to create and bind user",e);
		}
	}
	private boolean isValidEmail(String cstnetId){
		return EmailFormatChecker.isValidEmail(cstnetId);
	}
	private boolean isUserBinded(String siteName, String cstnetId) {
		return userService.getUserByOpenid(cstnetId, siteName, null)!=null;
	}
}

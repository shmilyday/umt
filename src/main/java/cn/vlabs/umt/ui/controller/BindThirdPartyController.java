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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.Credential;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.ShowPageController;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.impl.BindThirdPartyFormValidator;
import cn.vlabs.umt.validate.formValidator.impl.CreateRequestFormValidator;
/**
 * 绑定微博与umt账户
 * @author lvly
 * @since 2013-2-1
 */
@Controller
@RequestMapping("/bind.do")
public class BindThirdPartyController{
	private static final Logger LOGGER = Logger.getLogger(BindThirdPartyController.class);
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private IUserLoginNameService loginNameService;
	/**
	 * 创建coreMail账户并与umt进行关联
	 * @throws InvalidUserNameException 
	 * @throws IOException 
	 * */
	@RequestMapping(params="act=createEmailAndBindUmt")
	public String createEmailAndBindUmt(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute CreateRequestForm rform) throws InvalidUserNameException, IOException{
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		if(CommonUtils.isNull(screenName)||CommonUtils.isNull(openId)||CommonUtils.isNull(type)){
			response.sendRedirect(ShowPageController.getMessageUrl(request, "common.data.un.equals"));
			return null;
		}
		User user = rform.getUser();
		if(BindInfo.TYPE_QQ.equals(type)){
			User umtUser=SessionUtils.getUser(request);
			userService.remove(new int[]{umtUser.getId()});
			user.setUmtId(umtUser.getUmtId());
		}
		
		ICoreMailClient coreMailClient=ICoreMailClient.getInstance();
		if (!coreMailClient.isUserExt(user.getCstnetId())) {
			boolean flag=coreMailClient.createUser(user.getCstnetId(),user.getTrueName(),user.getPassword());
			if(flag){
				user.setType(User.USER_TYPE_CORE_MAIL);
				user.setPassword(null);
				int uid=userService.create(user,LoginNameInfo.STATUS_ACTIVE);
				SessionUtils.setSessionVar(request, "createUser", user);
				tokenService.createToken(uid,Token.OPERATION_ACTIVATION_PRIMARY_EMAIL, user.getUmtId(),null,Token.STATUS_USED);
				userService.bindThirdParty(new BindInfo(uid,screenName,openId,type,SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_URL )));
				if(!CommonUtils.isNull(rform.getTempSecurityEmail())){
					userService.sendActivicationSecurityMail(new UMTContext(request).getLocale(), user.getId(), rform.getTempSecurityEmail(), ServiceFactory.getWebUrl(request));
				}
				request.setAttribute("sendEmail", rform.getTempSecurityEmail());
				return "redirect:/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request);
			}else{
				response.sendRedirect(ShowPageController.getMessageUrl(request, "email.service.exception"));
				return null;
			}
		} else {
			request.setAttribute("message", "regist.user.exist");
			return "/accountBind_createCoreMail";
		}
	}
	/**
	 * 创建umt账户并与umt进行关联
	 * */
	@RequestMapping(params="act=createAndBindUmt")
	public String createAndBindUmt(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute CreateRequestForm rform) throws IOException {
		ErrorMsgs msgs=new CreateRequestFormValidator(request).validateForm();
		if(!msgs.isPass()){
			return "/accountBind_createUmt";
		}
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		try {
			User user = rform.getUser();
			int uid=userService.create(user,LoginNameInfo.STATUS_TEMP);
			userService.bindThirdParty(new BindInfo(uid,screenName,openId,type,SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_URL )));
			int loginNameId = loginNameService.getLoginNameId(uid, user.getCstnetId(), LoginNameInfo.LOGINNAME_TYPE_PRIMARY);
			userService.sendActivateionLoginMailAndSecurity(new UMTContext(request).getLocale(), uid, rform.getUsername(),ServiceFactory.getWebUrl(request), loginNameId);
			response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request));
			return null;
		} catch (InvalidUserNameException e) {
			request.setAttribute("message", "regist.username.format");
			return "/accountBind_createUmt";
		}
	}
	/**
	 * 更新qq用户信息，是完善信息
	 * */
	@RequestMapping(params="act=updateQQInfo")
	public String updateQQInfo(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute CreateRequestForm rform) throws IOException {
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		if(CommonUtils.isNull(screenName)||CommonUtils.isNull(openId)||CommonUtils.isNull(type)||!BindInfo.TYPE_QQ.equals(type)){
			response.sendRedirect(ShowPageController.getMessageUrl(request, "common.data.un.equals"));
			return null;
		}
		User formUser = rform.getUser();
		User umtUser=userService.getUserByUid(SessionUtils.getUserId(request));
		if(umtUser==null){
			response.sendRedirect(RequestUtil.getContextPath(request)+"/");
			return null;
		}
		if(!CommonUtils.isNull(formUser.getCstnetId())||userService.isUsed(formUser.getCstnetId())==UserService.USER_NAME_UNUSED){
			userService.updateValueByColumn(umtUser.getId(), "cstnet_id", formUser.getCstnetId());
			loginNameService.updateLoginName(umtUser.getId(), umtUser.getCstnetId(), formUser.getCstnetId());
			userService.updateValueByColumn(umtUser.getId(), "type", User.USER_TYPE_UMT);
			int loginNameId = loginNameService.getLoginNameId(umtUser.getId(), formUser.getCstnetId(), LoginNameInfo.LOGINNAME_TYPE_PRIMARY);
			userService.sendActivateionLoginMailAndSecurity(new UMTContext(request).getLocale(), umtUser.getId(), rform.getUsername(),ServiceFactory.getWebUrl(request), loginNameId);
		}
		if(!CommonUtils.isNull(formUser.getTrueName())){
			userService.updateValueByColumn(umtUser.getId(), "true_name", formUser.getTrueName());
		}
		if(!CommonUtils.isNull(formUser.getPassword())){
			userService.updatePassword(umtUser.getId(),formUser.getPassword());
		}
		return "redirect:/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request);
	}
	/**
	 * 创建umt账户并与umt进行关联,QQ比较隔路
	 * */
	@RequestMapping(params="act=createAndBindQQUmt")
	public String createAndBindQQUmt(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute CreateRequestForm rform) throws IOException {
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		if(CommonUtils.isNull(screenName)||CommonUtils.isNull(openId)||CommonUtils.isNull(type)||!BindInfo.TYPE_QQ.equals(type)){
			response.sendRedirect(ShowPageController.getMessageUrl(request, "common.data.un.equals"));
			return null;
		}
		try {
			User user = rform.getUser();
			if(CommonUtils.isNull(user.getCstnetId())){
				user.setCstnetId(BindInfo.LIKE_EMAIL);
				user.setType(type);
			}else{
				if(userService.isUsed(user.getCstnetId())!=UserService.USER_NAME_UNUSED){
					request.setAttribute("username_error", "regist.user.exist");
					return "/accountBind_createUmt";
				}
			}
			if(CommonUtils.isNull(user.getTrueName())){
				user.setTrueName(screenName);
			}
			if(CommonUtils.isNull(user.getPassword())){
				user.setPassword(new Random().nextInt(1000000)+"");
			}
			
			int uid=userService.create(user,LoginNameInfo.STATUS_TEMP);
			userService.bindThirdParty(new BindInfo(uid,screenName,openId,type));
			return "redirect:/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request);
		} catch (InvalidUserNameException e) {
			request.setAttribute("message", "regist.username.format");
			return "/accountBind_createUmt";
		}
	}
	
	
	/**
	 * 创建umt账户并与umt进行关联
	 * */
	@RequestMapping(params="act=createAndBindWeixinUmt")
	public String createAndBindWeixinUmt(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute CreateRequestForm rform) throws IOException {
		//String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		if(CommonUtils.isNull(openId)||CommonUtils.isNull(type)||!BindInfo.TYPE_WEIXIN.equals(type)){
			response.sendRedirect(ShowPageController.getMessageUrl(request, "common.data.un.equals"));
			return null;
		}
		try {
			User user = rform.getUser();
			if(CommonUtils.isNull(user.getCstnetId())){
				user.setCstnetId(BindInfo.LIKE_EMAIL);
				user.setType(type);
			}else{
				if(userService.isUsed(user.getCstnetId())!=UserService.USER_NAME_UNUSED){
					request.setAttribute("username_error", "regist.user.exist");
					return "/accountBind_createUmt";
				}
			}
			/*if(CommonUtils.isNull(user.getTrueName())){
				user.setTrueName(screenName);
			}*/
			if(CommonUtils.isNull(user.getPassword())){
				user.setPassword(new Random().nextInt(1000000)+"");
			}
			
			int uid=userService.create(user,LoginNameInfo.STATUS_TEMP);
			userService.bindThirdParty(new BindInfo(uid,"",openId,type));
			return "redirect:/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request);
		} catch (InvalidUserNameException e) {
			request.setAttribute("message", "regist.username.format");
			return "/accountBind_weixin_umt";
		}
	}
	
	/**
	 * 把第三方账户与umt进行绑定
	 * */
	@RequestMapping(params="act=bindUmt")
	public String bindUmt(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute CreateRequestForm rform) throws IOException {
		ErrorMsgs msgs=new BindThirdPartyFormValidator(request).validateForm();
		String type=request.getParameter("type");
		if(!msgs.isPass()){
			if(BindInfo.TYPE_WEIXIN.equals(type)){
				return "/accountBind_weixin_umt";
			}
			return "/accountBind_createUmt";
		}
		String loginName=request.getParameter("loginName");
		String password=request.getParameter("loginPassword");
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		boolean result=checkUser(loginName,password,request);
		//的确拥有该账户
		if(result){
			if(BindInfo.TYPE_QQ.equals(type)){
				int oldUid=SessionUtils.getUserId(request);
				userService.remove(new int[]{oldUid});
			}
			User user=userService.getUserByLoginName(loginName);
			int uid=user.getId();
			userService.bindThirdParty(new BindInfo(uid,screenName,openId,SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_TYPE),SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_URL )));
			return "redirect:/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request);
		}
		//账户名或者密码不存在
		else{
			request.setAttribute("hidden", false);
			request.setAttribute("loginName_error","login.password.wrong");
			if(BindInfo.TYPE_WEIXIN.equals(type)){
				return "/accountBind_weixin_umt";
			}
			return "/accountBind_createUmt";
		}
	}
	/**
	 * 判断用户名密码是否正确
	 * */
	private boolean checkUser(String username,String password,HttpServletRequest request){
		Credential credential = new UsernamePasswordCredential(username, password);
		LoginInfo prins = loginService.loginAndReturnPasswordType(credential);
		return prins.getUser()!=null;
	}
	private String getSiteInfoParam(HttpServletRequest request) throws UnsupportedEncodingException{
		StringBuffer result=new StringBuffer();
		Map<String,String> siteInfo=SessionUtils.getSiteInfo(request);
		if(siteInfo!=null){
			for (String param:Attributes.SSO_PARAMS){
				if (siteInfo.get(param)!=null){
					result.append("&").append(param).append("=").append(URLEncoder.encode(siteInfo.get(param),"utf-8"));
					if(Attributes.RETURN_URL.equals(param)){
						try {
							if(StringUtils.endsWith(siteInfo.get(param), "?")){
								result.append(URLEncoder.encode("&pageinfo=userinfo", "UTF-8"));
							}else{
								result.append(URLEncoder.encode("?pageinfo=userinfo", "UTF-8"));
							}
						}catch (UnsupportedEncodingException e){
							LOGGER.error(e.getMessage(),e);
						}
					}
				}
			}
		}
		return result.toString();
	}
	

}
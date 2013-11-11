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
package cn.vlabs.umt.ui.actions;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.Credential;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.impl.BindThirdPartyFormValidator;
import cn.vlabs.umt.validate.formValidator.impl.CreateRequestFormValidator;
/**
 * 绑定微博与umt账户
 * @author lvly
 * @since 2013-2-1
 */
public class BindThirdPartyAction extends DispatchAction{
	private static final Logger LOGGER = Logger.getLogger(BindThirdPartyAction.class);
	/**
	 * 创建coreMail账户并与umt进行关联
	 * */
	public ActionForward createEmailAndBindUmt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		if(CommonUtils.isNull(screenName)||CommonUtils.isNull(openId)||CommonUtils.isNull(type)){
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "common.data.un.equals"));
			return null;
		}
		CreateRequestForm rform = (CreateRequestForm) form;
		User user = rform.getUser();
		UserService us = ServiceFactory.getUserService(request);
		if(BindInfo.TYPE_QQ.equals(type)){
			User umtUser=SessionUtils.getUser(request);
			us.remove(new int[]{umtUser.getId()});
			user.setUmtId(umtUser.getUmtId());
		}
		
		ICoreMailClient coreMailClient=ICoreMailClient.getInstance();
		if (!coreMailClient.isUserExt(user.getCstnetId())) {
			boolean flag=coreMailClient.createUser(user.getCstnetId(),user.getTrueName(),user.getPassword());
			if(flag){
				user.setType(User.USER_TYPE_CORE_MAIL);
				user.setPassword(null);
				int uid=us.create(user,LoginNameInfo.STATUS_ACTIVE);
				SessionUtils.setSessionVar(request, "createUser", user);
				ServiceFactory.getTokenService(request).createToken(uid,Token.OPERATION_ACTIVATION_PRIMARY_EMAIL, user.getUmtId(),null,Token.STATUS_USED);
				us.bindThirdParty(new BindInfo(uid,screenName,openId,type,SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_URL )));
				if(!CommonUtils.isNull(rform.getTempSecurityEmail())){
					us.sendActivicationSecurityMail(new UMTContext(request).getLocale(), user.getId(), rform.getTempSecurityEmail(), ServiceFactory.getWebUrl(request));
				}
				request.setAttribute("sendEmail", rform.getTempSecurityEmail());
				response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request));
				return null;
			}else{
				response.sendRedirect(ShowPageAction.getMessageUrl(request, "email.service.exception"));
				return null;
			}
		} else {
			request.setAttribute("message", "regist.user.exist");
			return mapping.findForward("bind.crete.mail.fail");
		}
	}
	/**
	 * 创建umt账户并与umt进行关联
	 * */
	public ActionForward createAndBindUmt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ErrorMsgs msgs=new CreateRequestFormValidator(request).validateForm();
		if(!msgs.isPass()){
			return mapping.findForward("bind.crete.umt.fail");
		}
		CreateRequestForm rform = (CreateRequestForm) form;
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		try {
			User user = rform.getUser();
			UserService us = ServiceFactory.getUserService(request);
			int uid=us.create(user,LoginNameInfo.STATUS_TEMP);
			us.bindThirdParty(new BindInfo(uid,screenName,openId,type,SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_URL )));
			us.sendActivateionLoginMailAndSecurity(new UMTContext(request).getLocale(), uid, rform.getUsername(),ServiceFactory.getWebUrl(request), ServiceFactory.getLoginNameService(request).getLoginNameId(uid, user.getCstnetId(), LoginNameInfo.LOGINNAME_TYPE_PRIMARY));
			response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request));
			return null;
		} catch (InvalidUserNameException e) {
			request.setAttribute("message", "regist.username.format");
			return mapping.findForward("bind.crete.umt.fail");
		}
	}
	/**
	 * 更新qq用户信息，是完善信息
	 * */
	public ActionForward updateQQInfo(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CreateRequestForm rform = (CreateRequestForm) form;
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		if(CommonUtils.isNull(screenName)||CommonUtils.isNull(openId)||CommonUtils.isNull(type)||!BindInfo.TYPE_QQ.equals(type)){
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "common.data.un.equals"));
			return null;
		}
		User formUser = rform.getUser();
		UserService us = ServiceFactory.getUserService(request);
		User umtUser=us.getUserByUid(SessionUtils.getUserId(request));
		if(umtUser==null){
			response.sendRedirect(RequestUtil.getContextPath(request)+"/");
			return null;
		}
		if(!CommonUtils.isNull(formUser.getCstnetId())||!us.isUsed(formUser.getCstnetId())){
			us.updateValueByColumn(umtUser.getId(), "cstnet_id", formUser.getCstnetId());
			ServiceFactory.getLoginNameService(request).updateLoginName(umtUser.getId(), umtUser.getCstnetId(), formUser.getCstnetId());
			us.updateValueByColumn(umtUser.getId(), "type", User.USER_TYPE_UMT);
			us.sendActivateionLoginMailAndSecurity(new UMTContext(request).getLocale(), umtUser.getId(), rform.getUsername(),ServiceFactory.getWebUrl(request), ServiceFactory.getLoginNameService(request).getLoginNameId(umtUser.getId(), formUser.getCstnetId(), LoginNameInfo.LOGINNAME_TYPE_PRIMARY));
		}
		if(!CommonUtils.isNull(formUser.getTrueName())){
			us.updateValueByColumn(umtUser.getId(), "true_name", formUser.getTrueName());
		}
		if(!CommonUtils.isNull(formUser.getPassword())){
			us.updatePassword(umtUser.getId(),formUser.getPassword());
		}
		response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request));
		return null;
	}
	/**
	 * 创建umt账户并与umt进行关联,QQ比较隔路
	 * */
	public ActionForward createAndBindQQUmt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		CreateRequestForm rform = (CreateRequestForm) form;
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		if(CommonUtils.isNull(screenName)||CommonUtils.isNull(openId)||CommonUtils.isNull(type)||!BindInfo.TYPE_QQ.equals(type)){
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "common.data.un.equals"));
			return null;
		}
		try {
			User user = rform.getUser();
			UserService us = ServiceFactory.getUserService(request);
			if(CommonUtils.isNull(user.getCstnetId())){
				user.setCstnetId(BindInfo.LIKE_EMAIL);
				user.setType(type);
			}else{
				if(us.isUsed(user.getCstnetId())){
					request.setAttribute("username_error", "regist.user.exist");
					return mapping.findForward("bind.crete.umt.fail");
				}
			}
			if(CommonUtils.isNull(user.getTrueName())){
				user.setTrueName(screenName);
			}
			if(CommonUtils.isNull(user.getPassword())){
				user.setPassword(new Random().nextInt(1000000)+"");
			}
			
			int uid=us.create(user,LoginNameInfo.STATUS_TEMP);
			us.bindThirdParty(new BindInfo(uid,screenName,openId,type));
			response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request));
			return null;
		} catch (InvalidUserNameException e) {
			request.setAttribute("message", "regist.username.format");
			return mapping.findForward("bind.crete.umt.fail");
		}
	}
	/**
	 * 把第三方账户与umt进行绑定
	 * */
	public ActionForward bindUmt(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ErrorMsgs msgs=new BindThirdPartyFormValidator(request).validateForm();
		if(!msgs.isPass()){
			return mapping.findForward("bind.crete.umt.fail");
		}
		String loginName=request.getParameter("loginName");
		String password=request.getParameter("loginPassword");
		String screenName=request.getParameter("screenName");
		String openId=request.getParameter("openId");
		String type=request.getParameter("type");
		boolean result=checkUser(loginName,password,request);
		//的确拥有该账户
		if(result){
			UserService userService=ServiceFactory.getUserService(request);
			if(BindInfo.TYPE_QQ.equals(type)){
				int oldUid=SessionUtils.getUserId(request);
				userService.remove(new int[]{oldUid});
			}
			User user=userService.getUserByLoginName(loginName);
			int uid=user.getId();
			userService.bindThirdParty(new BindInfo(uid,screenName,openId,SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_TYPE),SessionUtils.getSessionVar(request,Attributes.THIRDPARTY_URL )));
			response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type="+type+"&act=Validate&authBy="+type+getSiteInfoParam(request));
			return null;
		}
		//账户名或者密码不存在
		else{
			request.setAttribute("hidden", false);
			request.setAttribute("loginName_error","login.password.wrong");
			return mapping.findForward("bind.crete.umt.fail");
		}
	}
	/**
	 * 判断用户名密码是否正确
	 * */
	private boolean checkUser(String username,String password,HttpServletRequest request){
		Credential credential = new UsernamePasswordCredential(username, password);
		LoginInfo prins = ServiceFactory.getLoginService(request).loginAndReturnPasswordType(credential);
		return prins.getUser()!=null;
	}
	private String getSiteInfoParam(HttpServletRequest request){
		StringBuffer result=new StringBuffer();
		Map<String,String> siteInfo=SessionUtils.getSiteInfo(request);
		if(siteInfo!=null){
			for (String param:Attributes.SSO_PARAMS){
				if (siteInfo.get(param)!=null){
					result.append("&").append(param).append("=").append(siteInfo.get(param));
					if(Attributes.RETURN_URL.equals(param)){
						try {
							result.append(URLEncoder.encode("&pageinfo=userinfo", "UTF-8"));
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
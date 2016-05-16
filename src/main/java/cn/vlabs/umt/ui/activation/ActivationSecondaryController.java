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
package cn.vlabs.umt.ui.activation;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.ITokenService;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 激活辅助账号
 * @author lvly
 * @since 2013-3-6
 */
@Controller
@RequestMapping("/secondary/activation.do")
public class ActivationSecondaryController extends ActivationBaseController{
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;

	/**
	 * 激活辅助邮箱
	 * */
	@RequestMapping(params="act=activeSecondaryEmail")
	public synchronized String activeSecondaryEmail(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ActivationForm data, BindingResult result)  {
		String failUrl = getRedirectMessageUrl("active.login.email.fail");
		if (result.hasErrors()){
			return failUrl;
		}
		boolean isValid=tokenService.isValid(data.getTokenid(), data.getRandom(),Token.OPERATION_ACTIVATION_SECONDARY_EMAIL);
		//token无效
		if(!isValid){
			return failUrl;
		}
		Token token=tokenService.getTokenById(data.getTokenid());
		User user=userService.getUserByUid(token.getUid());
		AbstractDoActivation doAction=new DoActivationServiceForSecondary(request, response, token, user, data);
		return doAction.doActivation();
	}

	/**
	 * 做登陆操作
	 * */
	@RequestMapping(params="act=doLoginSecondary")
	public String doLoginSecondary(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ActivationForm data, BindingResult result) {
		request.setAttribute("token", data);
		request.setAttribute("_primaryEmail",request.getParameter("primaryEmail"));
		return "/activation/login_name_set_secondary_login";
	}
	/**
	 * 做密码是否正确操作
	 * */
	@RequestMapping(params="act=validPasswordSecondary")
	public String validPasswordSecondary(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ActivationForm data, BindingResult result){
		if (result.hasErrors()){
			return getRedirectMessageUrl("active.login.email.fail");
		}
		String loginName=request.getParameter("loginName");
		String password=request.getParameter("password");
		LoginInfo loginInfo=loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(loginName, password));
		if(loginInfo.getUser()!=null){
			UMTContext.saveUser(request.getSession(),loginInfo);
			return activeSecondaryEmail(request,response,data,result);
		}else{
			request.setAttribute("password_error", "security.email.password.error");
			request.setAttribute("token", data);
			request.setAttribute("_primaryEmail", loginName);
			return "/activate/login_name_set_secondary_login";
		}
	}

}
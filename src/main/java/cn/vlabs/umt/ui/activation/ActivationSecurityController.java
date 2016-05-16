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
@RequestMapping("/security/activation.do")
public class ActivationSecurityController extends ActivationBaseController{
	@Autowired
	private ITokenService tokenService;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;

	@RequestMapping(params="act=activeSecurityEmail")
	public String activeSecurityEmail(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ActivationForm data, BindingResult result) {
		if(result.hasErrors() ||!tokenService.isValid(data.getTokenid(), data.getRandom(), Token.OPERATION_ACTIVATION_SECURITY_EMAIL)){
			return getRedirectMessageUrl("active.security.email.fail");
		}
		Token token=tokenService.getTokenById(data.getTokenid());
		
		User user=userService.getUserByUid(token.getUid());
		AbstractDoActivation doAction=new DoActivationServiceForSecurity(request, response, token, user, data);
		return doAction.doActivation();
		
	}
	/**
	 * 密保邮箱登陆
	 * */
	@RequestMapping(params="act=doLoginSecurity")
	public String doLoginSecurity(HttpServletRequest request, HttpServletResponse response,@ModelAttribute ActivationForm data, BindingResult result){
		request.setAttribute("token", data);
		request.setAttribute("_securityEmail", request.getParameter("securityEmail"));
		request.setAttribute("_primaryEmail",request.getParameter("loginName"));
		return "/activation/security_email_login";
	}
	/**
	 * 做密码是否正确操作
	 * */
	@RequestMapping(params="act=validPasswordSecurity")
	public String validPasswordSecurity(HttpServletRequest request,
			HttpServletResponse response,@ModelAttribute ActivationForm data, BindingResult result) {
		String loginName=request.getParameter("loginName");
		String password=request.getParameter("password");
		LoginInfo loginInfo=loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(loginName, password));
		if(loginInfo.getUser()!=null){
			UMTContext.saveUser(request.getSession(),loginInfo);
			return activeSecurityEmail(request,response, data,result);
		}else{
			request.setAttribute("password_error", "security.email.password.error");
			return doLoginSecurity(request, response,data,result);
		}
	}

}
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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.duckling.cloudy.common.CommonUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.vlabs.umt.common.EmailUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.AppSecret;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IAppSecretService;
import cn.vlabs.umt.services.user.service.ILdapService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.ui.BaseController;

import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;
import com.octo.captcha.service.CaptchaServiceException;

@Controller
@RequestMapping("/user/enableWifi")
public class EnableWIFIController extends BaseController {
	@Autowired
	private IAppSecretService appSecretService;
	@Autowired
	private ILdapService ldapService;
	@Autowired
	private LoginService loginService;
	@Autowired 
	private UserService userService;
	@Autowired
	private IUserLoginNameService loginNameService;
	private static final String WIFI_INPUT_COUNT="wifi.counter";
	private static Logger log = Logger.getLogger(EnableWIFIController.class);
	
	private int getCurrentCount(HttpServletRequest request){
		HttpSession session = request.getSession();
		Integer count =(Integer)session.getAttribute(WIFI_INPUT_COUNT);
		if (count==null){
			count=1;
		}else{
			count=count+1;
		}
		session.setAttribute(WIFI_INPUT_COUNT, count);
		return count;
	}
	private boolean isValidateCodeRight(HttpServletRequest request, int count){
		//检查验证码
		if (count>3) {
			String validCode = request.getParameter("validateCode");
			try{
				return SimpleImageCaptchaServlet.validateResponse(request,validCode );
			}catch(CaptchaServiceException e){
			}
		}
		return true;
	}
	
	@RequestMapping(params="act=save")
	public String save(HttpServletRequest request, HttpServletResponse repsonse){
		try{
			User user = SessionUtils.getUser(request);
			String username=user.getCstnetId();
			String password=request.getParameter("password");
			//input validate check
			if (StringUtils.isEmpty(username)||StringUtils.isEmpty(password)){
				return showPage(request);
			}
			request.setAttribute("username", username);
			//check validate code
			int count=this.getCurrentCount(request);
			if (count>=3){
				request.setAttribute("showValidate", "true");
			}
			if (!isValidateCodeRight(request,count)){
				request.setAttribute("validateCodeError", "true");
				return  showPage(request);
			}
	
			//check password
			if (!loginService.passwordRight(new UsernamePasswordCredential(username, password))){
				request.setAttribute("passworderror", "wifi.password.error");
				return showPage(request);
			}
			
			LdapBean ldapBean = ldapService.findAvailableWifi(username);
			if (ldapBean==null || !ldapBean.inScope(username)){
				request.setAttribute("userDomain",EmailUtil.extractDomain(username));
				return "wifi/notopen";
			};
			
			appSecretService.updateWifiPasswordOrInsertIfNotExist(ldapBean, user, password);
			
			request.setAttribute("loginname", user.getCstnetId());
			request.setAttribute("clientName", ldapBean.getClientName());
			//Reset the counter
			HttpSession session = request.getSession();
			session.removeAttribute(WIFI_INPUT_COUNT);
			session.removeAttribute("showValidate");
			if(StringUtils.equals(LdapBean.PRIV_NEED_APPLY, ldapBean.getPriv())){
				return "wifi/waitApply";
			}
			return "wifi/message";
		}catch (Throwable e){
			log.error("Fail to enable wifi", e);
			throw e;
		}
	}
	@RequestMapping(params="act=canBeOpen")
	@ResponseBody
	public boolean canBeOpen(@RequestParam("clientId")String clientId, @RequestParam("username")String userName){
		User user = userService.getUserByLoginName(userName);
		if (user==null){
			return true;
		}
		AppSecret secret = appSecretService.findAppSecretByUidAndAppId(clientId, user.getId());
		return secret==null;
	}
	@RequestMapping(params = "act=isNameAvailable")
	public void isNameAvailable(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String newName = CommonUtils.trim(request.getParameter("ldapName"));
		String username = CommonUtils.trim(request.getParameter("username"));
		if (username!=null){
			User user = userService.getUserByLoginName(username);
			if (user!=null){
				LoginNameInfo loginNameInfo = loginNameService.getLdapLoginName(user.getId());
				if (loginNameInfo!=null && loginNameInfo.getLoginName().equals(newName)){
					response.getWriter().print(true);
					return;
				};
			}
		}
		boolean flag = loginNameService.isUsed(newName);
		response.getWriter().print(!flag);
	}
	@RequestMapping
	public String showPage(HttpServletRequest request) {
		User user = SessionUtils.getUser(request);
		String domain = EmailUtil.extractDomain(user.getCstnetId());
		request.setAttribute("userDomain", domain);
		LdapBean app = ldapService.findAvailableWifi(user.getCstnetId());
		if (app == null) {
			return "wifi/notopen";
		}
		
		request.setAttribute("app", app);
		LoginNameInfo loginInfo = loginNameService.getLdapLoginName(user.getId());
		request.setAttribute("user", user);
		request.setAttribute("loginNameInfo", loginInfo);
		if (app.inScope(user.getCstnetId())){
			AppSecret secret = appSecretService.findAppSecretByUidAndAppId(Integer.toString(app.getId()), user.getId());
			if (secret!=null){
				return "wifi/alreadyopen";
			}
			return "wifi/loggedin";
		}else{
			return "wifi/notopen";
		}
	}
}

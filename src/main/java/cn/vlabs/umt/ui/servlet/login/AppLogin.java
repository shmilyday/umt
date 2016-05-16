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
package cn.vlabs.umt.ui.servlet.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;
import cn.vlabs.duckling.common.util.Base64;
import cn.vlabs.umt.common.util.RandomUtil;
import cn.vlabs.umt.services.site.AppService;
import cn.vlabs.umt.services.site.Application;

public class AppLogin extends LoginMethod {
	public AppLogin(BeanFactory factory) {
		super(factory);
	}
	@Override
	protected boolean checkValidateCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		//检查验证码
		String savedRandom=(String) session.getAttribute("AppRandom");
		session.removeAttribute("AppRandom");
		String appRandom = request.getParameter("rand");
		if (savedRandom==null || !savedRandom.equals(appRandom)){
			request.setAttribute("message", "login.imagetext.wrong");
			doForward("/message.jsp", request ,response);
			return false;
		};
		return true;
	}
	protected void onWrongPassword(HttpServletRequest request, HttpServletResponse response,String errorMsg) throws ServletException, IOException{
		HttpSession session = request.getSession();
		String loginURL=(String) session.getAttribute("applogin.LoginURL");
		String vmtApp =(String) session.getAttribute("applogin.LoginAPP"); 
		
		forwardToLogin(request, response, loginURL, vmtApp,true);
	}
	@Override
	protected void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String loginURL=request.getParameter("LoginURL");
		String vmtApp = request.getParameter("LoginAPP");
		if (loginURL==null || vmtApp==null)
		{
			return;
		}
		HttpSession session = request.getSession();
		session.setAttribute("applogin.LoginURL", loginURL);
		session.setAttribute("applogin.LoginAPP", vmtApp);
		
		forwardToLogin(request, response, loginURL, vmtApp,false);
	}

	private void forwardToLogin(HttpServletRequest request, HttpServletResponse response, String loginURL, String vmtApp,boolean wrongValidCode) throws ServletException, IOException {
		AppService appservice= (AppService)factory.getBean("ApplicationService");
		Application app = appservice.getApplication(vmtApp);
		if (app==null)
		{
			return;
		}
		KeyFile kf = new KeyFile();
		RSAKey key = kf.loadFromString(app.getPublicKey());
		if (key!=null){
			RandomUtil ru = new RandomUtil();
			String random = ru.getRandom(5);
			
			byte[] result = key.encrypt(random.getBytes());
			
			String encrypted = Base64.encode(result);
			request.getSession().setAttribute("AppRandom", random);
			
			request.setAttribute("Encrypted", encrypted);
			request.setAttribute("LoginURL", loginURL);
			request.setAttribute("WrongValidCode", wrongValidCode);
			doForward("/loginpost.jsp", request, response);
		}
	}

}
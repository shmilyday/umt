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
package cn.vlabs.umt.ui.restfullapi;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.vmt.api.IRestOrgService;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.services.certificate.ICertificateService;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IUnitDomainService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.service.impl.PasswordLogin;
import cn.vlabs.umt.ui.jsapi.APIBaseServlet;

/**
 * 提供用户搜索功能 /api/users?q=query
 */
@Controller
@RequestMapping("/api-idp/validate/password")
public class APIPasswordValidate extends APIBaseServlet {
	@Autowired
    private UserService userService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private IUserLoginNameService loginNameService;
	@Autowired
	private IRestOrgService orgService;
	@Autowired
    private ICertificateService certs;
	@Autowired
	private IUnitDomainService unitDomainService;
	
	
	
	private static final Logger LOG=Logger.getLogger(PasswordLogin.class);
	
	
  // @RequestMapping(method=RequestMethod.POST)
   @RequestMapping
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	
    	
		if (ensureParamExist(request, response, "userName")&&ensureParamExist(request, response, "password")){
			String userName = request.getParameter("userName");
			String password = request.getParameter("password");
			LoginInfo loginInfo=loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(userName,password));
			
			if(!StringUtils.equals(loginInfo.getValidateResult(), "true")){
				JSONObject result=new JSONObject();
				result.put("validateResult", false);
				result.put("reasion", loginInfo.getValidateResult());
				saySuccess(response, result);
				return;
			}
			
			
			JSONObject result=new JSONObject();
			result.put("validateResult", true);
			result.put("userInfo", getUserAsJSON(userName, password));
			saySuccess(response, result);
		}
	}
    
	private  JSONObject getUserAsJSON( String userName,String password){
		LoginInfo loginInfo=loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(userName,password));
		User user=loginInfo.getUser();
		LoginNameInfo loginNameInfo = loginNameService.getALoginNameInfo(user.getId(), user.getCstnetId());
		String rootDomain=unitDomainService.getRootDomainByMailArreess(user.getCstnetId());
		
	    return IdpUserInfoUtils.buildUserInfoJSON(user, loginNameInfo,rootDomain);
	}
    
    
    
    
}

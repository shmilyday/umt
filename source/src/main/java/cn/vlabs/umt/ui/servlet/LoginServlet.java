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
package cn.vlabs.umt.ui.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.servlet.login.LocalLogin;

public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		addCrossDomainHeader(response);
		
		saveParams(request);
		request.setAttribute("basePath", request.getServerPort());
		LocalLogin login = new LocalLogin(factory);
		login.doLogin(request, response);
	}
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	public void init() throws ServletException {
		
		factory = (BeanFactory) getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY); 
	}
	
	private void addCrossDomainHeader(HttpServletResponse response){
		//允许IE跨域访问
		response.setHeader(	"P3P",
				"CP=\"IDC DSP COR ADM DEVi TAIi PSA PSD IVAi IVDi CONi HIS OUR IND CNT\"");
	}
	private void saveParams(HttpServletRequest request) {
		int port = request.getServerPort();
		HttpSession session = request.getSession();
		String basePath = request.getScheme() + "://" + request.getServerName();
		if(port != 80) {
			basePath += ":" + request.getServerPort();
		}
		session.setAttribute("rootPath", basePath);
		basePath += request.getContextPath();
		session.setAttribute("basePath", basePath);
		Map<String,String> siteInfo=new HashMap<String,String>();
		for (String param:Attributes.SSO_PARAMS){
			if (request.getParameter(param)!=null){
				siteInfo.put(param, request.getParameter(param));
			}
		}
		session.setAttribute(Attributes.SITE_INFO, siteInfo);
	}
	private BeanFactory factory;
}
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
package cn.vlabs.umt.ui.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.vlabs.umt.services.user.bean.ThirdPartyCredential;
import cn.vlabs.umt.ui.Attributes;

public class LoginThirdPartyApp extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;


	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
         this.doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		   ThirdPartyCredential tpc = (ThirdPartyCredential)request.getSession().getAttribute(Attributes.THIRDPARTY_CREDENTIAL);
           if(tpc!=null)
           {
        	   String loginThirdpartyAppURL = request.getParameter("loginThirdpartyAppURL");
        	   if(loginThirdpartyAppURL == null)
        	   {
        		   loginThirdpartyAppURL = "http://mail.cnic.cn/coremail/index.jsp";
               }
               request.setAttribute("loginThirdpartyAppURL", loginThirdpartyAppURL);
               String userName = request.getParameter("userName");
               if(userName == null){
            	   userName = "uid";
               }
               request.setAttribute("userName", userName);
               request.setAttribute("userNameValue", tpc.getUsername());
               String password = request.getParameter("password");
               if(password == null){
            	   password = "password";
               }
               request.setAttribute("password", password);
               request.setAttribute("passwordValue", tpc.getPassword());
               request.getRequestDispatcher("/loginThirdPartyApp.jsp").forward(request, response);

           }else{
        	   request.setAttribute("tip", "onlyCstnetUserLoginMail");
        	   request.getRequestDispatcher("/thirdpartyLostPassword.jsp").forward(request, response);
           }
     }

}
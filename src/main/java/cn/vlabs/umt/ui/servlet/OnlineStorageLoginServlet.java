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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.log4j.Logger;

import cn.vlabs.umt.services.user.bean.ThirdPartyCredential;
import cn.vlabs.umt.ui.Attributes;

public class OnlineStorageLoginServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final Logger LOGGER=Logger.getLogger(OnlineStorageLoginServlet.class);


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
		   String sid = (String)request.getSession(true).getAttribute("_onlineStorageUid");
		   sid =  sid==null?this.getSid(request):sid;
           if(sid!=null)
           {
        	   String location = null;
        	   if(onlineStorageURL.indexOf('?')<0)
        	   {
        		   location = onlineStorageURL+"?sid="+sid;
        	   }else
        	   {
        		   location = onlineStorageURL+"&sid="+sid;
        	   }
        	   request.getSession(true).setAttribute("_onlineStorageUid", sid);
        	   response.sendRedirect(location);
           }else{
        	   request.getRequestDispatcher("/thirdpartyLostPassword.jsp").forward(request, response);
           }
     }
	private String getSid(HttpServletRequest request)
	{
	   String uid = null;
	   ThirdPartyCredential tpc = (ThirdPartyCredential)request.getSession().getAttribute(Attributes.THIRDPARTY_CREDENTIAL);
	   if(tpc!=null)
	   {
				   HttpClient client = new HttpClient(new MultiThreadedHttpConnectionManager());
				   String loginThirdpartyAppURL = request.getParameter("loginThirdpartyAppURL");
				   if(loginThirdpartyAppURL == null)
				   {
					   loginThirdpartyAppURL = "http://mail.cnic.cn/coremail/index.jsp";
				   }
			  	   PostMethod method = new PostMethod(loginThirdpartyAppURL);
			  	   method.setParameter("uid", tpc.getUsername());
			  	   method.setParameter("password", tpc.getPassword());
			  	   method.setParameter("action:login", "");
			  	   method.setParameter("face", "H");
			  	   try {
					client.executeMethod(method);
				} catch ( IOException e) {
					 LOGGER.error(e.getMessage(),e);
				}finally{
					if(uid == null){
						   request.setAttribute("tip", "ssoLostPassword");
						   request.setAttribute(Attributes.RETURN_URL, request.getParameter(Attributes.RETURN_URL));
					}
				}
			  	   Header locationHeader = (Header)method.getResponseHeader("Location");
			  	   if(locationHeader!=null)
			  	   {
			  		   String location = locationHeader.getValue();
			  		   uid = location.substring(location.lastIndexOf("?")+5);
			  	   }
			  	   method.releaseConnection();
		  	       client.getHttpConnectionManager().closeIdleConnections(0);
	   }else
	   {
		   request.setAttribute("tip", "onlyCstnetUserLoginOnlineStorage");
	   }
  	   return uid;
	}
    private String onlineStorageURL = null;
	public void init(ServletConfig config) throws ServletException{
		onlineStorageURL = config.getInitParameter("onlineStorageURL");
	}

}
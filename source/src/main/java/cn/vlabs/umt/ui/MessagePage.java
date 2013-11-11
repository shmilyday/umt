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
package cn.vlabs.umt.ui;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public final class MessagePage {
	private MessagePage(){}
	public static ActionForward showNoMenuPage(String messagekey, HttpServletRequest request,ActionMapping mapping){
		request.setAttribute("WithoutMenu", "true");
		request.setAttribute("message", messagekey);
		return mapping.findForward("message");
	}
	
	public static ActionForward showMenuMessagePage(String messagekey, HttpServletRequest request,ActionMapping mapping){
		request.setAttribute("message", messagekey);
		return mapping.findForward("message");
	}
	
	public static void showNoMenuPage(String messagekey, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setAttribute("WithoutMenu", "true");
		request.setAttribute("message", messagekey);
		forward("/message.jsp", request, response);
	}
	
	public static void showMenuMessagePage(String messagekey, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		request.setAttribute("message", messagekey);
		forward("/message.jsp", request, response);
	}
	public static void forward(String url, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		RequestDispatcher rd = request.getSession().getServletContext().getRequestDispatcher(url);
		rd.forward(request, response);
	}
}
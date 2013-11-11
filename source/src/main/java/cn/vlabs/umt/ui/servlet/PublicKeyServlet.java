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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.services.site.AppService;
import cn.vlabs.umt.services.site.Application;
import cn.vlabs.umt.ui.Attributes;

public class PublicKeyServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyString = request.getParameter("keyid");
		if (keyString==null){
			wrongParameter(response);
			return;
		}
		try{
			AppService service = (AppService) factory.getBean("ApplicationService");
			int keyid=-1;
			String content;
			if ("umt".equals(keyString)){
				Application app = service.getApplication("umt");
				keyid=app.getKeyid();
				content=app.getPublicKey();
			}else{
				keyid = Integer.parseInt(keyString);
				content = service.getPublicKey(keyid);
			}
			if (content!=null){
				response.setContentType("text/plain");
				response.setHeader("keyid", Integer.toString(keyid));
				PrintWriter out = response.getWriter();
				out.write(content);
				out.flush();
				out.close();
			}else{
				notFound(response);
			}
		}catch (NumberFormatException e){
			wrongParameter(response);
			return;
		}
	}

	private void notFound(HttpServletResponse response) throws IOException {
		response.sendError(404);
	}

	private void wrongParameter(HttpServletResponse response) throws IOException{
		response.sendError(500);
	}
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	public void init() throws ServletException {
		factory = (BeanFactory) getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
	private BeanFactory factory;
}
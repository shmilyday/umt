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
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.vlabs.umt.services.site.AppService;
import cn.vlabs.umt.services.site.Application;
@Controller
@RequestMapping("/pubkey")
@Deprecated
public class PublicKeyController {
	@Autowired
	private AppService service;
	@RequestMapping(method=RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String keyString = request.getParameter("keyid");
		if (keyString==null){
			wrongParameter(response);
			return;
		}
		try{
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

}
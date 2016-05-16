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
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.cloudy.common.CommonUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.services.user.service.IClbService;
import cn.vlabs.umt.services.user.service.impl.FileSaverBridge;

@Controller
@RequestMapping("/logo")
public class LogoServlet{
	@Autowired
	private IClbService clbService;
	
	@RequestMapping
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		int logoId=Integer.parseInt(req.getParameter("logoId"));
		String size=req.getParameter("size");
		if(logoId==0){
			req.getRequestDispatcher("images/defaultApp.png").forward(req, resp);
			return;
		}
		if(CommonUtils.isNull(size)){
			clbService.download(logoId, new FileSaverBridge(resp,req));
		}
		else{
			clbService.downloadBySize(logoId, new FileSaverBridge(resp,req),size);
		}
	}
}

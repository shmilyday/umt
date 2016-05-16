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
package cn.vlabs.umt.ui.user;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.ui.UMTContext;

@Controller
@RequestMapping("/user/closeUser.do")
@Deprecated
public class CloseUserController {
	@Autowired
	private UserService us;
	@RequestMapping
	public String execute(HttpServletRequest request, HttpServletResponse response) {
		UMTContext context= new UMTContext(request);
		us.removeByUid((context.getCurrentUMTUser().getId()));
		request.getSession().invalidate();
		request.setAttribute("WithoutMenu", "true");
		request.setAttribute("message", "closeuser.message.userclosed");
		return "/message";
	}
}
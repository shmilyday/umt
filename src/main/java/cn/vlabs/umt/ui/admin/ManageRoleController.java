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
package cn.vlabs.umt.ui.admin;

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.user.bean.User;

@Controller
@RequestMapping("/admin/manageRole.do")
@Deprecated
public class ManageRoleController {
	@Autowired
	private RoleService rolService;

	@RequestMapping
	public String load(HttpServletRequest request, HttpServletResponse response) {
		Collection<User> users = rolService.getRoleMembers("admin");
		request.setAttribute("users", users);
		return "/admin/managerole";
	}

	@RequestMapping(params = "act=add")
	public String add(HttpServletRequest request, HttpServletResponse response) {
		String username = request.getParameter("username");
		rolService.addMember("admin", Integer.parseInt(username));
		return load(request, response);
	}

	@RequestMapping(params = "act=remove")
	public String remove(HttpServletRequest request,
			HttpServletResponse response) {
		String[] usernames = request.getParameterValues("usernames");
		rolService.removeMemeber("admin", usernames);
		return load(request, response);
	}
}
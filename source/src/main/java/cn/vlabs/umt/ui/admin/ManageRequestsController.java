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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cn.vlabs.umt.common.util.PageBean;
import cn.vlabs.umt.services.requests.RequestService;
import cn.vlabs.umt.services.requests.UserExist;
import cn.vlabs.umt.services.requests.UserRequest;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.ui.UMTContext;

@Controller
@RequestMapping("/admin/manageRequests.do")
@Deprecated
public class ManageRequestsController {
	@Autowired
	private RequestService us;
	@RequestMapping(params = "act=approve")
	public String approve(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("rid") int rid) {

		UMTContext context = new UMTContext(request);
		try {
			us.approveRequest(rid, context);
		} catch (UserExist e) {
			request.setAttribute("error", "message.registuser.exist");
		} catch (InvalidUserNameException e) {
			request.setAttribute("error", "message.registuser.exist");
		}
		return showRequests(request, response);
	}
	@RequestMapping(params = "act=deny")
	public String deny(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("rid") int rid) {
		UMTContext context = new UMTContext(request);
		us.denyRequest(rid, context);
		return showRequests(request, response);
	}
	@RequestMapping(params = "act=remove")
	public String remove(HttpServletRequest request,
			HttpServletResponse response, @RequestParam("rid") int rid) {
		UMTContext context = new UMTContext(request);
		us.removeRequest(rid, context);
		return showRequests(request, response);
	}
	@RequestMapping
	public String showRequests(HttpServletRequest request,
			HttpServletResponse response) {
		int total = 0;
		int page = 0;
		if (request.getParameter("total") != null) {
			try {
				total = Integer.parseInt(request.getParameter("total"));
				page = Integer.parseInt(request.getParameter("page"));
			} catch (NumberFormatException e) {
				total = us.getRequestCount(UserRequest.INIT);
			}
		} else {
			total = us.getRequestCount(UserRequest.INIT);
		}

		PageBean bean = new PageBean(total);
		bean.setCurrentPage(page);
		bean.setItems(us.getRequests(UserRequest.INIT, bean.getStart(),
				bean.getRecordPerPage()));

		request.setAttribute("PageBean", bean);
		return "/admin/requests";
	}
}
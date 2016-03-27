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

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.service.IAccessIPService;

/**
 * @author lvly
 * @since 2013-3-15
 */
@Controller
@RequestMapping("/admin/accessIps.do")
public class AccessIpsController {
	@Autowired
	private IAccessIPService accessIpService;
	/**
	 * 显示所有ip
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * */
	@RequestMapping
	public String showAccessIps(HttpServletRequest request,
			HttpServletResponse response) {
		request.setAttribute("ips", accessIpService.getAllAccessIps());
		return "/admin/access_ips_nav";
	}

	/**
	 * 删除
	 * */
	@RequestMapping(params = "act=delete")
	public String delete(HttpServletRequest request,
			HttpServletResponse response) {
		accessIpService.deleteIp(Integer.valueOf(request.getParameter("ipId")));
		return showAccessIps(request, response);
	}
	/**
	 * 增加
	 * */
	@RequestMapping(params = "act=add")
	public String add(HttpServletRequest request, HttpServletResponse response) {
		int userId = SessionUtils.getUserId(request);
		String ip = CommonUtils.trim(request.getParameter("ip"));
		String remark = CommonUtils.trim(request.getParameter("remark"));
		String scope = StringUtils.defaultIfEmpty(CommonUtils.trim(request.getParameter("scope")), "B");
		accessIpService.addAccessIp(userId, ip,scope, remark);
		return "redirect:/admin/accessIps.do";
	}
}
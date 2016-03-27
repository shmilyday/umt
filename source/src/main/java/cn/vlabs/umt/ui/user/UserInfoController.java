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

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;

/**
 * 个人资料
 * 
 * @author lvly
 * @since 2013-1-29
 */
@Controller
@RequestMapping("/user/info.do")
public class UserInfoController{
	@Autowired
	private UserService userService;
	/**
	 * 显示账户安全
	 * */
	@RequestMapping
	public String show() {
		return "/user/personalInfo";
	}
	/**
	 * 存储用户信息
	 * */
	@RequestMapping(params = "act=save")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String trueName = CommonUtils.trim(request.getParameter("trueName"));
		User user = userService.getUserByUid(SessionUtils.getUserId(request));
		user.setTrueName(trueName);
		userService.updateValueByColumn(user.getId(), "true_name", trueName);
		response.sendRedirect(RequestUtil.addParam(
				RequestUtil.getContextPath(request) + "/index.jsp", "msg",
				"userinfo.truename.change.success"));
	}
}
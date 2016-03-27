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

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.ShowPageController;

/**
 * 账户合并
 * 
 * @author lvly
 * @since 2013-2-4
 */
@Controller
@RequestMapping("/user/merge.do")
public class MergeController {
	@Autowired
	private UserService userService;
	/**
	 * 显示合并页面
	 * @throws IOException 
	 * */
	@RequestMapping(params = "act=show")
	public String show(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// 检查是否是该用户真的需要合并？
		User user = SessionUtils.getUser(request);
		if (user.getType().equals(User.USER_TYPE_MAIL_AND_UMT)) {
			return "/user/passportMerge";
		} else {
			response.sendRedirect(ShowPageController.getMessageUrl(request,
							"passport.merge.can.not"));
			return null;
		}
	}
	/**
	 * 合并用户
	 * */
	@RequestMapping(params = "act=merge")
	public String merge(HttpServletRequest request, HttpServletResponse response) {
		String username = SessionUtils.getUser(request).getCstnetId();
		String password = request.getParameter("password");
		boolean flag = ICoreMailClient.getInstance()
				.authenticate(username, password).isSuccess();
		if (flag) {
			return success(request, response);
		} else {
			request.setAttribute("password_error", "current.password.error");
			return "/user/passportMerge";
		}
	}
	private String success(HttpServletRequest request,
			HttpServletResponse response) {
		int userId = SessionUtils.getUserId(request);
		userService.updateValueByColumn(userId, "type",
				User.USER_TYPE_CORE_MAIL);
		return "redirect:"
				+ RequestUtil.addParam("/index.jsp", "msg",
						"passport.merge.success");
	}
}
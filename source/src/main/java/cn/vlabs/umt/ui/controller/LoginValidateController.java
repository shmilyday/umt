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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.validate.validator.Validator;
import cn.vlabs.umt.validate.validator.ValidatorFactory;
/**
 * 主要用于登陆页面，验证
 * 
 * @author lvly
 * @since 2013-2-1
 */
@Controller
@RequestMapping("/signin/hint.do")
public class LoginValidateController {
	/**
	 * 是否是coreMail账户
	 * */
	@RequestMapping(params = "act=isCoreMail")
	public void isCoreMail(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String email = request.getParameter("email");

		Validator emailValidate = ValidatorFactory.getEmailRegixValidator();
		response.setContentType("json");
		PrintWriter writer = response.getWriter();
		boolean isEmail = emailValidate.validate(email);
		if (isEmail) {
			writer.println(ICoreMailClient.getInstance().isUserExt(email));
		} else {
			writer.println(false);
		}
		writer.flush();
		writer.close();
	}
}
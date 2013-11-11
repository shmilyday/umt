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
package cn.vlabs.umt.ui.servlet.login;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.ui.Attributes;

public class LocalLogin extends LoginMethod {
	public LocalLogin(BeanFactory factory) {
		super(factory);
	}
	protected void redirectToLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		doForward("/login.jsp", request, response);
	}
	private static final String REQUIRED_VALID = "requireValid";
	protected boolean checkValidateCode(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session =request.getSession();
		//检查验证码
		String requireValid = (String)session.getAttribute(LocalLogin.REQUIRED_VALID);
		if (requireValid != null) {
			String savedCode = (String) session
					.getAttribute(Attributes.VALID_CODE);
			String validCode = request.getParameter("ValidCode");
			if (!StringUtils.equals(savedCode, validCode)) {
				request.setAttribute("WrongValidCode", "true");
				request.setAttribute("showValidCode", "true");
				if(!CommonUtils.isNull(validCode)){
					request.setAttribute("lastErrorValidCode", "error");
				}else{
					request.setAttribute("lastErrorValidCode", "required");
				}
				request.setAttribute("username", request.getParameter("username"));
				doForward("/login.jsp", request, response);
				session.removeAttribute(Attributes.VALID_CODE);
				return false;
			}
		}
		return true;
	}
	private static final String WRONG_INPUT_COUNT_KEY = "_wrongInputCountKey";
	@Override
	protected void onWrongPassword(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String counts =(String)(request.getSession().getAttribute(WRONG_INPUT_COUNT_KEY));
		if(counts!=null)
		{
			int iCounts = Integer.parseInt(counts);
			iCounts++;
			if(iCounts>2)
			{
				request.setAttribute("showValidCode", "true");
				request.setAttribute("WrongValidCode", "true");
				request.getSession().setAttribute(REQUIRED_VALID, "true");
			}else
			{
				request.getSession().setAttribute(WRONG_INPUT_COUNT_KEY, String.valueOf(iCounts));
			}
		}else{
			request.getSession().setAttribute(WRONG_INPUT_COUNT_KEY, "1");
		}
		request.setAttribute("WrongPassword", "true");
		doForward("/login.jsp", request, response);
	}
}
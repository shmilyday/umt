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
package cn.vlabs.umt.ui.servlet.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.vlabs.umt.ui.UMTContext;

public class LoginOnlyFilter implements Filter {
	public void destroy() {
	}
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		String contexPath=req.getContextPath();
		if (hasLogin(req)){
			chain.doFilter(request, response);//Ignored
			return;
		}else{
			HttpServletResponse resp = (HttpServletResponse)response;
			resp.sendRedirect(contexPath+"/login");
		}
	}

	private boolean hasLogin(HttpServletRequest req) {
		HttpSession session = req.getSession(false);
		if (session==null){
			return false;
		}
		return UMTContext.getLoginInfo(session).getUser()!=null;
	}

	public void init(FilterConfig config) throws ServletException {
	}
}
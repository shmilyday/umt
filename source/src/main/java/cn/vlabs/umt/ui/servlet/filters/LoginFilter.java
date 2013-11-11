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

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

public class LoginFilter implements Filter {
	public void destroy() {
	}
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest)request;
		HttpServletResponse res = (HttpServletResponse)response;
		String contexPath=req.getContextPath();
		if (hasLogin(req)){
			User user=UMTContext.getLoginInfo(req.getSession()).getUser();
			//未激活用户登录
			LoginNameInfo info=ServiceFactory.getLoginNameService(req).getALoginNameInfo(user.getId(), user.getCstnetId());
			if(info.getType().equals(LoginNameInfo.LOGINNAME_TYPE_PRIMARY)
					&&LoginNameInfo.STATUS_TEMP.equals(info.getStatus())){
				String showUrl=RequestUtil.getContextPath(req)+"/show.do";
				showUrl=RequestUtil.addParam(showUrl, "act", "showFilterActive");
				
				String openId=SessionUtils.getSessionVar(req, Attributes.THIRDPARTY_OPEN_ID);
				showUrl=RequestUtil.addParam(showUrl, "oper","login");
				if(CommonUtils.isNull(openId)){
					showUrl=RequestUtil.addParam(showUrl, "type", user.getType());
				}else{
					showUrl=RequestUtil.addParam(showUrl, "type",SessionUtils.getSessionVar(req, Attributes.THIRDPARTY_TYPE));
				}
				
				showUrl=RequestUtil.addParam(showUrl, "sendEmail", user.getCstnetId());
				res.sendRedirect(showUrl);
				return;
			}else{
				chain.doFilter(request, response);//Ignored
			}
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
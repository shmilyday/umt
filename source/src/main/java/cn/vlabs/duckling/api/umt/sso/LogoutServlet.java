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
package cn.vlabs.duckling.api.umt.sso;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.vlabs.duckling.common.util.ClassUtil;

/**
 * Introduction Here.
 * 
 * @date 2010-7-1
 * @author Fred Zhang (fred@cnic.cn)
 */
public class LogoutServlet extends HttpServlet {
	private static final long serialVersionUID = -198729384L;

	public void service(HttpServletRequest request, HttpServletResponse response)
			throws IOException {

		String defaultRetoLocalAppUrl = SSOProperties.getInstance()
				.getProperty(ILoginHandle.LOCALAPP_LOGIN_RETURN);
		if (defaultRetoLocalAppUrl == null
				|| defaultRetoLocalAppUrl.trim().length() == 0) {
			defaultRetoLocalAppUrl = "http://" + request.getLocalAddr();
			if (request.getLocalPort() != 80) {
				defaultRetoLocalAppUrl = defaultRetoLocalAppUrl + ":"
						+ request.getLocalPort();
			}
			defaultRetoLocalAppUrl = defaultRetoLocalAppUrl
					+ request.getContextPath();
		}
		String umtSsoLogout = request.getParameter("umtSsoLogout");
		UserContext context = SessionUtil.getUserContext(request);
		if (context != null) {
			String loginHandClass = SSOProperties.getInstance().getProperty(
					ILoginHandle.UMT_LOGIN_EXTHANDLE_CLASS);
			Object object = null;
			if (loginHandClass != null) {
				object = ClassUtil.classInstance(loginHandClass);
				if (object != null) {
					((ILoginHandle) object).destroyBeforeLogout(request,
							response, context);
				}
			}
			if (umtSsoLogout == null) {
				String sid = request.getSession().getId();
				String logoutURL = buildLogoutURL(
						defaultRetoLocalAppUrl, sid);
				response.sendRedirect(logoutURL);
			}
		} else {
			if (umtSsoLogout == null) {
				response.sendRedirect(defaultRetoLocalAppUrl);
			}
		}
		HttpSession session = request.getSession(false);
		if (session != null)
		{
			session.invalidate();
		}
	}

	private String buildLogoutURL(
			String defaultRetoLocalAppUrl, String sid) {
		String url;
		try {
			url = SSOProperties.getInstance().getProperty(
					ILoginHandle.UMT_LOGOUT_URL_KEY)
					+ "?appname="
					+ URLEncoder.encode(SSOProperties.getInstance()
							.getProperty(ILoginHandle.UMT_AUTH_APPNAME_KEY),
							"UTF-8")
					+ "&sid="
					+ sid
					+ "&WebServerURL="
					+ URLEncoder.encode(defaultRetoLocalAppUrl, "UTF-8");
			return url;
		} catch (UnsupportedEncodingException e) {
			// Impossible
		}
		return null;
	}
}
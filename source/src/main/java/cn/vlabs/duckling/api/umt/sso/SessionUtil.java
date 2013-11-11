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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

/**
 * Introduction Here.
 * @date 2010-6-29
 * @author Fred Zhang (fred@cnic.cn)
 */
public final class SessionUtil {
	private static final String CLASSNAME = SessionUtil.class.getName();
	private static final Logger logger = Logger.getLogger(CLASSNAME);

	public static final String USER_CONTEXT = "SSO_USER_CONTEXT";
	public static final String USER_REDIRECT_URL = "USER_REDIRECT_URL";

	private SessionUtil() { 
		// do not allow instances of this class
	}
    public static String getUserRedirectUrl(HttpServletRequest pRequest)
    {
    	return (String)pRequest.getSession().getAttribute(SessionUtil.USER_REDIRECT_URL);
    }
    public static void setUserRedirectUrl(HttpServletRequest pRequest,String redirectUrl)
    {
    	pRequest.getSession().setAttribute(SessionUtil.USER_REDIRECT_URL, redirectUrl);
    }
	public static UserContext getUserContext(HttpServletRequest pRequest) {
		UserContext usrctx = null;
		final HttpSession httpSession = pRequest.getSession(false);
		if (httpSession != null) {
			final Object obj = getObj(pRequest,SessionUtil.USER_CONTEXT);
			if (obj instanceof UserContext) {
				usrctx = (UserContext) obj;
				logger.debug("Found UserContext in session: " + usrctx);
			} else {
				if (obj != null) {
					logger.error("Session attribute '"+ SessionUtil.USER_CONTEXT + "' is not of type '"
							+ UserContext.class.getName() + "': " + obj,
							new Exception());
				} else {
					logger.debug("The user context session attribute is null!",new Exception());
				}
			}
		} else {
			logger.warn("No HTTP session available!");
		}
		return usrctx;
	}
	public static Object getObj(HttpServletRequest request,String key){
		final HttpSession httpSession = request.getSession(false);
		if(httpSession!=null){
			return httpSession.getAttribute(key);
		}
		return null;
	}
	public static void setUserContext(HttpServletRequest pRequest,
			UserContext userctx) {
		setObject(pRequest,SessionUtil.USER_CONTEXT,userctx);
	}
	public static void setObject(HttpServletRequest request,String key,Object value){
		final HttpSession httpSession = getHttpSession(request);
		httpSession.setAttribute(key,value);
	}

	public static HttpSession getHttpSession(HttpServletRequest pRequest) {
		return pRequest.getSession(false);
	}  
}
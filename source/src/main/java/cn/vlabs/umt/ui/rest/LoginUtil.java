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
package cn.vlabs.umt.ui.rest;

import java.security.Principal;

import cn.vlabs.rest.RestSession;

public final class LoginUtil {
	private LoginUtil(){}
	private static final String REST_PRINCIPAL_KEY="rest.principal";
	private static final String REST_LOGGED_IN_KEY="rest.login";
	
	public static void savePrincipal(RestSession session, Principal principal){
		session.setAttribute(REST_PRINCIPAL_KEY, principal);
		session.setAttribute(REST_LOGGED_IN_KEY, Boolean.TRUE);
	}
	
	public static void loginHasBeenDone(RestSession session){
		session.setAttribute(REST_LOGGED_IN_KEY, Boolean.TRUE);
	}
	
	public static boolean isLoggedin(RestSession session){
		return Boolean.TRUE.equals(session.getAttribute(REST_LOGGED_IN_KEY));
	}
	
	public static boolean hasPermission(RestSession session){
		Principal prin = (Principal) session.getAttribute("rest.principal");
		return (prin != null); 
	}
}
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
package cn.vlabs.umt.common.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * @author lvly
 * @since 2013-1-17
 */
public final class APPUrlUtils {
	/**
	 * 
	 */
	private APPUrlUtils() {
		// TODO Auto-generated constructor stub
	}
	/**默认的应用映射*/
	public static final Map<String,String> URL_MAPPING=new HashMap<String,String>();
	
	static{
		URL_MAPPING.put("escience", "http://www.escience.cn");
		URL_MAPPING.put("ddl", "http://ddl.escience.cn");
		URL_MAPPING.put("dhome", "http://www.escience.cn/people");
		URL_MAPPING.put("csp", "http://csp.escience.cn");
		URL_MAPPING.put("mail", "http://mail.escience.cn");
		URL_MAPPING.put("site", "http://www.escience.cn/site");
		URL_MAPPING.put("rol", "http://rol.escience.cn");
	}
	public static void setURLtoRequest(HttpServletRequest request){
		String appName=(String)request.getAttribute("appName");
		String loginURL=(String)request.getAttribute("loginURL");
		request.setAttribute("escience_loginUrl","escience".equals(appName)?loginURL:URL_MAPPING.get("escience"));
		request.setAttribute("ddl_loginUrl","ddl".equals(appName)?loginURL:URL_MAPPING.get("ddl"));
		request.setAttribute("dhome_loginUrl","dhome".equals(appName)?loginURL:URL_MAPPING.get("dhome"));
		request.setAttribute("csp_loginUrl","csp".equals(appName)?loginURL:URL_MAPPING.get("csp"));
		request.setAttribute("mail_loginUrl","mail".equals(appName)?loginURL:URL_MAPPING.get("mail"));
		request.setAttribute("site_loginUrl","site".equals(appName)?loginURL:URL_MAPPING.get("site"));
		request.setAttribute("rol_loginUrl","rol".equals(appName)?loginURL:URL_MAPPING.get("rol"));
	}
	public static String getUrl(String appName){
		return URL_MAPPING.get(appName);
	}
}
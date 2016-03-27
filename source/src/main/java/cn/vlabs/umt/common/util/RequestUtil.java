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

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;


/**
 * Introduction Here.
 * @date 2013-1-23
 * @author lvly
 */
public final class RequestUtil {
	
	private static final int HTTPS_DEFAULT_PORT = 443;
	private static final int HTTP_DEFAULT_PORT = 80;
	
	private RequestUtil(){
		
	}
	/**
	 * 获得url，带contextPath
	 * @param request http请求
	 * */
	public static String getContextPath(HttpServletRequest request){
		String url=request.getContextPath();
		if(CommonUtils.isNull(url)){
			return "";
		}
		return url;
	}
	/**
	 * 获得请求的url，完整,包括post请求的所有东西都会构建成url
	 * @param request http请求
	 * @return 用于请求的url，浏览器打的什么，这里就应该体现神马
	 * */
	public static String getFullRequestUrl(HttpServletRequest request){
		String url=request.getRequestURL().toString();
		for(Enumeration<String> paramNames=request.getParameterNames();paramNames.hasMoreElements();){
			String pName=paramNames.nextElement();
			url=addParam(url, pName, request.getParameter(pName));
		}
		return url;
	}
	/**
	 * 给url添加参数，如果已经有别的参数就加&，如果没有就加？
	 * @param url 目标url
	 * @param key 参数key
	 * @param value 参数值
	 * */
	public static String addParam(String url,String key,String value){
		if(CommonUtils.isNull(url)||CommonUtils.isNull(key)||CommonUtils.isNull(value)){
			return url;
		}else if(url.contains(key+"=")){
			return url;
		}else{
			if(url.contains("?")){
				return url+"&"+key+"="+value;
			}else{
				return url+"?"+key+"="+value;
			}
		}
	}
	/**
	 * 获得浏览器信息
	 * @param request
	 * */
	public static String getBrowseType(HttpServletRequest request){
		return request.getHeader("User-Agent");
	}
	/**
	 * 获取远程机器的ip,直接用jspAPI的话，用nginx之类的中间层，取不到真实ip
	 * @param request
	 * */
	public static String getRemoteIP(HttpServletRequest request){
		if (request.getHeader("x-forwarded-for") == null) {  
			 return request.getRemoteAddr();  
		}  
		return request.getHeader("x-forwarded-for");  
	}
	
	
	public static String getBaseURL(HttpServletRequest request) {
		
		String url = request.getScheme() + "://" + request.getServerName();
		int port = request.getServerPort();
		if ((port != HTTP_DEFAULT_PORT) && (port != HTTPS_DEFAULT_PORT)) {
			url = url + ":" + port;
		}
		
		return url+ request.getContextPath();
	}

	
	
}
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
import java.util.Locale;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.duckling.cloudy.common.CommonUtils;

public class CharacterFilter implements Filter {
	public void destroy() {
		charset = null;
	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		request.setCharacterEncoding(charset);
		LocalRequest req = new LocalRequest((HttpServletRequest)request);
		chain.doFilter(req, response);
	}

	public void init(FilterConfig config) throws ServletException {
		charset = config.getInitParameter("charset");
		if (charset==null)
		{
			charset="UTF-8";
		}
	}
	
	private static class LocalRequest extends HttpServletRequestWrapper{
		public LocalRequest(HttpServletRequest request) {
			super(request);
			//add by lvly
			String localeStr= request.getParameter("locale");
			if(CommonUtils.isNull(localeStr)||(!"en_US".equals(localeStr)&&!"zh_CN".equals(localeStr))){
				localeStr=getCookie("umt.locale");
			}
			setLocaleLogic(request,localeStr);
			
		}
		public void setLocaleLogic(HttpServletRequest request,String localeStr){
			if (localeStr!=null){
				locale = getloacle(localeStr);
				if (locale!=null){
					request.setAttribute(
							"javax.servlet.jsp.jstl.fmt.locale.request",locale);
					request.setAttribute("org.apache.struts.action.LOCALE",locale);
					request.getSession(true).setAttribute("user_locale", localeStr);
				}
			}else if(isNotZhOrEn(request.getLocale())){
				request.setAttribute("javax.servlet.jsp.jstl.fmt.locale.request",Locale.ENGLISH);
				request.getSession(true).setAttribute("user_locale", Locale.ENGLISH.toString());
			}else{
				request.getSession(true).setAttribute("user_locale", request.getLocale().toString());
			}
			
		}
		private boolean isNotZhOrEn(Locale locale){
			boolean result=false;
			if(locale!=null){
				result=!Locale.ENGLISH.equals(locale)
						&&!Locale.US.equals(locale)
						&&!Locale.CHINA.equals(locale)
						&&!Locale.CHINESE.equals(locale);
				
			}
			return result;
		}
		public Locale getLocale(){
			if (locale!=null)
			{
				return locale;
			}
			else
			{
				return super.getLocale();
			}
		}
		
		private static Locale getloacle(String lstr) {
			if (lstr == null || lstr.length() < 1) {
				return null;
			}
			Locale locale=null;
			StringTokenizer localeTokens = new StringTokenizer(lstr, "_");
			String lang = null;
			String country = null;
			if (localeTokens.hasMoreTokens()) {
				lang = localeTokens.nextToken();
			}
			if (localeTokens.hasMoreTokens()) {
				country = localeTokens.nextToken();
			}
			locale = new Locale(lang, country);
			return locale;
		}
		
		private String getCookie(String name){
			if (name!=null){
				Cookie[] cookies = getCookies();
				if (cookies!=null){
					for (Cookie cookie:cookies){
						if (name.equals(cookie.getName())){
							return cookie.getValue();
						}
					}
				}
			}
			return null;
		}
		private Locale locale;
	}
	private String charset; 
}
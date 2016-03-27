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

/**
 * @author Tom
 */
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import net.duckling.cloudy.common.CommonUtils;

import org.apache.log4j.Logger;

public class XssFilter implements Filter {
	private static final Logger LOGGER = Logger
			.getLogger(XssHttpServletRequestWrapper.class);
	private class XssHttpServletRequestWrapper extends
			HttpServletRequestWrapper {
		public XssHttpServletRequestWrapper(HttpServletRequest request) {
			super(request);
		}

		/**
		 * 将容易引起xss漏洞的半角字符直接替换成全角字符
		 * 
		 * @param s
		 * @return
		 */
		private String xssEncode(String s) {
			if (s == null || s.equals("")) {
				return s;
			}

			for (int i = 0; i < filterChars.length; i++) {
				if (s.contains(filterChars[i])) {
					s = s.replace(filterChars[i], replaceChars[i]);
				}
			}
			return s;
		}

		/**
		 * 覆盖getHeader方法，将参数名和参数值都做xss过滤。<br/>
		 * 如果需要获得原始的值，则通过super.getHeaders(name)来获取<br/>
		 * getHeaderNames 也可能需要覆盖
		 */
		public String getHeader(String name) {

			String value = super.getHeader(xssEncode(name));
			if (value != null) {
				value = xssEncode(value);
			}
			return value;
		}

		/**
		 * 覆盖getParameter方法，将参数名和参数值都做xss过滤。<br/>
		 * 如果需要获得原始的值，则通过super.getParameterValues(name)来获取<br/>
		 * getParameterNames,getParameterValues和getParameterMap也可能需要覆盖
		 */
		public String getParameter(String name) {
			if (isNeverParameter(name)) {
				return super.getParameter(name);
			}
			if (isIgnoreparameter(name)) {
				String param = super.getParameter(name);
				if (param == null) {
					return null;
				}
				try {
					URI uri=new URI(param);
					return CommonUtils.trim(param);
				} catch (URISyntaxException e) {
					return null;
				}
			}
			String value = super.getParameter(xssEncode(name));

			if (value != null) {
				value = xssEncode(value);
			}
			return CommonUtils.trim(value);
		}
		private boolean isNeverParameter(String name) {
			if (name == null) {
				return false;
			}
			for (String ignoreInput : neverInputs) {
				if (name.equals(ignoreInput)) {
					return true;
				}
			}
			return false;

		}

		private boolean isIgnoreparameter(String name) {
			if (name == null) {
				return false;
			}
			for (String ignoreInput : ignoreInputs) {
				if (name.equals(ignoreInput)) {
					return true;
				}
			}
			return false;
		}

		public String[] getParameterValues(String name) {
			if (isNeverParameter(name)) {
				return super.getParameterValues(name);
			}
			if (isIgnoreparameter(name)) {
				String[] param = super.getParameterValues(name);
				if (param == null) {
					return null;
				}
				int index = 0;
				for (String pa : param) {
					try{
						URI uri=new URI(pa);
						param[index++]=pa;
					}catch(URISyntaxException e){
						param[index++] = "";
					}
					
				}
				return param;
			}
			String[] parameters = super.getParameterValues(name);
			if (parameters == null || parameters.length == 0) {
				return null;
			}
			for (int i = 0; i < parameters.length; i++) {
				parameters[i] = xssEncode(parameters[i]);
			}
			return parameters;
		}

		public String getQueryString() {
			String value = super.getQueryString();
			if (value != null) {
				value = xssEncode(value);
			}
			return value;
		}
	}

	private String[] filterChars;

	private String[] replaceChars;
	private String[] ignoreInputs = new String[] {};
	private String[] neverInputs = new String[] {};

	public void destroy() {
		// Do nothing
	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		XssHttpServletRequestWrapper xssRequest = new XssHttpServletRequestWrapper(
				(HttpServletRequest) request);
		chain.doFilter(xssRequest, response);
	}
	public void init(FilterConfig filterConfig) throws ServletException {
		String filterChar = filterConfig.getInitParameter("FilterChar");
		String replaceChar = filterConfig.getInitParameter("ReplaceChar");
		String splitChar = filterConfig.getInitParameter("SplitChar");
		String ignoreChar = filterConfig.getInitParameter("IgnoreInput");
		String xssConf = filterConfig.getInitParameter("XssConf");
		String neverChar = filterConfig.getInitParameter("NeverInput");
		if (filterChar != null && filterChar.length() > 0) {
			filterChars = filterChar.split(splitChar);
		}
		if (replaceChar != null && replaceChar.length() > 0) {
			replaceChars = replaceChar.split(splitChar);
		}
		if (ignoreChar != null && !ignoreChar.isEmpty()) {
			ignoreInputs = ignoreChar.split(splitChar);
		}
		if (neverChar != null && !neverChar.isEmpty()) {
			neverInputs = neverChar.split(splitChar);
		}

	}
}

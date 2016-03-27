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
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.ui.Attributes;

public class ReadonlyFilter implements Filter {
	
	private String[] ignoreList;
	private static final Logger LOGGER=Logger.getLogger(ReadonlyFilter.class);

	@Override
	public void destroy() {
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,FilterChain chain) throws IOException, ServletException {
		HttpServletRequest req=(HttpServletRequest) request;
		String path=req.getServletPath();

		LOGGER.debug(path);
		if (isReadonly(req)) {
			String recoveryTime=getRecoveryTime(req);
			request.setAttribute("recoveryTime", recoveryTime);
			request.setAttribute("date", new Date());
			request.setAttribute("isReadonly", true);
			if(!isIgnored(path)){
				req.getRequestDispatcher("/maintain.jsp").forward(request, response);
				return;
			}
		}else{
			request.setAttribute("isReadonly", false);
		}
		
		
		chain.doFilter(request, response);
	}

	@Override
	public void init(FilterConfig config) throws ServletException {
		String ignore = config.getInitParameter("ignore");
		if (ignore != null) {
			ignoreList = ignore.split(";");
		}
	}
	
	private boolean isIgnored(String servletPath) {
		if (ignoreList != null && ignoreList.length > 0) {
			for (String ignorePath : ignoreList) {
				if (StringUtils.startsWith(servletPath,ignorePath)){
					return true;
				}
			}
		}
		return false;
	}
	
	private boolean isReadonly(HttpServletRequest request){
		return getConfig(request).getBooleanProp("system.readonly", false);
	}
	
	private String getRecoveryTime(HttpServletRequest request){
		Config config=this.getConfig(request);
		return config.getStringProp("system.readonly.recovery.time", "");
	}
	
	public Config getConfig(HttpServletRequest request){
		BeanFactory beanFactory=(BeanFactory) request.getServletContext()
		.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		return (Config)beanFactory.getBean(Config.BEAN_ID);
	}

}

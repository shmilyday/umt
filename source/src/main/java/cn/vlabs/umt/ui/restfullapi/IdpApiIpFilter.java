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
package cn.vlabs.umt.ui.restfullapi;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.user.service.IAccessIPService;
import cn.vlabs.umt.ui.UMTContext;

import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;

public class IdpApiIpFilter implements Filter {
	
	private static final Logger LOG=Logger.getLogger(IdpApiIpFilter.class);
	private IAccessIPService ipService;
	
	public void destroy() {
	}
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		String ip=RequestUtil.getRemoteIP((HttpServletRequest)request);
		if(!ipService.canAccess_A(ip)){
			error(403,(HttpServletResponse)response,"connect refuse! who ip is:"+ip);
			return;
		}
		chain.doFilter(request, response);//Ignored
	}

	public void init(FilterConfig config) throws ServletException {
		BeanFactory factory = UMTContext.getFactory();
		this.ipService=(IAccessIPService)factory.getBean(IAccessIPService.BEAN_ID);
	}
	
	private void error(int statusCode,HttpServletResponse response,String msg) throws IOException{
		JSONObject jso=new JSONObject();
		try {
			jso.put("code", statusCode);
			jso.put("status", "fail");
			jso.put("stauts", "fail");
			jso.put("result", msg);
		} catch (JSONException e) {
			LOG.error("",e);
		}
		LOG.error(msg);
		response.getWriter().print(jso);
	}
	
}
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
package cn.vlabs.umt.ui.servlet;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.jasig.cas.client.authentication.AttributePrincipal;

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;

@SuppressWarnings("serial")
/**
 * 物理所成功登陆回调接口
 * */
public class GeoLoginServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(GeoLoginServlet.class);
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		AttributePrincipal principal=(AttributePrincipal) request.getUserPrincipal();
		
		if(principal==null){
			response.sendRedirect(RequestUtil.getContextPath(request)+"/index.jsp");
			return;
		}
		Map<String,Object> map=principal.getAttributes();
		String openId=(String) map.get("email");
		String trueName=(String) map.get("realName");
		
		UserService userService=ServiceFactory.getUserService(request);
		User umtUser=userService.getUserByOpenid(openId,BindInfo.TYPE_CAS_GEO,null);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_USER, trueName);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_EMAIL, openId);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_OPEN_ID, openId);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_TYPE, BindInfo.TYPE_CAS_GEO);
		if(umtUser==null){
			LOGGER.info("use openId ["+openId+"] bind account,_casGeo");
			response.sendRedirect(RequestUtil.getContextPath(request)+"/accountBind_createUmt.jsp");
		}else{
			response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type="+BindInfo.TYPE_CAS_GEO+"&act=Validate&authBy="+BindInfo.TYPE_CAS_GEO+getSiteInfoParam(request));
		}
		
		
	}
	private String getSiteInfoParam(HttpServletRequest request){
		StringBuffer result=new StringBuffer();
		Map<String,String> siteInfo=SessionUtils.getSiteInfo(request);
		if(siteInfo!=null){
			for (String param:Attributes.SSO_PARAMS){
				if (siteInfo.get(param)!=null){
					result.append("&").append(param).append("=").append(siteInfo.get(param));
				}
			}
		}
		return result.toString();
	}
}
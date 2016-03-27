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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.dacas.websso.LoginContext;
import cn.dacas.websso.Ticket;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;

@SuppressWarnings("serial")
/**
 * 院机关登陆回调接口
 * */
public class CashqSsoCallBackServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(CashqSsoCallBackServlet.class);
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Ticket ticket = LoginContext.getTicket(request);
		String openId = ticket.getSubject();
		UserService userService=ServiceFactory.getUserService(request);
		User umtUser=userService.getUserByOpenid(openId,BindInfo.TYPE_CASHQ_SSO,null);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_USER, ticket.getAttribute("name"));
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_OPEN_ID, openId);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_TYPE, BindInfo.TYPE_CASHQ_SSO);
		if(umtUser==null){
			LOGGER.info("use openId ["+openId+"] bind account,_cashqSso");
			response.sendRedirect(RequestUtil.getContextPath(request)+"/accountBind_createUmt.jsp");
		}else{
			response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type="+BindInfo.TYPE_CASHQ_SSO+"&act=Validate&authBy="+BindInfo.TYPE_CASHQ_SSO+getSiteInfoParam(request));
		}
			
	}
	private String getSiteInfoParam(HttpServletRequest request){
		String result="";
		Map<String,String> siteInfo=SessionUtils.getSiteInfo(request);
		if(siteInfo!=null){
			for (String param:Attributes.SSO_PARAMS){
				if (siteInfo.get(param)!=null){
					result+="&"+param+"="+siteInfo.get(param);
					if(Attributes.RETURN_URL.equals(param)){
						try {
							result+=URLEncoder.encode("&pageinfo=userinfo", "UTF-8");
						}catch (UnsupportedEncodingException e){
							LOGGER.error(e.getMessage(),e);
						}
					}
				}
			}
		}
		return result;
	}
}

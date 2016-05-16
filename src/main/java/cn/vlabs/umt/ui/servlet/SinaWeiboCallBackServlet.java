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

import weibo4j.Oauth;
import weibo4j.Users;
import weibo4j.http.AccessToken;
import weibo4j.model.User;
import weibo4j.model.WeiboException;
import cn.vlabs.umt.common.util.ReflectUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;

@SuppressWarnings("serial")
/**
 * 新浪成功登陆回调接口
 * */
public class SinaWeiboCallBackServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(SinaWeiboCallBackServlet.class);
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String code=request.getParameter("code");
		Oauth oauth=new Oauth();
		if(code==null){
			response.sendRedirect(RequestUtil.getContextPath(request)+"/index.jsp");
			return;
		}
		try {
			AccessToken token=oauth.getAccessTokenByCode(code);
			String openId=ReflectUtils.getValue(token, "uid").toString();
			Users um = new Users();
			um.client.setToken(token.getAccessToken());
			User user=um.showUserById(openId);
			UserService userService=ServiceFactory.getUserService(request);
			cn.vlabs.umt.services.user.bean.User umtUser=userService.getUserByOpenid(openId,BindInfo.TYPE_SINA,null);
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_USER, user.getScreenName());
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_ACCESS_TOKEN, token);
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_CODE, code);
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_OPEN_ID, openId);
			SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_TYPE, "weibo");
			if(umtUser==null){
				LOGGER.info("use code["+code+"] and get accesstoken!");
				response.sendRedirect(RequestUtil.getContextPath(request)+"/accountBind_createUmt.jsp");
			}else{
				response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type=weibo&act=Validate&authBy=weibo"+getSiteInfoParam(request));
			}
			
		} catch (WeiboException e) {
			LOGGER.error("access token can't get,because code["+code+"] is unkown or used!");
			LOGGER.error(e.getMessage(),e);
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
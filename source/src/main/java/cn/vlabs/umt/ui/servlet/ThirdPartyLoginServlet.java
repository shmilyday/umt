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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import weibo4j.Oauth;
import weibo4j.model.WeiboException;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.oauth.UMTOauthConnectException;
import cn.vlabs.umt.services.auth.IAuthService;
import cn.vlabs.umt.services.auth.ThirdPartyAuth;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.ui.Attributes;

import com.qq.connect.QQConnectException;

/**
 * 第三方登陆入口，比如新浪微博，扣扣等
 * */
@Controller
@RequestMapping("/thirdParty/login")
public class ThirdPartyLoginServlet {
	private static final Logger LOGGER = Logger.getLogger(ThirdPartyLoginServlet.class);
	private static final String TYPE_WEIBO="weibo";
	private static final String TYPE_QQ="qq";
	private static final String TYPE_WEIXIN="weixin";
	private static final String TYPE_CASHQ="cashq";
	private static final String TYPE_UAF="uaf";
	private static final String TYPE_GEO="geo";
	@Autowired
	private Config config;
	@Autowired
	private IAuthService authService;
	@RequestMapping
	public void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		Map<String,String> siteInfo=new HashMap<String,String>();
		for (String param:Attributes.SSO_PARAMS){
			if (request.getParameter(param)!=null){
				siteInfo.put(param, request.getParameter(param));
			}
		}
		if(!siteInfo.isEmpty()){
			SessionUtils.setSessionVar(request,Attributes.SITE_INFO, siteInfo);
		}
		String type=request.getParameter("type");
		switch(type){
			case TYPE_WEIBO:{
				doWeiboRequestToken(response);
				break;
			}
			case TYPE_QQ:{
				doQQRequestToken(request, response);
				break;
			}
			case TYPE_WEIXIN:{
				WeiXinUrils.doWeiXinRequestToken(request, response);
				break;
			}
			case TYPE_CASHQ:{
				doCasSsoRequest(request,response);
				break;
			}
			case TYPE_GEO:{
				doGEOCasSsoRequest(request,response);
				break;
			}
			case TYPE_UAF:
			{
				doUafRequest(request,response);
				break;
			}
			default:{
				ThirdPartyAuth auth = authService.find(type);
				if (auth!=null){
					try {
						response.sendRedirect(auth.createOauth().getAuthorizeURL(request));
						return;
					} catch (UMTOauthConnectException e) {
					}
				}
				LOGGER.error("third party login error: the type is ["+type+"]");
				break;
			}
		}
	}
	/**
	 * 统一登录平台
	 * */
	public void doUafRequest(HttpServletRequest request,HttpServletResponse response){
		try {
			response.sendRedirect(config.getStringProp("uaf.login.url", ""));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	/**
	 * 院机关工作平台登陆
	 * @param request
	 * @param response
	 */
	private void doCasSsoRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(RequestUtil.getContextPath(request)+"/cashq");
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	/**
	 * 物理所工作平台登陆
	 * @param request
	 * @param response
	 */
	private void doGEOCasSsoRequest(HttpServletRequest request, HttpServletResponse response) {
		try {
			response.sendRedirect(RequestUtil.getContextPath(request)+"/geo");
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	/**
	 * 向扣扣发出第三方登陆的请求，也就是取得requestToken
	 * @param request
	 * @param response
	 * */
	private void doQQRequestToken(HttpServletRequest request, HttpServletResponse response) {
		 try {
			String url=new com.qq.connect.oauth.Oauth().getAuthorizeURL(request);
			response.sendRedirect(url);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (QQConnectException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	/**
	 * 向新浪发出第三方登陆的请求，也就是取得requestToken
	 * @param request
	 * @param response
	 * */
	private void doWeiboRequestToken(HttpServletResponse response)
			throws ServletException, IOException{
		Oauth oauth = new Oauth();
        try {
			String url=oauth.authorize("code","","");
			response.sendRedirect(url);
		} catch (WeiboException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
}
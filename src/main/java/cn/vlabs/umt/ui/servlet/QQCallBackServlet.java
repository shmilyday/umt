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
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;

@SuppressWarnings("serial")
/**
 *扣扣登陆成功后回调接口
 * */
public class QQCallBackServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(QQCallBackServlet.class);
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {
		try {
			AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
			if (CommonUtils.isNull(accessTokenObj.getAccessToken())){
				// 我们的网站被CSRF攻击了或者用户取消了授权
				// 做一些数据统计工作
				response.sendRedirect(RequestUtil.getContextPath(request)+"/index.jsp");
				LOGGER.error("can't get userInfo...");
			} else {
				String accessToken=accessTokenObj.getAccessToken();
				String openID = new OpenID(accessToken).getUserOpenID();
				UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
				UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
				SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_ACCESS_TOKEN, accessToken);
				SessionUtils.setSessionVar(request,Attributes.THIRDPARTY_USER, userInfoBean.getNickname());
				SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_CODE, request.getParameter("code"));
				SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_OPEN_ID, openID);
				SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_TYPE, BindInfo.TYPE_QQ);
				UserService userService=ServiceFactory.getUserService(request);
				cn.vlabs.umt.services.user.bean.User umtUser=userService.getUserByOpenid(openID,BindInfo.TYPE_QQ,null);
				if(umtUser==null){
					response.sendRedirect(RequestUtil.getContextPath(request)+"/bind.do?act=createAndBindQQUmt&type="+BindInfo.TYPE_QQ+"&screenName="+URLEncoder.encode(userInfoBean.getNickname(),"utf8")+"&openId="+openID);
				}else{
					response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type=qq&act=Validate&authBy=qq"+getSiteInfoParam(request));
				}
			}
		} catch (QQConnectException e) {
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
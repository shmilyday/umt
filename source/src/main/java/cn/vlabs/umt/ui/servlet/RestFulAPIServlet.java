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
import java.text.SimpleDateFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.cloudy.common.CommonUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.OauthToken;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.service.IAccessIPService;
import cn.vlabs.umt.services.user.service.IOauthTokenService;
import cn.vlabs.umt.ui.UMTContext;

import com.qq.connect.utils.json.JSONException;
import com.qq.connect.utils.json.JSONObject;

public class RestFulAPIServlet  extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6346965378988395684L;
	private static final Logger LOG=Logger.getLogger(RestFulAPIServlet.class);
	private IAccessIPService ipService;
	private UserService umtService;
	private IOauthTokenService tokenService;
	@Override
	public void init() throws ServletException {
		BeanFactory factory = UMTContext.getFactory();
		this.ipService=(IAccessIPService)factory.getBean(IAccessIPService.BEAN_ID);
		this.umtService=(UserService)factory.getBean(UserService.BEAN_ID);
		this.tokenService=(IOauthTokenService)factory.getBean(IOauthTokenService.BEAN_ID);
	}
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String act=CommonUtils.trim(request.getParameter("act"));
		String ip=RequestUtil.getRemoteIP(request);
		if(!ipService.canAccessScope_B(ip)){
			error(403,response,"connect refuse! who ip is:"+ip);
			return;
		}
		if(act==null){
			error(403,response,"param missing!![act]");
			return;
		}
		LOG.info("ip:"+ip+",use RestAPI."+act);
		switch(act){
		case "getLastLoginTime":{
			getLastLoginTime(request, response);
			return;
		}
		default:{
			error(403,response,"not found act named["+act+"]");
			return;
		}
		}
	}
	protected void getLastLoginTime(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		String cstnetId=CommonUtils.trim(request.getParameter("cstnetId"));
		if(CommonUtils.isNull(cstnetId)){
			error(403,response,"param missing! [cstnetId]");
			return;
		}
		User u=umtService.getUserByLoginName(cstnetId);
		if(u==null){
			error(404,response,"user not found["+cstnetId+"]");
			return;
		}
		String clientId="73969";
		OauthToken oauthToken=tokenService.getLastTokenByUidAndClientId(u.getId(), clientId);
		JSONObject jso=new JSONObject();
		try {
			jso.put("statusCode", 200);
			jso.put("act", "getLastLoginTime");
			if(oauthToken==null){
				jso.put("lastLoginTime", "");
			}else{
				jso.put("lastLoginTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(oauthToken.getCreateTime()));
			}
		} catch (JSONException e) {
			LOG.error("",e);
		}
		response.getWriter().println(jso.toString());
		
	}
	private void error(int statusCode,HttpServletResponse response,String msg) throws IOException{
		JSONObject jso=new JSONObject();
		try {
			jso.put("statusCode", statusCode);
			jso.put("desc", msg);
			
		} catch (JSONException e) {
			LOG.error("",e);
		}
		LOG.error(msg);
		response.getWriter().print(jso);
	}
}

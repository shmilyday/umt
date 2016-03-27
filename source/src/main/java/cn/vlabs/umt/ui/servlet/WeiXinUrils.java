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
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import cn.vlabs.duckling.ca.HttpClientFactory;
import cn.vlabs.umt.common.ReturnURLUtils;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.ui.Attributes;

public class WeiXinUrils {
	
	private static final String APPID = "wx3c148e35ae421b4e";
	private static final String APPSECRET = "5af696bf9266c9619fd49d96006a9b70";
	private static final String CALLBACK_URL = "/thirdParty/callback/weixin";
	private static final String GET_CODE_WEIXIN="https://open.weixin.qq.com/connect/oauth2/authorize?appid=${appid}&redirect_uri=${redirectUrl}&response_type=code&scope=snsapi_base#wechat_redirect";
	private static final String CODE_2_ACCESSTOKEN="https://api.weixin.qq.com/sns/oauth2/access_token?appid=${appid}&secret=${secret}&code=${code}&grant_type=authorization_code";
	
	private static final Logger LOGGER = Logger.getLogger(WeiXinUrils.class); 

	public static void doWeiXinRequestToken(HttpServletRequest request, HttpServletResponse response) {
		String baseUrl=RequestUtil.getBaseURL(request);
		String redirectUrl=baseUrl+CALLBACK_URL;
		 try {
			String url=getWeixinCodeUrl(URLEncoder.encode(redirectUrl,"utf-8"));
			response.sendRedirect(url);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
	}
	
	private static String getWeixinCodeUrl(String redirectUrl){
		String result=StringUtils.replace(GET_CODE_WEIXIN, "${appid}", APPID);
		result=StringUtils.replace(result, "${redirectUrl}", redirectUrl);
		return result;
	}
	
	public static WeiXinAccessToken getAccessToken(HttpServletRequest request) throws IOException, JSONException{
		String code=request.getParameter("code");
		return getAccessToken(code);
	}
	
	
	private static WeiXinAccessToken getAccessToken(String code) throws IOException, JSONException{
		String url = StringUtils.replace(CODE_2_ACCESSTOKEN,"${appid}", APPID);
		url = StringUtils.replace(url,"${secret}", APPSECRET);
		url = StringUtils.replace(url,"${code}", code);
		JSONObject temp= doGetStr(url);
		WeiXinAccessToken result=new WeiXinAccessToken();
		result.setAccessToken(temp.getString("access_token"));
		result.setExpiresIN(temp.getInt("expires_in"));
		result.setRefresh_token(temp.getString("refresh_token"));
		result.setOpenid(temp.getString("openid"));
		result.setScope(temp.getString("scope"));
		return result;
	}
	
	public static JSONObject doGetStr(String url) throws  IOException, JSONException{
		HttpClient client = HttpClientFactory.getHttpClient();
		HttpGet httpGet = new HttpGet(url);
		JSONObject jsonObject = null;
		HttpResponse httpResponse = client.execute(httpGet);
		HttpEntity entity = httpResponse.getEntity();
		if(entity != null){
			String result = EntityUtils.toString(entity,"UTF-8");
			jsonObject = new JSONObject(result);
		}
		httpGet.releaseConnection();
		return jsonObject;
	}
	
	
	
}

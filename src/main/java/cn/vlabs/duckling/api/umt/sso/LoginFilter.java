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
package cn.vlabs.duckling.api.umt.sso;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import cn.vlabs.duckling.common.util.ClassUtil;

/**
 * Introduction Here.
 * @date 2010-6-29
 * @author Fred Zhang (fred@cnic.cn)
 */
public class LoginFilter implements Filter{
	private static final Logger LOG =Logger.getLogger(LoginFilter.class);
	private ArrayList<Pattern> ignoreList;
	public void destroy() {
		// TODO Auto-generated method stub
		
	}
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest pRequest = (HttpServletRequest)request;
		HttpServletResponse pResponse = (HttpServletResponse)response;
		UserContext userContext = SessionUtil.getUserContext(pRequest);
		if(userContext == null&&needLoginRequest(pRequest))
		{
			Map<String,String> params = new HashMap<String,String>();
			String loginHandClass = SSOProperties.getInstance().getProperty(ILoginHandle.UMT_LOGIN_EXTHANDLE_CLASS);
			Object object = null;
			if(loginHandClass!=null)
			{
				object = ClassUtil.classInstance(loginHandClass);
				if(object!=null)
				{
					((ILoginHandle)object).initBeforLogin((HttpServletRequest)request, (HttpServletResponse)response,params);
				}
			}
			String uri = pRequest.getRequestURL().toString();
			String fullRequest = uri;
			String queryString = pRequest.getQueryString();
			if (queryString!=null)
			{
				fullRequest=fullRequest+"?"+queryString;
			}
			SessionUtil.setUserRedirectUrl(pRequest, fullRequest);
			pResponse.sendRedirect(makeUmtLoginURL(pRequest,params));
		}else
		{
			chain.doFilter(request, response);
			return;
		}
	}
	private boolean needLoginRequest(HttpServletRequest request)
	{
		String uri = request.getRequestURI();
		String contexPath=request.getContextPath();
		if (contexPath!=null && contexPath.length()>0){
			uri = uri.substring(contexPath.length());
		}
		for (Pattern p:ignoreList){
			if (p.matcher(uri).matches()){
				return false;
			}
		}
		return true;
	}
	private String extract(String url){
		int index = url.indexOf('/', 7);
		if (index==-1)
			return url;
		else
			return url.substring(0, index);
	}
	private String makeUmtLoginURL(HttpServletRequest request,Map<String,String> extParamsToUmt)
	{
		String localURL = extract(request.getRequestURL().toString());
		localURL = localURL +request.getContextPath() +"/login";
		String url = null;
		String defaultLogoutUrl = "http://"+request.getLocalAddr();
		if (request.getLocalPort()!=80){
			defaultLogoutUrl =defaultLogoutUrl+":"+request.getLocalPort();
		}
		defaultLogoutUrl = defaultLogoutUrl+request.getContextPath()+"/logout";
		StringBuffer extparams = new StringBuffer();
		try {
		for(Entry<String,String> entry:extParamsToUmt.entrySet()){
			extparams.append("&").append(URLEncoder.encode(entry.getKey(),"UTF-8")).append("=").append(URLEncoder.encode(extParamsToUmt.get(entry.getKey()), "UTF-8"));
		}
		String logoutUrl = SSOProperties.getInstance().getProperty(ILoginHandle.LOCALAPP_LOGOUT_URL,defaultLogoutUrl);
		if(logoutUrl.indexOf("?")>0)
		{
			logoutUrl = logoutUrl+"&umtSsoLogout=true";
		}else
		{
			logoutUrl = logoutUrl+"?umtSsoLogout=true";
		}
		url = getUMTLoginURL() 
		+ "?appname="
		+ URLEncoder.encode(SSOProperties.getInstance().getProperty(ILoginHandle.UMT_AUTH_APPNAME_KEY), "UTF-8") + "&WebServerURL="
		+ URLEncoder.encode(localURL, "UTF-8")
		+"&sid="+request.getSession(true).getId()
		+"&logoutURL="+URLEncoder.encode(logoutUrl, "UTF-8")
		+extparams.toString();
		} catch (UnsupportedEncodingException e) {
		}
		return url; 
	}
	private String getUMTLoginURL()
	{
		String umtLoginUrl = SSOProperties.getInstance().getProperty(ILoginHandle.UMT_LOGIN_URL_KEY);
		umtLoginUrl = umtLoginUrl.trim();
		if (umtLoginUrl.endsWith("/")) {
			umtLoginUrl = umtLoginUrl.substring(0, umtLoginUrl.length() - 1);
		}
       return umtLoginUrl;
	}
	private void initRules(ServletContext context) {
		ignoreList = new ArrayList<Pattern>();
		String file = SSOProperties.getInstance().getProperty(ILoginHandle.UMT_AUTH_RULE_KEY);
		file = context.getRealPath(file);
		BufferedReader reader=null;
		InputStreamReader isr=null;
		FileInputStream fis=null;
		try {
			fis=new FileInputStream(file);
			isr=new InputStreamReader(fis, "UTF-8");
			reader=new BufferedReader(isr);
			String line = null;
			while ((line=reader.readLine())!=null){
				line=line.trim();
				if (line.length()>0 && !line.startsWith("#")){//A pattern line
					line = line.replaceAll("/", "\\/");
					line = line.replaceAll("\\*", ".*");
					Pattern p = Pattern.compile(line);
					ignoreList.add(p);
				}
			}
		} catch (Exception e) {
			LOG.error("初始化登录过滤器时失败:"+e.getMessage());
		}finally{
			try{
			if(fis!=null){fis.close();}
			if(isr!=null){isr.close();}
			if(reader!=null){reader.close();}
			}catch(IOException e){
				LOG.error(e.getMessage(),e);
			}
		}
	}
	public void init(FilterConfig filterConfig) throws ServletException {
		String file = filterConfig.getInitParameter("config");
		file = filterConfig.getServletContext().getRealPath(file);
		SSOProperties.getInstance().initProperties(file, true);
		initRules(filterConfig.getServletContext());
	}

}
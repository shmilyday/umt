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
package cn.vlabs.umt.common;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.ui.servlet.LogoutServlet;

public class ReturnURLUtils {
	private static IOauthClientService clientService;
	private static Config config;
	private static IOauthClientService getClientService(){
		if(clientService==null){
			clientService=(IOauthClientService)UMTContext.getFactory().getBean("oauthClientService");
		}
		return clientService;
	}
	private static String getMyBaseUrl(){
		if(config==null){
			config=(Config)UMTContext.getFactory().getBean("Config");
		}
		return config.getStringProp("umt.this.base.url", null);
	}
	static class ReturnURLErrorException extends Exception{
		/**
		 * 
		 */
		private static final long serialVersionUID = 625015965301737798L;
		public ReturnURLErrorException(String msg) {
			super(msg);
		}
		public ReturnURLErrorException(Exception e){
			super(e);
		}
	}
	public static String getBaseUrl(String returnUrl) throws ReturnURLErrorException{
		String urlStr="";
		if(returnUrl==null){
			return urlStr;
		}
		returnUrl=CommonUtils.trim(returnUrl);
		try {
			URI uri=new URI(returnUrl);
		} catch (URISyntaxException e) {
			throw new ReturnURLErrorException(e);
		}
		if(returnUrl.contains("://")){
			if(!returnUrl.startsWith("http")&&!returnUrl.startsWith("https")){
				throw new ReturnURLErrorException("url:"+returnUrl+",is error");
			}
			urlStr=returnUrl.substring(returnUrl.indexOf("://")+3);
			if(urlStr.contains("/")){
				urlStr=urlStr.substring(0,urlStr.indexOf("/"));
			}
		}else{
			urlStr=returnUrl;
		}
		return CommonUtils.trim(urlStr);
	}
	public static String checkUrl(String returnUrl){
		if(CommonUtils.isNull(returnUrl)){
			return null;
		}
		List<String> array=getClientService().getAllCallBack();
		try {
			array.add(getBaseUrl(getMyBaseUrl()));
			String returnUrlBase=getBaseUrl(returnUrl);
			for(String callback:array){
				String callbackBase=getBaseUrl(callback);
				if(callbackBase.equals(returnUrlBase)){
					return returnUrl;
				}
			}
			LOG.error("dangerous returnUrl ["+returnUrl+"]");
			return null;
		} catch (ReturnURLErrorException e) {
			LOG.error(e.getMessage(),e);
			return null;
		}
		
	}
	private static final Logger LOG=Logger.getLogger(LogoutServlet.class);
	
}

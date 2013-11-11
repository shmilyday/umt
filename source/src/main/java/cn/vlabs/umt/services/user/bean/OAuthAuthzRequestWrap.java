/*
 * Copyright (c) 2008-2013 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
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
package cn.vlabs.umt.services.user.bean;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import cn.vlabs.umt.oauth.as.request.OAuthAuthzRequest;

public class OAuthAuthzRequestWrap {
	private String clientId;
	private Set<String> scopes;
	private String clientSecret;
	private Map<String,String[]> parameters;
	private String state;
	private String redirectURI;
	private String responseType;
	private String theme;
	/**
	 * 从自定义的URL中获取数据
	 * @param http
	 */
	public OAuthAuthzRequestWrap(HttpServletRequest request){
		this.clientId = request.getParameter("client_id");
		this.clientSecret = request.getParameter("client_secret");
		this.state = request.getParameter("state");
		this.scopes = getSopeSet(request.getParameter("scope"));
		this.redirectURI = request.getParameter("redirect_uri");
		this.responseType = request.getParameter("response_type");
		this.theme = request.getParameter("theme");
		this.parameters = dealParameter(request);
	} 
	
	public OAuthAuthzRequestWrap(OAuthAuthzRequest request,HttpServletRequest http){
		this.parameters =dealParameter(http);
		this.clientId = request.getClientId();
		this.scopes = request.getScopes();
		this.clientSecret = request.getClientSecret();
		this.state = request.getState();
		this.redirectURI = request.getRedirectURI();
		this.responseType = request.getResponseType();
		this.theme = http.getParameter("theme");
	}
	
	private Map<String, String[]> dealParameter(HttpServletRequest request) {
		Map<String,String[]> result = new HashMap<String,String[]>();
		Map<String,String[]> maps = request.getParameterMap();
		if(maps==null||maps.isEmpty()){
			return null;
		}
		for(Entry<String,String[]> entry : maps.entrySet()){
			String key = entry.getKey();
			if(key!=null&&key.startsWith("oauthparam.")){
				result.put(key.substring(11), entry.getValue());
			}
		}
		return result;
	}

	private Set<String> getSopeSet(String parameter) {
		Set<String> scope = new HashSet<String>();
		if(parameter==null||parameter.length()==0){
			return scope;
		}
		String[] ss = parameter.split(",");
		for(String s: ss){
			scope.add(s);
		}
		return scope;
	}
	
	public String getTheme() {
		return theme;
	}

	public String getClientId() {
		return clientId;
	}
	public String getClientSecret() {
		return clientSecret;
	}
	public String getParam(String name) {
		String[] re = parameters.get(name);
		
		if(re==null||re.length==0){
			return null;
		}else{
			return re[0];
		}
	}
	public String getState() {
		return state;
	}
	public String getRedirectURI() {
		return redirectURI;
	}
	
	public String getResponseType() {
		return responseType;
	}
	public Set<String> getScopes() {
		return scopes;
	}
	
	public String getUrl() throws UnsupportedEncodingException{
		StringBuilder sb = new StringBuilder();
		appandString("client_id", clientId, sb);
		appandString("redirect_uri", redirectURI, sb);
		appandString("response_type", responseType, sb);
		appandString("scope", scopeToString(), sb);
		appandString("state", state, sb);
		appandString("client_secret", clientSecret, sb);
		appandString("theme", theme, sb);
		for(Entry<String,String[]> pa :parameters.entrySet()){
			String [] value = pa.getValue();
			if(value!=null&&value.length>0){
				appandString("oauthparam."+pa.getKey(), value[0], sb);
			}
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	private String scopeToString(){
		if(scopes==null||scopes.isEmpty()){
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for(String s : scopes){
			sb.append(s).append(",");
		}
		if(sb.length()>0){
			sb.deleteCharAt(sb.length()-1);
		}
		return sb.toString();
	}
	
	private void appandString(String key,String value,StringBuilder sb) throws UnsupportedEncodingException{
		String enc="utf-8";
		if(value!=null&&value.length()>0){
			sb.append(URLEncoder.encode(key,enc)).append("=").append(URLEncoder.encode(value, enc)).append("&");
		}
	}
	
	
}
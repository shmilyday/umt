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
/**
 * 
 */
package cn.vlabs.umt.domain;

import java.util.Date;

import net.duckling.cloudy.common.CommonUtils;
import net.duckling.cloudy.common.DateUtils;

import org.json.simple.JSONObject;

/**
 * 不可变对象的写法
 * @author lvly
 * @since 2013-11-14
 */
public class OauthLog {
	/**
	 * Oauth的app key,冗余字段，不是用来联查的
	 * */
	private  String clientId;
	/**
	 * Oauth的app name，冗余字段，不更新了，麻烦
	 */
	private  String clientName;
	/**
	 * 发生时间，不要更新和赋值
	 * */
	private  Date occurTime=new Date();
	/**
	 * 日志分类两种，一个是针对oauth参数的，一个是oauth参数->uid的，如果是前者，则没有必要插入uid
	 * */
	private  int uid;
	/**
	 * 操作成功与否，success代表成功，异常状态码
	 * */
	private  String result;
	
	/**
	 * 邮箱
	 * */
	private String cstnetId;
	public static final String RESULT_SUCCESS="success";
	public static final String RESULT_REDIRECT_URL_ERROR="redirectURL.error";
	public static final String RESULT_REDIRECT_URL_MISMATCH="redirectURL.mismatch";
	public static final String RESULT_CLIENT_ID_ERROR="client.id.error";
	public static final String RESULT_VALIDATE_USER_ERROR="username.or.pwd.error";
	public static final String RESULT_USER_NOT_FOUND="user.not.found";
	public static final String RESULT_SECRET_ERROR="secret.error";
	public static final String RESULT_TOKEN_FORMAT_ERROR="token.format.error";
	public static final String RESULT_TOKEN_NOT_FOUND="token.not.found";
	public static final String RESULT_TOKEN_EXPIRED="token.expired";
	public static final String RESULT_CODE_NOT_FOUND="code.not.found";
	public static final String RESULT_CODE_EXPIRED="code.expired";
	public static final String RESULT_SCOPE_MISMATCH="scope.mismatch";
	public static final String RESULT_REFRESH_TOKEN_REQUIRED="refresh.token.required";
	public static final String RESULT_REFRESH_TOKEN_EXPIRED="refresh.token.expired";
	public String toString(){
		JSONObject obj=new JSONObject();
		obj.put("action", this.action);
		obj.put("clientId",this.clientId);
		obj.put("clientName", this.clientName);
		obj.put("desc", this.desc);
		obj.put("ip", this.ip);
		obj.put("occurTime", DateUtils.getDateStr(occurTime));
		obj.put("result",this.result);
		obj.put("uid", this.uid);
		obj.put("cstnetId", this.cstnetId);
		obj.put("userAgent", this.userAgent);
		if(!CommonUtils.isNull(this.cstnetId)){
			String[] split= this.cstnetId.split("@");
			if(split.length==2){
				obj.put("domain",split[1]);
			}else{
				obj.put("domain", this.cstnetId);
			}
			
		}
		return obj.toString();
	}
	private String userAgent;
	
	
	public String getUserAgent() {
		return userAgent;
	}
	public void setUserAgent(String userAgent) {
		this.userAgent = userAgent;
	}
	public String getCstnetId() {
		return cstnetId;
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}

	/**
	 * 描述信息，可为空，
	 * */
	private  String desc;
	/**
	 * oauth的动作，对应着AuthorizationCodeServlet和OauthTokenServlet的两种方法
	 * */
	private  String action;
	public static final String ACTION_VALIDATE_CLIENT="validate.client";
	public static final String ACTION_VALIDATE_USERINFO="validate.userinfo";
	public static final String ACTION_CHECK_PASSWORD="validate.check.password";
	/**
	 * 手机上的用法，返回url#accesstoken=xxx&userinfo=xxx;
	 * */
	public static final String ACTION_VALIDATE_USERINFO_TOKEN="validate.userinfo.token";
	public static final String ACTION_VALIDATE_ACCESS_TOKEN="validate.accessToken";
	public static final String ACTION_VALIDATE_REFRESH_ACCESS_TOKEN="validate.refresh.accessToken";
	public static final String ACTION_VALIDATE_CODE="validate.code";
	/**
	 * Dchat上的用法
	 * */
	public static final String ACTION_VALIDATE_TOKEN_LOGIN="validate.token.login";
	public static final String ACTION_VALIDATE_TOKEN_LOGIN_RANDOM_TOKEN="validate.token.get.tmprandom";
	/**
	 * ip地址
	 * */
	private  String ip;
	public String getClientId() {
		return clientId;
	}
	public String getClientName() {
		return clientName;
	}
	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public void setClientName(String clientName) {
		this.clientName = clientName;
	}
	public void setOccurTime(Date occurTime) {
		this.occurTime = occurTime;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public static void main(String[] args) {
		Date date=new Date();
		date.setTime(1397105328181l);
		System.out.println(date);
	}
	public void setResult(String result) {
		this.result = result;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public void setAssertDesc(String expect,String desc){
		this.desc="{\"expect\":\""+expect+"\",\"actual\":\""+desc+"\"}";
	}
	public void setAction(String action) {
		this.action = action;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public Date getOccurTime() {
		return occurTime;
	}
	public int getUid() {
		return uid;
	}
	public String getResult() {
		return result;
	}
	public String getDesc() {
		return desc;
	}
	public String getAction() {
		return action;
	}
	public String getIp() {
		return ip;
	}

}

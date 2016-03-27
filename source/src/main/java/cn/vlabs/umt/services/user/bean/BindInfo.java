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
package cn.vlabs.umt.services.user.bean;

/**
 * 绑定信息
 * @author lvly
 * @since 2013-2-25
 */
public class BindInfo {
	public static final String SUBFIX="@passport.escience.cn";
	public static final String LIKE_EMAIL="tempemail"+SUBFIX;
	public static final String TYPE_QQ="qq";
	public static final String TYPE_WEIXIN="weixin";
	public static final String TYPE_SINA="weibo";
	public static final String TYPE_CASHQ_SSO="cashq";
	public static final String TYPE_CAS_GEO="geo";
	public static final String TYPE_UAF="uaf";
	private int id;
	private int uid;
	private String type;
	private String trueName;
	private String openId;
	private String url;
	private String typeName;
	public BindInfo(){
		
	}
	public static boolean isSupportedThirdParty(String type){
		return TYPE_QQ.equals(type)||TYPE_SINA.equals(type)||TYPE_CASHQ_SSO.equals(type)||TYPE_CASHQ_SSO.equals(type)||TYPE_CAS_GEO.equals(type);
	}
	public BindInfo(int uid, String screenName, String openId, String type){
		this.uid=uid;
		this.trueName=screenName;
		this.openId=openId;
		this.type=type;
	}
	public BindInfo(int uid, String screenName, String openId, String type,String url ){
		this(uid,screenName,openId,type);
		this.url=url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTrueName() {
		return trueName;
	}
	public void setTrueName(String screenName) {
		this.trueName = screenName;
	}
	public String getOpenId() {
		return openId;
	}
	public void setOpenId(String openId) {
		this.openId = openId;
	}
	public static String getDummyEmail(String umtId){
		return umtId+SUBFIX;
	}
	/**
	 * 是否是umt承认的第三方登陆
	 * @param type
	 * @return
	 */
	public static boolean isThirdParty(String type) {
		return BindInfo.TYPE_QQ.equals(type)||BindInfo.TYPE_SINA.equals(type)||BindInfo.TYPE_CASHQ_SSO.equals(type)||BindInfo.TYPE_UAF.equals(type)||BindInfo.TYPE_CAS_GEO.equals(type)||BindInfo.TYPE_WEIXIN.equals(type);
	}
	public static boolean isBuildinThirdParty(String type){
		return BindInfo.TYPE_QQ.equalsIgnoreCase(type)||BindInfo.TYPE_SINA.equalsIgnoreCase(type)||BindInfo.TYPE_CASHQ_SSO.equalsIgnoreCase(type)||BindInfo.TYPE_CAS_GEO.equalsIgnoreCase(type)||BindInfo.TYPE_UAF.equalsIgnoreCase(type)||BindInfo.TYPE_WEIXIN.equalsIgnoreCase(type);
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
}
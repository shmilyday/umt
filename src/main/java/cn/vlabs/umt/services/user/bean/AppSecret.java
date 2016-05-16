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

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

import cn.vlabs.umt.services.user.service.ITransform;
import cn.vlabs.umt.services.user.service.impl.EncryptorTransform;
import cn.vlabs.umt.services.user.service.impl.NTHashTransform;

public class AppSecret {
	public static final String HAHS_ALOG_NT = "nthash";
	public static final String HASH_ALOG_SHA = "sha";
	public static final String SECRET_TYPE_LDAP = "ldap";
	public static final String SECRET_TYPE_OAUTH = "oauth";
	public static final String SECRET_TYPE_WIFI = "wifi";
	public static final String USER_STATUS_ACCEPT = "accept";
	public static final String USER_STATUS_APPLY = "apply";
	private String appId;
	private HashMap<String, String> hashedSecret = new HashMap<String, String>();
	private int id;
	/**
	 * 用户输入的原始密码，不会存入数据库
	 */
	private String rawPassword = null;
	/**
	 * 旧的Secret，以前存放原始密码和SHA Hash，为了避免误解，将原始密码放到新的字段rawPassword中
	 */
	private String secret;
	/**
	 * 密码类型，可选值有LDAP和OAUTH
	 */
	private String secretType;
	/**
	 * 用户的内部编号
	 */
	private int uid;
	/**
	 * 用户名，邮件格式的
	 */
	private String userCstnetId;
	/**
	 * 用户在LDAP中的用户名
	 */
	private String userLdapName;
	private String userName;
	/**
	 * 用户状态，可能的选项由申请状态和接受状态
	 */
	private String userStatus;

	private void updateHash() {
		// TODO 暂时直接调用对应的Hash算法,以后调整代码位置.
		EncryptorTransform sha = new EncryptorTransform();
		sha.setAlgorithm(ITransform.TYPE_SHA);
		secret = sha.transform(rawPassword);
		hashedSecret.put(ITransform.TYPE_SHA, secret);
		NTHashTransform ntHash = new NTHashTransform();
		hashedSecret
				.put(ITransform.TYPE_NT_HASH, ntHash.transform(rawPassword));
	}

	public String getAllHashedSecret() {
		StringBuffer buffer = new StringBuffer();
		for (String key : hashedSecret.keySet()) {
			buffer.append(String.format("%s:%s;", key, hashedSecret.get(key)));
		}
		return buffer.toString();
	}

	public String getAppId() {
		return appId;
	}

	public String getHashedSecret(String alogrithm) {
		return hashedSecret.get(alogrithm);
	}

	public int getId() {
		return id;
	}

	public String getRawPassword() {
		return rawPassword;
	}

	public String getSecret() {
		return secret;
	}

	public String getSecretType() {
		return secretType;
	}

	public int getUid() {
		return uid;
	}

	public String getUserCstnetId() {
		return userCstnetId;
	}

	public String getUserLdapName() {
		return userLdapName;
	}

	public String getUserName() {
		return userName;
	}

	public String getUserStatus() {
		return userStatus;
	}

	public boolean isLDAPSecret() {
		return SECRET_TYPE_LDAP.equals(secretType) || SECRET_TYPE_WIFI.equals(secretType);
	}

	public boolean isOAuthSecret() {
		return SECRET_TYPE_OAUTH.equals(secretType);
	}

	public boolean isUserAccept() {
		return USER_STATUS_ACCEPT.equals(userStatus);
	}

	public boolean isUserApply() {
		return USER_STATUS_APPLY.equals(userStatus);
	}

	public void parse(String encodedString) {
		if (encodedString != null) {
			String[] secretPair = encodedString.split(";");
			for (String pair : secretPair) {
				if (pair != null) {
					pair = pair.trim();
					String[] keyValue = pair.split(":");
					hashedSecret.put(keyValue[0], keyValue[1]);
				}
			}
		}
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setRawPassword(String rawPassword) {
		this.rawPassword = rawPassword;
		updateHash();
	}

	public void setSecret(String secret) {
		this.secret = secret;
		this.hashedSecret.put(ITransform.TYPE_SHA, secret);
	}

	public void setSecretType(String secretType) {
		this.secretType = secretType;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public void setUserCstnetId(String userCstnetId) {
		this.userCstnetId = userCstnetId;
	}

	public void setUserLdapName(String userLdapName) {
		this.userLdapName = userLdapName;
	}

	public void setUserName(String userName) {
		if (StringUtils.isEmpty(userName)){
			this.userName=" ";
		}else{
			this.userName = userName;
		}
	}

	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}

}

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
package cn.vlabs.umt.common.mail;

public class EmailTemplate {
	
	public static final String SIGN_TITLE = "title=";
	public static final String SIGN_CONTENT = "content=";
	
	public static final String TARGET_APPROVE = "approveuser.html";
	public static final String TARGET_APPROVE_WAITING = "approvewaiting.html";
	public static final String TARGET_DENY = "denyuser.html";
	public static final String ACTIVATION_LOGIN_EMAIL="activation_login_email.html";
	public static final String CONFIRM_CHANGE_PRIMARY_EMAIL="confirm_change_primary_email.html";
	public static final String ACTIVATION_SECONDARY_EMAIL="activation_secondary_email.html";
	public static final String ACTIVATION_SECURITY_EMAIL="activation_security_email.html";
	public static final String MERGE_USER="merge_user.html";
	public static final String TARGET_REGISTER = "registuser.html";
	public static final String TARGET_PASSWORD = "remindpass.html";
	public static final String NOTICE_ADMIN_OAUTH_ADD="oauthAdd.html";
	public static final String NOTICE_ADMIN_LDAP_ADD="ldapAdd.html";
	public static final String NOTICE_ADMIN_WIFI_ADD="wifiAdd.html";
	public static final String NOTICE_ADMIN_OAUTH_UPDATE="oauthUpdate.html";
	public static final String NOTICE_USER_OAUTH_UPDATE="adminUpdateOauth.html";
	public static final String NOTICE_USER_LDAP_UPDATE="adminUpdateLdap.html";
	
	public static final String NOTICE_LDAP_ADD_APPLY_MEMBER="addLdapMember.html";
	public static final String NOTICE_LDAP_HAS_ADD_MEMBER="hasAddLdapMember.html";
	public static final String NOTICE_LDAP_DELETE_MEMBER="deleteLdapMember.html";
	public static final String NOTICE_WIFI_DELETE_MEMBER="deleteWifiMember.html";
	public static final String NOTICE_LDAP_PASS_MEMBER="passLdapMember.html";
	public static final String NOTICE_WIFI_PASS_MEMBER="passWifiMember.html";
	public static final String CONFIG_SMTP="mail.host";
	public static final String CONFIG_EMAIL="mail.username";
	public static final String CONFIG_PASSWORD="mail.password";
	public static final String TEMPLATE_DIR = "mail.template.dir";
	
	public static final String NOTICE_DIFF_REGISTER="diffrentRegister.html";

	private String title;
	private String content;
	private String target;

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
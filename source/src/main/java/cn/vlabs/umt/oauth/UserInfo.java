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
package cn.vlabs.umt.oauth;

public class UserInfo {
	private String trueName;
	private String type;
	private String cstnetId;
	private String umtId;
	private String securityEmail;
	private String cstnetIdStatus;
	private String passwordType;
	private String[] secondaryEmails;

	public String getTrueName() {
		return trueName;
	}
	
	public String getPasswordType() {
		return passwordType;
	}

	public void setPasswordType(String passwordType) {
		this.passwordType = passwordType;
	}

	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}
	public String getCstnetId() {
		return cstnetId;
	}
	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}
	public String getSecurityEmail() {
		return securityEmail;
	}
	public void setSecurityEmail(String securityEmail) {
		this.securityEmail = securityEmail;
	}
	public String getCstnetIdStatus() {
		return cstnetIdStatus;
	}
	public void setCstnetIdStatus(String cstnetIdStatus) {
		this.cstnetIdStatus = cstnetIdStatus;
	}
	public String[] getSecondaryEmails() {
		return secondaryEmails;
	}
	public void setSecondaryEmails(String[] secondaryEmails) {
		this.secondaryEmails = secondaryEmails==null?null:secondaryEmails.clone();
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getUmtId() {
		return umtId;
	}
	public void setUmtId(String umtId) {
		this.umtId = umtId;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("[umtid="+umtId).append(";cstnetId="+cstnetId);
		sb.append(";trueName="+trueName).append(";type="+type).append(";securityEmail="+securityEmail);
		sb.append(";cstnetIdStatus="+cstnetIdStatus);
		sb.append(";passwordType="+passwordType);
		if(secondaryEmails!=null&&secondaryEmails.length>0){
			sb.append("secondaryEmails:[");
			for(String e :secondaryEmails){
				sb.append(e).append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append("]");
		}
		sb.append("]");
		return sb.toString();
	}
}
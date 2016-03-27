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
package cn.vlabs.umt.services.certificate;

import java.util.Date;


/**
 * 证书管理类
 * 
 * @author xiejj@cstnet.cn
 * 
 */
public class DucklingCertificate {
	private String dn;
	private String cstnetId;
	private String fullCert;
	private String pubCert;
	private Date registTime;
	public String getDn() {
		return dn;
	}

	public String getFullCert() {
		return fullCert;
	}

	public String getPubCert() {
		return pubCert;
	}

	public void setDn(String dn) {
		this.dn = dn;
	}

	public void setFullCert(String fullCert) {
		this.fullCert = fullCert;
	}

	public void setPubCert(String pubCert) {
		this.pubCert = pubCert;
	}

	public String getCstnetId() {
		return cstnetId;
	}

	public void setCstnetId(String cstnetId) {
		this.cstnetId = cstnetId;
	}

	public Date getRegistTime() {
		return registTime;
	}

	public void setRegistTime(Date registTime) {
		this.registTime = registTime;
	}
}

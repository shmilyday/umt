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
package cn.vlabs.umt.proxy.impl;

import java.security.Principal;

import com.thoughtworks.xstream.XStream;

public abstract class CredContent {
	public void setAppKeyId(int appKeyId) {
		AppKeyId = appKeyId;
	}

	public int getAppKeyId() {
		return AppKeyId;
	}

	public void setUMTKeyId(int uMTKeyId) {
		UMTKeyId = uMTKeyId;
	}

	public int getUMTKeyId() {
		return UMTKeyId;
	}

	public void setIpAddress(String ipAddress) {
		IpAddress = ipAddress;
	}

	public String getIpAddress() {
		return IpAddress;
	}

	public void setValidTime(String validTime) {
		ValidTime = validTime;
	}

	public String getValidTime() {
		return ValidTime;
	}
	
	public static CredContent valueOf(String xml){
		XStream stream = getXStream();
		return (CredContent) stream.fromXML(xml);
	}
	
	public String toXML(){
		return getXStream().toXML(this);
	}
	
	public abstract Principal getPrincipal();
	
	private static XStream getXStream() {
		XStream stream = new XStream();
		return stream;
	}
	
	private int AppKeyId;
	private int UMTKeyId;
	private String IpAddress;
	private String ValidTime;
}
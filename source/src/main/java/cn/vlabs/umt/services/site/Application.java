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
package cn.vlabs.umt.services.site;
/**
 * 代表应用程序
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 7, 2009 9:41:40 AM
 */
public class Application {
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getUrl() {
		return url;
	}

	public void setKeyid(int keyid) {
		this.keyid = keyid;
	}
	public int getKeyid() {
		return keyid;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}
	public String getPublicKey() {
		return publicKey;
	}

	public void setServerType(String serverType) {
		this.serverType = serverType;
	}
	public String getServerType() {
		return serverType;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	public String getDescription() {
		return description;
	}

	public void setAllowOperate(boolean allowOperate) {
		this.allowOperate = allowOperate;
	}
	public boolean isAllowOperate() {
		return allowOperate;
	}

	private int id;
	private String name;
	private String url;
	private int keyid;
	private String serverType;
	private String publicKey;
	private String description;
	private boolean allowOperate;
}
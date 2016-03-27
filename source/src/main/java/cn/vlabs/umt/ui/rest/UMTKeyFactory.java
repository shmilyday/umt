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
package cn.vlabs.umt.ui.rest;

import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;
import cn.vlabs.umt.proxy.KeyFactory;
import cn.vlabs.umt.services.site.AppService;
import cn.vlabs.umt.services.site.Application;

public class UMTKeyFactory implements KeyFactory {
	public UMTKeyFactory(AppService service){
		this.service=service;
	}
	public RSAKey getAppKey() {
		return null;
	}

	public RSAKey getKey(int keyid) {
		String keycontent = service.getPublicKey(keyid);
		KeyFile kf = new KeyFile();
		return kf.loadFromString(keycontent);
	}

	public RSAKey getUMTKey(int keyid) {
		Application app=service.getApplication("umt");
		KeyFile kf = new KeyFile();
		return kf.loadFromString(app.getPublicKey());
	}
	
	private AppService service;
}
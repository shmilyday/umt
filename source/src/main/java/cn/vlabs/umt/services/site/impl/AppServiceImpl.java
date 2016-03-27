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
package cn.vlabs.umt.services.site.impl;

import java.util.Collection;

import cn.vlabs.duckling.common.crypto.HexUtil;
import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;
import cn.vlabs.umt.common.LRULinkedHashMap;
import cn.vlabs.umt.services.site.AppService;
import cn.vlabs.umt.services.site.Application;
import cn.vlabs.umt.services.site.ApplicationDAO;
import cn.vlabs.umt.services.site.ApplicationNotFound;
import cn.vlabs.umt.services.site.KeyDAO;

public class AppServiceImpl implements AppService {
	public AppServiceImpl(KeyDAO kd, ApplicationDAO ad){
		this.ad=ad;
		this.kd=kd;
		keys = new LRULinkedHashMap<Integer, RSAKey>(5);
	}
	public int createApplication(Application app) {
		if (app.getPublicKey()!=null){
			int keyid = kd.create(app.getPublicKey());
			app.setKeyid(keyid);
		}
		return ad.createApplication(app);
	}

	public void deleteApplication(String name) {
		ad.deleteApplication(name);
	}

	public void deleteApplication(int appid) {
		ad.deleteApplication(appid);
	}

	public Application getApplication(String name) {
		Application app = ad.getApplication(name);
		if (app!=null)
		{
			app.setPublicKey(kd.getContent(app.getKeyid()));
		}
		return app;
	}

	public void updateApplcation(Application app) {
		ad.updateApplcation(app);
	}

	public int updatePublicKey(int appid, String publickey) {
		int keyid = kd.create(publickey);
		ad.changePublicKey(appid, keyid);
		return keyid;
	}
	
	public String encrypt(String appname, String message) throws ApplicationNotFound {
		Application app = ad.getApplication(appname);
		if (app==null){
			throw new ApplicationNotFound(appname);
		}
		RSAKey rsa =getKey(app.getKeyid());
		
		return HexUtil.toHexString(rsa.encrypt(message.getBytes()));
	}
	
	public String getPublicKey(int keyid) {
		return kd.getContent(keyid);
	}
	public Collection<Application> getAllApplication(int start, int count) {
		return ad.getApplications(start, count);
	}
	public int getApplicationCount() {
		return ad.getApplicationCount();
	}
	private RSAKey getKey(int keyid){
		RSAKey key = keys.get(keyid);
		if (key==null){
			String keyContent = kd.getContent(keyid);
			if (keyContent!=null){
				KeyFile file = new KeyFile();
				key = file.loadFromString(keyContent);
				keys.put(keyid, key);
			}
		}
		return key;
	}
	private LRULinkedHashMap<Integer, RSAKey> keys;
	private KeyDAO kd;
	private ApplicationDAO ad;
	public Application getApplication(int appid) {
		return ad.getApplication(appid);
	}
}
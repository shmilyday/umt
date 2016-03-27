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
package cn.vlabs.umt.ui.servlet;

import java.io.File;

import cn.cnic.uaf.common.crypto.KeyFile;
import cn.cnic.uaf.common.crypto.RSAKey;
import cn.vlabs.umt.common.util.Config;

/**
 * 类描述：APP 签名
 *
 * 日期：2013-2-6
 *
 * 作者：wkm@cstnet.cn
 */
public class UAFAppCredentail {

	public UAFAppCredentail(Config config) {
		init(config.getMappedPath("umt.cert.keyfile", null));
	}

	public String getUMTId()
	{
		return "umt";
	}
	public byte[] decrypt(byte[] encryptedcontent)
	{
		return key.decrypt(encryptedcontent);
	}
	public byte[] encrypt(byte[] content)
	{
		return key.encrypt(content);
	}
	
	public RSAKey getUMTKey(){
		return key;
	}
	private void init(String keyfile){
		File f = new File(keyfile);
		KeyFile kf = new KeyFile();
		if (f.exists()){
			key = kf.load(keyfile);
		}else{
			key=new RSAKey();	//如果不存在直接创建新的应用
			key.generate();
			kf.saveKey(keyfile, key);
		}
		createTime = f.lastModified();
	}
	
    private long createTime;
	private RSAKey key;
	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}
	/**
	 * @param createTime the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
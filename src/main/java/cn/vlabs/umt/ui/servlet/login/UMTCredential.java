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
package cn.vlabs.umt.ui.servlet.login;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.log4j.Logger;

import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;
import cn.vlabs.umt.services.runtime.IRunTimePropService;
import cn.vlabs.umt.services.runtime.bean.RunTimeProp;

/**
 * UMT加解密的控制
 * 
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 8, 2009 1:35:59 PM
 */
public class UMTCredential {
	private static final Logger LOGGER=Logger.getLogger(UMTCredential.class);
	
	private long createTime;

	private RSAKey key;
	
	private IRunTimePropService service;

	public UMTCredential(IRunTimePropService service) {
		this.service=service;
		init();
	}
	private String toString(InputStream ins){
		BufferedReader reader =null;
		try{
			reader = new BufferedReader(new InputStreamReader(ins, "UTF-8"));
			StringBuffer sb=new StringBuffer();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			return sb.toString();
		}catch(IOException e){
			LOGGER.error(e.getMessage(),e);
		}finally{
			if(reader!=null){
				try {
					reader.close();
				} catch (IOException e) {
					LOGGER.error(e.getMessage(),e);
				}
			}
		}
		return null;
	}
	
	private void init() {
		RunTimeProp runTime=service.getValueByName(RunTimeProp.PROP_NAME_UMT_CERT);
		KeyFile kf = new KeyFile();
		if (runTime!=null) {
			key = kf.loadFromString(runTime.getPropValue());
			createTime = runTime.getLastModifyTime().getTime();
		} else {
			key = new RSAKey(); // 如果不存在直接创建新的应用
			key.generate();
			service.createProp(RunTimeProp.PROP_NAME_UMT_CERT, toString(kf.saveAsStream(key)));
			LOGGER.info("create umt cert");
			createTime = System.currentTimeMillis();
		}
		
	}

	public byte[] decrypt(byte[] encryptedcontent) {
		return key.decrypt(encryptedcontent);
	}

	public byte[] encrypt(byte[] content) {
		return key.encrypt(content);
	}

	/**
	 * @return the createTime
	 */
	public long getCreateTime() {
		return createTime;
	}
	public String getUMTId() {
		return "umt";
	}

	public RSAKey getUMTKey() {
		return key;
	}

	/**
	 * @param createTime
	 *            the createTime to set
	 */
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
}
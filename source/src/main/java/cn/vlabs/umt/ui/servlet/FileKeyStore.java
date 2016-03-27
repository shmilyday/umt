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

import org.apache.log4j.Logger;

import cn.vlabs.duckling.common.crypto.IKey;

/**
 * 文件存储工具
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 4, 2009 11:27:18 AM
 */
public class FileKeyStore {
	public static final Logger LOG=Logger.getLogger(FileKeyStore.class);
	public IKey load(String filepath){
		File f = new File(filepath);
		try {
			if (f.exists()) {
				java.io.ObjectInputStream in = null;
				try { // 从密钥文件中读取
					in = new java.io.ObjectInputStream(new java.io.FileInputStream(
							filepath));
					return (IKey) in.readObject();
				} finally {
					if (in != null){
						in.close();
					}
				}
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
	
	public void save(String filepath, IKey key){
		try{
			java.io.ObjectOutputStream out = null;
			try {
				out = new java.io.ObjectOutputStream(new java.io.FileOutputStream(
						filepath));
				out.writeObject(key);
			} finally {
				if (out != null){
					out.close();
				}
			}
		}catch(Exception e){
			LOG.error(e.getMessage(),e);
		}
	}
}
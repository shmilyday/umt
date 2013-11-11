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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Logger;

public final class UMTOauthConfig {
	private UMTOauthConfig(){}
	private static final Logger LOG = Logger.getLogger(UMTOauthConfig.class);
	private static final String configName ="umtoauthconfig.properties";
	private static Properties properties;
	public static String getProperty(String key){
		Properties p = getp();
		if(p==null){
			return null;
		}
		String result = p.getProperty(key);
		if(result!=null){
			return result.trim();
		}
		return result;
	}
	
	private static Properties getp(){
		if(properties==null){
			try {
				initProperty();
			} catch (IOException e) {
				LOG.error("加载umt oauth的配置文件"+configName+"出现错误");
			}
		}
		return properties;
	}

	private synchronized static void initProperty() throws IOException {
		if(properties==null){
			InputStream in  =UMTOauthConfig.class.getResourceAsStream("/"+configName);
			properties = new Properties();
			properties.load(in);
		}
	}
	
	
}
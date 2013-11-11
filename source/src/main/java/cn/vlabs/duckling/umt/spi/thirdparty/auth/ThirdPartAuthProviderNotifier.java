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
package cn.vlabs.duckling.umt.spi.thirdparty.auth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;

public final class ThirdPartAuthProviderNotifier {
	private static final Logger LOGGER = Logger.getLogger(ThirdPartAuthProviderNotifier.class);
	private Map<String,String> contextAttributes = new HashMap<String,String>();

	public ThirdPartAuthProviderNotifier(String configFile) {
		if(configFile != null)
		{
			File cFile = new File(configFile);
			if(cFile.exists()&&!cFile.isDirectory()&&configFile.endsWith("properties"))
			{
				Properties config = new Properties();
				FileInputStream fis=null;
				try {
					fis=new FileInputStream(cFile);
					config.load(fis);
					this.fillMap(contextAttributes, config);
				} catch (FileNotFoundException e) {
					LOGGER.error("第三方认证上下文配置文件未发现，文件名："+configFile,e);
				} catch (IOException e) {
					LOGGER.error("第三方认证上下文配置文件读取错误",e);
				}
				finally{
					if(fis!=null){
						try {
							fis.close();
						} catch (IOException e) {
							LOGGER.error(e.getMessage(),e);
						}
					}
				}
			}else{
					LOGGER.warn("请正确配置第三方认证上下文配置文件，文件名："+configFile);
			}
		}
	}
	private void fillMap(Map<String, String> configs, Properties defaultPros) {
		Enumeration<?> iter = defaultPros.propertyNames();
		while (iter.hasMoreElements()) {
			String key = (String)iter.nextElement();
			String value = defaultPros.getProperty(key);
			configs.put(key, value);
		}
	}

	public void notifyProvider() {
		try {
			List<URL> providers = scanProviders("META-INF/thirdparty-auth.properties");
			Iterator<URL> itr = providers.iterator();
			Properties p = new Properties();
	        while (itr.hasNext()) {
	            URL url = (URL) itr.next();
	            p.load(url.openStream());
	            String impl = p.getProperty(IThirdPartAuthProvider.class.getName());
	            if (impl != null) {
						try {
							((IThirdPartAuthProvider)(Thread.currentThread().getContextClassLoader().loadClass(impl).newInstance())).notify(contextAttributes);
						} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
							LOGGER.warn("实例化 IAuthProvider接口类发生错误 " + impl,e);
						}
	            }
	            p.clear();
	        }
		} catch (IOException e) {
			LOGGER.error("扫描第三方jar包中的META-INF/thirdparty-auth.properties文件时发生错误！",e);
		}
	} 
	
	private List<URL> scanProviders(String file) throws IOException {
		List<URL> list = new ArrayList<URL>();
        Enumeration<URL> enumeration = this.getClass().getClassLoader().getResources(file);
        while (enumeration.hasMoreElements()) {
            list.add(enumeration.nextElement());
        }
		return list;
	}
	
	

}
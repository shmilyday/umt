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
package cn.vlabs.duckling.api.umt.sso;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.log4j.Logger;

import cn.vlabs.duckling.common.properties.Config;

public final class SSOProperties {
   private static final Logger LOGGER=Logger.getLogger(SSOProperties.class);
   private SSOProperties(){}
   
   private static SSOProperties instance = new SSOProperties();
   private Config properties;
   public static SSOProperties getInstance()
   {
	   return instance;
   }
   public void initProperties(String fileName,boolean refresh)
   {
	   if(properties == null||refresh)
	   {
		   properties = new Config();
		   try {
			properties.load(new FileInputStream(fileName));
		} catch (FileNotFoundException e) {
			LOGGER.error(e.getMessage(),e);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
	   }
   }
   public String getProperty(String key)
   {
	  return properties.getProperty(key);   
   }
   public String getProperty(String key, String defaultValue) {
		return properties.getProperty(key, defaultValue);
	}
}
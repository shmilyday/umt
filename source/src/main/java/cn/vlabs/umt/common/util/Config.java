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
package cn.vlabs.umt.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import cn.vlabs.umt.ui.PathMapper;

public class Config{
	public static final String BEAN_ID="Config";
	public Config(PathMapper mapper, String filename) {
		props = new Properties();
		FileInputStream fis=null;
		try {
			fis=new FileInputStream(mapper.getRealPath(filename));
			props.load(fis);
		} catch (FileNotFoundException e) {
			log.error("配置文件" + filename + "未找到。");
		} catch (IOException e) {
			log.error(e);
		}finally{
			if(fis!=null){
				try {
					fis.close();
				} catch (IOException e) {
					log.error("",e);
				}
			}
		}
		
		this.mapper=mapper;
	}
	/**
	 * 从配置文件中读取一个整数值
	 * @param key	该配置项的关键字
	 * @param defaultval	缺省值
	 * @return 如果找到了该配置项，并且能转换成整数值，则返回读取的内容。否则返回缺省值
	 */
	public int getInt(String key, int defaultval) {
		String value = getStringProp(key, null);
		if (value != null) {
			try {
				return Integer.parseInt(value);
			} catch (NumberFormatException e) {
				log.warn("配置错误" + key + "=" + value + " 不是整数值");
			}
		}
		return defaultval;
	}

	/**
	 * 从配置文件中读取一个布尔值
	 * @param key	该配置项的关键字
	 * @param defaultValue 缺省值
	 * @return 如果找到了该配置项，并且能转换成布尔值，则返回读取的内容。否则返回缺省值
	 */
	public boolean getBooleanProp(String key, boolean defaultValue) {
		String value =  getStringProp(key, null);
		if (value != null)
			return Boolean.parseBoolean(value);
		else
			return defaultValue;
	}

	/**
	 * 从配置文件中获取一个字符串值
	 * @param key	该配置项的关键字
	 * @param defaultval 缺省值
	 * @return	如果找到了该配置项，则返回读取的内容。否则返回缺省值
	 */
	public String getStringProp(String key, String defaultval) {
		String value = props.getProperty(key);
		if (value != null) {
			return replace(value);
		} else
			return defaultval;
	}
	
	public String getMappedPath(String key, String defaultval){
		String before=this.getStringProp(key, defaultval);
		if (before!=null){
			return mapper.getRealPath(before);
		}else
			return null;
	}

	private String replace(String input) {
		Matcher matcher  = pattern.matcher(input);
    	StringBuffer value = new StringBuffer();
    	while(matcher.find())
    	{
    		String key = matcher.group(1);
    		String keyValue = this.getStringProp(key,key);
    		if(keyValue!=null)
    		{
    			matcher.appendReplacement(value, keyValue); 
    		}else
    		{
    			matcher.appendReplacement(value, key);
    		}
    		
    	}
    	matcher.appendTail(value);
		return value.toString();
	}
	private PathMapper mapper;
	private Properties props;
	private static final Logger log =Logger.getLogger(Config.class);;
	private static final Pattern pattern = Pattern.compile("\\$\\{([^}]*)\\}");
}
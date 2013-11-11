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
package cn.vlabs.umt.ui.tags.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.vlabs.umt.common.util.CommonUtils;

/**
 * 通过邮箱地址找到url
 * @author lvly
 * @since 2013-2-1
 */
public final class EmailUrlTagUtils {
	private EmailUrlTagUtils(){}
	private static Map<String,String> MAP=new HashMap<String,String>();
	static{
		MAP.put("qq.com", "http://mail.qq.com");
	    MAP.put("gmail.com","http://mail.google.com");
	    MAP.put("sina.com", "http://mail.sina.com.cn");
	    MAP.put("sina.cn", "http://mail.sina.com.cn");
	    MAP.put("163.com", "http://mail.163.com");
	    MAP.put("126.com", "http://mail.126.com");
	    MAP.put("sohu.com","http://mail.sohu.com");
	    MAP.put("tom.com", "http://mail.tom.com/");
	    MAP.put("sogou.com", "http://mail.sogou.com/");
	    MAP.put("139.com", "http://mail.10086.cn/");
	    MAP.put("hotmail.com", "http://www.outlook.com");
	    MAP.put("outlook.com", "http://www.outlook.com/");
	    MAP.put("live.cn", "http://login.live.cn/");
	    MAP.put("live.com.cn", "http://login.live.com.cn");
	    MAP.put("189.com", "http://webmail16.189.cn/webmail/");
	    MAP.put("yahoo.com.cn", "http://mail.cn.yahoo.com/");
	    MAP.put("yahoo.cn", "http://mail.cn.yahoo.com/");
	    MAP.put("eyou.com", "http://www.eyou.com/");
	    MAP.put("21cn.com", "http://mail.21cn.com/");
	    MAP.put("188.com", "http://www.188.com/");
	    MAP.put("foxmail.com", "http://www.foxmail.com");
        MAP.put("wo.com.cn", "http://mail.wo.com.cn/mail/login.action");
        MAP.put("2980.com", "http://www.2980.com/" );  
        MAP.put("56.com", "http://www.56.com/");
        MAP.put("hexun.com", "http://mail.hexun.com/");
        MAP.put("china.com", "http://mail.china.com/");
        MAP.put("263.net", "http://mail.263.net/");
        MAP.put("263.net.cn", "http://mail.263.net/");
        MAP.put("x263.net", "http://mail.263.net/");
	}
	/**
	 * 判断是不是email格式，主要判断是否包含@
	 * @param email
	 * @return isEmail
	 * */
	public static boolean isEmail(String email){
		return !CommonUtils.isNull(email)&&email.contains("@");
	}
	/**
	 * 获得邮箱地址的域名，当然得是email
	 * @param email fufyddns@sina.com
	 * @return sina.com
	 * */
	public static String getDomain(String email){
		if(isEmail(email)){
			return email.substring(email.indexOf("@")+1);
		}
		return null;
	}
	/**
	 * 获得已知域名邮箱的，超链接，如果找不到，那就是没有
	 * @param email fufyddns@sina.com等
	 * @return http://mail.sina.com.cn/
	 * */
	public static String getEmailLink(String email){
		if(isEmail(email)){
			String domain=getDomain(email);
			Set<Entry<String,String>> set=MAP.entrySet();
			for(Entry<String,String> entry:set){
				if(domain.equals(entry.getKey())){
					return entry.getValue();
				}
			}
		}
		return "";
	}

}
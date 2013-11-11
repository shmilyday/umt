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
package cn.vlabs.umt.services.session;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

/**
 * Session工具类
 * @author lvly
 * @since 2013-1-16
 */
public final class SessionUtils {
	private SessionUtils(){}
	/**
	 * 把一些值带到session保存起来
	 * */
	public static void setSessionVar(HttpServletRequest request,String key,Object obj){
		getSession(request).setAttribute(key, obj);
	}
	/**
	 * 取得用户信息
	 * */
	public static User getUser(HttpServletRequest request){
		UMTContext context=new UMTContext(request);
		return context.getCurrentUMTUser();
		
	}
	/**
	 * 取得用户Id
	 * */
	public static int getUserId(HttpServletRequest request){
		User user=getUser(request);
		return user==null?-1:user.getId();
	}
	/**
	 * 取得，返回值地址，应用名称等信息
	 * */
	public static Map<String,String> getSiteInfo(HttpServletRequest request){
		return (Map<String,String>)getSession(request).getAttribute(Attributes.SITE_INFO);
	}
	
	
	/**
	 * 取之，同上
	 * */
	public static String getSessionVar(HttpServletRequest request,String key){
		Object obj=getSession(request).getAttribute(key);
		return obj==null?null:obj.toString();
	}
	/**
	 * 取得request
	 * */
	public static HttpSession getSession(HttpServletRequest request){
		return request.getSession(true);
	}
	/**
	 * 删除第三方绑定的信息
	 * */
	public static void removeThirdPartyInfo(HttpServletRequest request){
		getSession(request).removeAttribute(Attributes.THIRDPARTY_USER);
		getSession(request).removeAttribute(Attributes.THIRDPARTY_ACCESS_TOKEN);
		getSession(request).removeAttribute(Attributes.THIRDPARTY_CODE);
		getSession(request).removeAttribute(Attributes.THIRDPARTY_OPEN_ID);
		getSession(request).removeAttribute(Attributes.THIRDPARTY_TYPE);
	}
	/**
	 * 把session里的登录信息，置为已激活
	 * */
	public static void toActive(HttpServletRequest request){
		LoginInfo info=UMTContext.getLoginInfo(getSession(request));
		if(info.getLoginNameInfo()!=null){
			info.getLoginNameInfo().setStatus(LoginNameInfo.STATUS_ACTIVE);
		}
	}

}
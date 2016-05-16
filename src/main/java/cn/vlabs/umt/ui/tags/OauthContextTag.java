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
package cn.vlabs.umt.ui.tags;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.duckling.cloudy.common.CookieUtils;
import cn.vlabs.umt.common.util.CommonUtils;

public class OauthContextTag  extends TagSupport {
	private static final long serialVersionUID = 5172095130610228924L;

	@Override
	public int doStartTag() throws JspException {

		HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
		String schema=getScheme(request);
		String preBtnClass="unlockImg";
		boolean isHttps=isHttps(schema);
		if(isHttps){
			preBtnClass="lockImg";
		}
		Cookie autoFillCookie=CookieUtils.getCookie(request, "AUTO_FILL");
		pageContext.setAttribute("preSpanId", preBtnClass);
		if(autoFillCookie!=null){
			pageContext.setAttribute("autoFill", CommonUtils.killNull(autoFillCookie.getValue()));
		}
		pageContext.setAttribute("isHttps", isHttps); 
		pageContext.setAttribute("context", request.getContextPath());
		return super.doStartTag();
	}
	private String getScheme(HttpServletRequest request){
		String header=request.getHeader("x-forward-scheme");
		if(CommonUtils.isNull(header)){
			header=request.getScheme();
		}
		return header;
		
	}
	private boolean isHttps(String schema){
		return "https".equals(schema);
	}

}

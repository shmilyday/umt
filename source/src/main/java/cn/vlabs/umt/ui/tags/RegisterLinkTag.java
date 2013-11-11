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
package cn.vlabs.umt.ui.tags;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.ui.Attributes;

public class RegisterLinkTag extends TagSupport {
	public int doStartTag() throws JspException {
		try {
			
			String contextPath = ((HttpServletRequest)pageContext.getRequest()).getContextPath(); 
			String link = "/regist.jsp";
			if(StringUtils.isNotEmpty(contextPath)){
				link=contextPath+link;
			}
			Map<String,String> params =SessionUtils.getSiteInfo((HttpServletRequest)(pageContext.getRequest()));
			if(params!=null){
				String otherAppRegisterLink = params.get(Attributes.APP_REGISTER_URL_KEY);
				link = otherAppRegisterLink!=null?otherAppRegisterLink:link;
			}
			pageContext.getOut().print("<a href="+link+">");
			
		} catch (IOException e) {
		}
		return EVAL_BODY_INCLUDE;
	}

	public int doEndTag() throws JspException{
		try {
			pageContext.getOut().print("</a>");
		} catch (IOException e) {
			
		}
		return EVAL_PAGE;
	}
	private static final long serialVersionUID = 1L;
}
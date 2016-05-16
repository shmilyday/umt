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

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.commons.lang.StringUtils;

import cn.vlabs.umt.ui.Attributes;

public class SsoParamsTag extends TagSupport{

	public int doEndTag()throws JspException{
		ServletRequest request = pageContext.getRequest();
		JspWriter writer = pageContext.getOut();
		for (String param:Attributes.SSO_PARAMS){
			String value = request.getParameter(param);
			if (StringUtils.isNotBlank(value)){
				try {
					writer.println(String.format("<input type=\"hidden\" name=\"%s\" value=\"%s\">", param, value));
				} catch (IOException e) {
					throw new JspException(e);
				}
			}
		}
		return EVAL_PAGE;
	}
	private static final long serialVersionUID = 1L;
}
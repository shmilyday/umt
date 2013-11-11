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
import java.io.Writer;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.services.requests.RequestService;
import cn.vlabs.umt.services.requests.UserRequest;
import cn.vlabs.umt.ui.Attributes;


public class RequestCount extends TagSupport {
	public int doStartTag() throws JspException {
		BeanFactory factory = (BeanFactory) pageContext.getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		RequestService us = (RequestService) factory.getBean("RequestService");
		int count=us.getRequestCount(UserRequest.INIT);
		try {
			Writer writer = pageContext.getOut();
			writer.write(Integer.toString(count));
			writer.flush();
		} catch (IOException e) {
			throw new JspException(e);
		}
		return SKIP_BODY;
	}

	public int doEndTag() throws JspException{
		return EVAL_PAGE;
	}
	private static final long serialVersionUID = 1L;
}
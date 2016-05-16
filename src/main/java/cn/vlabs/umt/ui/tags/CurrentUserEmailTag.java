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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.UMTContext;

public class CurrentUserEmailTag extends TagSupport {
	public int doStartTag() throws JspException {
		UMTContext context = new UMTContext((HttpServletRequest) pageContext.getRequest());
		User up = context.getLoginInfo().getUser();
		try {
			if (up!=null){
				JspWriter writer = pageContext.getOut();
				writer.print(CommonUtils.killNull(up.getCstnetId()));
				writer.flush();
			}
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
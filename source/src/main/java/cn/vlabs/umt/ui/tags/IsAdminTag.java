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

import javax.servlet.jsp.tagext.TagSupport;

import cn.vlabs.umt.ui.UMTContext;

public class IsAdminTag extends TagSupport {
	public int doStartTag(){
		if (UMTContext.isAdminUser(pageContext.getSession())){
			return EVAL_BODY_INCLUDE;
		}
		else{
			return SKIP_BODY;
		}
	}

	public int doEndTag(){
		return EVAL_PAGE;
	}
	private static final long serialVersionUID = 1L;
}
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

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.UMTSystem;
import cn.vlabs.umt.ui.Attributes;

public class SystemTag extends TagSupport {
	public int doStartTag(){
		if (system==null){
			system = getUMTSystem();
		}
		pageContext.setAttribute(var, system);
		return EVAL_BODY_INCLUDE;
	}


	public int doEndTag(){
		pageContext.removeAttribute(var);
		return EVAL_PAGE;
	}
	
	public void setVar(String var){
		this.var=var;
	}
	private UMTSystem getUMTSystem() {
		BeanFactory factory = (BeanFactory)pageContext.getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		return factory.getBean(UMTSystem.class);
	}
	private String var;
	private UMTSystem system;
	private static final long serialVersionUID = 1L;
}
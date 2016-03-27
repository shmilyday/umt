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
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RequestUtil;

/**
 * @author lvly
 * @since 2013-1-17
 */
public class AbsoluteUrlTag extends TagSupport {
	private static final Logger LOGGER=Logger.getLogger(AbsoluteUrlTag.class);
	private String value;
	public void setValue(String value){
		this.value=value;
	}
	public String getValue(){
		return this.value;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2747829603275284165L;

	public int doStartTag(){
		JspWriter out = this.pageContext.getOut();
		String url=RequestUtil.getContextPath((HttpServletRequest)this.pageContext.getRequest());
		try {
			if(CommonUtils.isNull(url)){
				if(this.value.startsWith("/")){
					out.print(this.value);
				}else{
					out.print("/"+this.value);
				}
			}else{
				out.print(url+this.value);
			}
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return SKIP_BODY;
	}
}
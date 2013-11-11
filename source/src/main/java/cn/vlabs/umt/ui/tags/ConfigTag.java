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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.ui.Attributes;

/**
 * 读取服务项
 * @author lvly
 * @since 2013-1-17
 */
public class ConfigTag extends TagSupport {
	private static final Logger LOGGER=Logger.getLogger(ConfigTag.class);
	private String key;
	public void setKey(String key){
		this.key=key;
	}
	public String getKey(){
		return this.key;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2747829603275284165L;
	
	public Config getConfig(){
		HttpServletRequest request=(HttpServletRequest)this.pageContext.getRequest();
		BeanFactory beanFactory=(BeanFactory) request.getServletContext()
		.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		return (Config)beanFactory.getBean(Config.BEAN_ID);
	}
	public int doStartTag(){
		JspWriter out = this.pageContext.getOut();
		try {
			out.print(getConfig().getStringProp(key, ""));
		} catch (IOException e) {
			LOGGER.error(e.getMessage(),e);
		}
		return SKIP_BODY;
	}
}
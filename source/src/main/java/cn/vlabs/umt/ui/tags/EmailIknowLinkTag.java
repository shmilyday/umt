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

import javax.servlet.jsp.tagext.TagSupport;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.ui.tags.utils.EmailUrlTagUtils;

/**
 * 判断所提供的Email，系统是否知道超链接
 * @author lvly
 * @since 2013-1-17
 */
public class EmailIknowLinkTag extends TagSupport {
	private String email;
	public void setEmail(String email){
		this.email=email;
	}
	public String getEmail(){
		return this.email;
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 2747821239327528165L;

	public int doStartTag(){
		if(CommonUtils.isNull(EmailUrlTagUtils.getEmailLink(email))){
			return SKIP_BODY;
		}else{
			return EVAL_BODY_INCLUDE;
		}
	}
}
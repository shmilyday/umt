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
package cn.vlabs.umt.ui.actions;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import cn.vlabs.umt.common.util.CommonUtils;

/** 
 * MyEclipse Struts
 * Creation date: 12-29-2009
 * 
 * XDoclet definition:
 * @struts.form name="editTemplateForm"
 */
public class EditTemplateForm extends ActionForm {

	private static final long serialVersionUID = 1L;

	private String email;
	private String password;
	private String smtp;
	
	private String title;
	private String content;
	private String target;
	
	public String getEmail() {
		return CommonUtils.trim(email);
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmtp() {
		return CommonUtils.trim(smtp);
	}

	public void setSmtp(String smtp) {
		this.smtp = smtp;
	}

	public String getTitle() {
		return CommonUtils.trim(title);
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return CommonUtils.trim(content);
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTarget() {
		return CommonUtils.trim(target);
	}

	public void setTarget(String target) {
		this.target = target;
	}


	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

}
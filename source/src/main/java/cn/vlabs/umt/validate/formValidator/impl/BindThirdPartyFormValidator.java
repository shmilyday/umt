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
package cn.vlabs.umt.validate.formValidator.impl;

import javax.servlet.http.HttpServletRequest;

import cn.vlabs.umt.validate.domain.ErrorMsg;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.FormValidator;
import cn.vlabs.umt.validate.validator.ValidatorFactory;

/**
 * @author lvly
 * @since 2013-2-25
 */
public class BindThirdPartyFormValidator extends FormValidator{
	public BindThirdPartyFormValidator(HttpServletRequest request){
		setRequest(request);
	}
	@Override
	public ErrorMsgs validateForm() {
		ErrorMsgs errors=new ErrorMsgs();
		validateUserName(errors, "loginName");
		this.validatePassword(errors,"loginPassword");
		setMsgs(errors);
		if(!errors.isPass()){
			getRequest().setAttribute("hidden", false);
			errorToRequest();
		}
		return errors;
	}
	@Override
	protected void validatePassword(ErrorMsgs errors, String name) {
		String password=getRequest().getParameter(name);
		if(!ValidatorFactory.getRequiredValidator().validate(password)){
			errors.addMsg(new ErrorMsg(name,"common.validate.password.required"));
			return;
		}
	}
}
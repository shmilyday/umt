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
package cn.vlabs.umt.validate.formValidator.impl;

import javax.servlet.http.HttpServletRequest;

import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.FormValidator;

/**
 * @author lvly
 * @since 2013-1-22
 */
public class UpdatePasswordFormValidator extends FormValidator{
	public UpdatePasswordFormValidator(HttpServletRequest request){
		setRequest(request);
	}
	@Override
	public ErrorMsgs validateForm() {
		ErrorMsgs errors=new ErrorMsgs();
		validatePassword(errors,"password");
		validateRepassword(errors,"retype","password");
		setMsgs(errors);
		if(!errors.isPass()){
			errorToRequest();
		}
		return errors;
	}

}
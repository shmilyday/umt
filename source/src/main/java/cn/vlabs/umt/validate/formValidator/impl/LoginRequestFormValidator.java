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

import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.FormValidator;

/**
 * 登陆后端验证，暂时无用，需求逻辑混乱，有无必要尚待考察
 * @author lvly
 * @since 2013-1-21
 */
public class LoginRequestFormValidator extends FormValidator{
	/**
	 * 构造方法
	 * @param request http请求，主要用来获取表单信息，和bean
	 * */
	public LoginRequestFormValidator(HttpServletRequest request){
		setRequest(request);
	}
	@Override
	public ErrorMsgs validateForm() {
		ErrorMsgs errors=new ErrorMsgs();
		validateUserName(errors,"username");
		validatePassword(errors,"password");
		setMsgs(errors);
		if(!errors.isPass()){
			errorToRequest();
		}
		return errors;
	}
}
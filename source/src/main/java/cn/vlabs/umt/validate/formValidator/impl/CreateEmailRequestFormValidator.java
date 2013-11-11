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
 * 
 * 创建core_mail账号验证器
 * @author lvly
 * @since 2013-1-21
 */
public class CreateEmailRequestFormValidator extends FormValidator {
	private static final String REGIX="^[a-zA-Z0-9\\-]*$";
	/**
	 * 构造方法
	 * @param request http请求，主要用来获取表单信息，和bean
	 * */
	public CreateEmailRequestFormValidator(HttpServletRequest request){
		setRequest(request);
	}
	@Override
	public ErrorMsgs validateForm() {
		ErrorMsgs errors=new ErrorMsgs();
		validateUserName(errors,"username");
		validateUserNameExt(errors,"username");
		validatePassword(errors,"password");
		validateRepassword(errors,"repassword","password");
		validateTrueName(errors,"truename");
		validateCode(errors,"validcode");
		setMsgs(errors);
		if(!errors.isPass()){
			errorToRequest();
		}
		return errors;
	}
	/**
	 * 重写
	 * 验证格式不同
	 * */
	@Override
	protected void validateUserName(ErrorMsgs errors,String name){
		String username=getRequest().getParameter(name);
		if(!ValidatorFactory.getRequiredValidator().validate(username)){
			errors.addMsg(new ErrorMsg(name,"common.validate.email.required"));
			return;
		}
		if(!ValidatorFactory.getRegixValidator(REGIX).validate(username)){
			errors.addMsg(new ErrorMsg(name,"common.validate.escience.email.invalid"));
			return;
		}
		if(!ValidatorFactory.getUserExistValidator(getBeanFactory()).validate(username)){
			errors.addMsg(new ErrorMsg(name,"regist.user.exist"));
		}
		if(!ValidatorFactory.getMinLengthValidator(3).validate(username)){
			errors.addMsg(new ErrorMsg(name,"input.email.length"));
			return;
		}
	}
	
}
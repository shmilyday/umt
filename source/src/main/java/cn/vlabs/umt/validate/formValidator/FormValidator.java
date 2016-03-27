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
package cn.vlabs.umt.validate.formValidator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.validate.domain.ErrorMsg;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.validator.ValidatorFactory;


/**
 * 表单验证的扩展类，有一些基本的验证方法，继承，并直接调用即可，
 * 囊括了umt主要的字段验证
 * @author lvly
 * @since 2013-1-21
 */
public abstract class FormValidator {
	/**
	 * 最重要的变量，无论是值还是bean都从这里获得
	 * */
	private HttpServletRequest request;
	/**
	 * 运行结果，如果为空，则视为pass，里面有isPass()方法
	 * */
	private ErrorMsgs msgs;
	
	
	public ErrorMsgs getMsgs() {
		return msgs;
	}
	public void setMsgs(ErrorMsgs msgs) {
		this.msgs = msgs;
	}
	public HttpServletRequest getRequest() {
		return request;
	}
	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	/**
	 * 抽象方法，子类可扩展，自定义验证方法
	 * */
	public abstract ErrorMsgs validateForm();
	/**
	 * 把验证信息，放到request里面
	 * 规则是  paramName_error,error_key
	 * 
	 * */
	protected void errorToRequest(){
		if(msgs!=null){
			for(ErrorMsg msg:msgs.getMsgs()){
				request.setAttribute(msg.getProperty()+"_error", msg.getKey());
			}
		}
	}
	protected BeanFactory getBeanFactory(){
		return (BeanFactory)request.getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
	/**
	 * 验证图形验证码
	 * 验证是否正确
	 * @param request
	 * @param errors
	 * @param name 字段名
	 */
	protected void validateCode(ErrorMsgs errors,String name) {
		String validcode=request.getParameter(name);
		if(!ValidatorFactory.getValidCodeEqualsValidator(getRequest()).validate(validcode)){
			errors.addMsg(new ErrorMsg(name,"regist.wrongValidcode"));
			return;
		}
		
	}
	/**
	 * 验证用户邮箱
	 * 验证非空，和邮箱是否合法
	 * * @param request
	 * @param errors
	 * @param name 字段名
	 * */
	protected void validateUserName(ErrorMsgs errors,String name){
		String username=request.getParameter(name);
		if(!ValidatorFactory.getRequiredValidator().validate(username)){
			errors.addMsg(new ErrorMsg(name,"common.validate.email.required"));
			return;
		}
		if(!ValidatorFactory.getEmailRegixValidator().validate(username)){
			errors.addMsg(new ErrorMsg(name,"common.validate.email.invalid"));
			return;
		}
	}
	/**
	 * 验证用户名是否存在
	 * * @param request
	 * @param errors
	 * @param name 字段名
	 * */
	protected void validateUserNameExt(ErrorMsgs errors,String name){
	if(!ValidatorFactory.getUserExistValidator(getBeanFactory()).validate(request.getParameter(name))){
		errors.addMsg(new ErrorMsg(name,"regist.user.exist"));
	}
	}
	/**
	 * 验证密码
	 * 验证长度，为空
	 * * @param request
	 * @param errors
	 * @param name 字段名
	 * */
	protected void validatePassword(ErrorMsgs errors,String name){
		String password=request.getParameter(name);
		if(!ValidatorFactory.getRequiredValidator().validate(password)){
			errors.addMsg(new ErrorMsg(name,"common.validate.password.required"));
			return;
		}
		if(!ValidatorFactory.getMinLengthValidator(8).validate(password)){
			errors.addMsg(new ErrorMsg(name,"common.validate.password.minlength"));
			return;
		}
	}
	/**
	 * 验证重复密码
	 * 验证，两次输入是否一致，为空，和最小长度
	 * * @param request
	 * @param errors
	 * @param rePasswordname 重复密码字段名
	 * @param password 密码字段名
	 * 
	 * */
	protected void validateRepassword(ErrorMsgs errors,String repasswordName,String passwordName){
		String repassword=request.getParameter(repasswordName);
		if(!ValidatorFactory.getRequiredValidator().validate(repassword)){
			errors.addMsg(new ErrorMsg(repasswordName,"common.validate.repassword.required"));
			return;
		}
		if(!ValidatorFactory.getMinLengthValidator(6).validate(repassword)){
			errors.addMsg(new ErrorMsg(repasswordName,"common.validate.repassword.minlength"));
			return;
		}
		String password=request.getParameter(passwordName);
		if(!ValidatorFactory.getPasswordEqualsValidator(password).validate(repassword)){
			errors.addMsg(new ErrorMsg(repasswordName,"common.validate.password.retype.not.equals"));
			return;
		}
	}
	/**
	 * 验证姓名
	 * 验证不为空
	 * @param request
	 * @param errors
	 * @param name 验证字段名
	 * */
	protected void validateTrueName(ErrorMsgs errors,String name){
		String repassword=request.getParameter(name);
		if(!ValidatorFactory.getRequiredValidator().validate(repassword)){
			errors.addMsg(new ErrorMsg(name,"common.validate.truename.required"));
			return;
		}
	}
}
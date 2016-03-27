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

import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.validate.domain.ErrorMsg;
import cn.vlabs.umt.validate.domain.ErrorMsgs;
import cn.vlabs.umt.validate.formValidator.FormValidator;
import cn.vlabs.umt.validate.validator.ValidatorFactory;

/**
 * 重置密码验证
 * @author lvly
 * @since 2013-1-21
 */
public class RemindPasswordFormValidator extends FormValidator{
	/**
	 * 构造方法
	 * @param request http请求，主要用来获取表单信息，和bean
	 * */
	public RemindPasswordFormValidator(HttpServletRequest request) {
		setRequest(request);
	}
	@Override
	public ErrorMsgs validateForm() {
		ErrorMsgs errors=new ErrorMsgs();
		validateLoginEmail(errors,"loginEmail");
		validateCode( errors, "ValidCode");
		setMsgs(errors);
		if(!errors.isPass()){
			errorToRequest();
		}
		return errors;
	}
	private void validateLoginEmail(ErrorMsgs errors,String paramname){
		String loginEmail=getRequest().getParameter(paramname);
		if(!ValidatorFactory.getRequiredValidator().validate(loginEmail)){
			errors.addMsg(new ErrorMsg(paramname,"common.validate.email.required"));
			return;
		}
		if(!ValidatorFactory.getEmailRegixValidator().validate(loginEmail)){
			errors.addMsg(new ErrorMsg(paramname,"common.validate.email.invalid"));
			return;
		}
		UserService userService=ServiceFactory.getUserService(getRequest());
		User user= userService.getUserByLoginName(loginEmail);
		if(user==null&&ICoreMailClient.getInstance().isUserExt(loginEmail)){
			user=userService.upgradeCoreMailUser(loginEmail);
		}
		//这里逻辑跟注册相反，所以没有反号
		if(user==null){
			errors.addMsg(new ErrorMsg(paramname,"remindpass.usernotfound"));
			return;
		}
		LoginNameInfo info=ServiceFactory.getLoginNameService(getRequest()).getALoginNameInfo(user.getId(), loginEmail);
		if(LoginNameInfo.STATUS_TEMP.equals(info.getStatus())&&LoginNameInfo.LOGINNAME_TYPE_SECONDARY.equals(info.getType())){
			errors.addMsg(new ErrorMsg(paramname,"remindpass.usernotfound"));
			return;
		}
	}

}
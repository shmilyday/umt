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

import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.validate.validator.Validator;
import cn.vlabs.umt.validate.validator.ValidatorFactory;
/**
 * 主要用于登陆页面，验证
 * @author lvly
 * @since 2013-2-1
 */
public class LoginValidateAction extends DispatchAction{
	/**
	 * 是否是coreMail账户
	 * */
	public ActionForward isCoreMail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String email=request.getParameter("email");
		
		Validator emailValidate=ValidatorFactory.getEmailRegixValidator();
		response.setContentType("json");
		PrintWriter writer = response.getWriter();
		boolean isEmail=emailValidate.validate(email);
		if(isEmail){
			writer.println(ICoreMailClient.getInstance().isUserExt(email));
		}else{
			writer.println(false);
		}
		writer.flush();
		writer.close();
		return null;
	}
}
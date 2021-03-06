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
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

/** 
 * MyEclipse Struts
 * Creation date: 12-17-2009
 * 
 * XDoclet definition:
 * @struts.action path="/updateUser" name="updateUserForm" input="/index.jsp" parameter="act" scope="request" validate="true"
 */
public class UpdateUserAction extends DispatchAction {
	public ActionForward show(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		UserService us =(UserService) factory.getBean("UserService");
		UMTContext context= new UMTContext(request);
		
		request.setAttribute("User", us.getUserByLoginName(context.getLoginInfo().getUser().getCstnetId()));
		return mapping.getInputForward();
	}
	
	public ActionForward update(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		UserService us =(UserService) factory.getBean("UserService");
		UMTContext context= new UMTContext(request);
		
		UpdateUserForm updateUserForm = (UpdateUserForm) form;
		User u = updateUserForm.getUser();
		
		if ( u.getCstnetId().equals(context.getCurrentUMTUser().getCstnetId())){
			us.update(u);
		}
		request.setAttribute("User", us.getUserByLoginName(context.getCurrentUMTUser().getCstnetId()));
		request.setAttribute("message", "userinfo.message.updated");
		return mapping.getInputForward();
	}
	
}
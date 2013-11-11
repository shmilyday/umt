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

import java.util.Collection;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.Attributes;

/** 
 * MyEclipse Struts
 * Creation date: 12-29-2009
 * 
 * XDoclet definition:
 * @struts.action path="/admin/manageRole" name="manageRoleForm" input="/admin/managerole.jsp" parameter="act" scope="request" validate="true"
 */
public class ManageRoleAction extends DispatchAction {
	public ActionForward load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		RoleService rs = getRoleService();
		Collection<User> users = rs.getRoleMembers("admin");
		request.setAttribute("users", users);
		return mapping.getInputForward();
	}
	
	
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		RoleService rs = getRoleService();
		String username = request.getParameter("username");
		rs.addMember("admin", username);
		return load(mapping, form, request, response);
	}
	
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		RoleService rs = getRoleService();
		String[] usernames = request.getParameterValues("usernames");
		rs.removeMemeber("admin", usernames);
		return load(mapping, form, request, response);
	}
	private RoleService getRoleService(){
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		RoleService rs = (RoleService)factory.getBean("RoleService");
		return rs;
	}
}
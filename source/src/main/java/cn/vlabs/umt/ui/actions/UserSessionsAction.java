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

import cn.vlabs.umt.services.session.LoginRecordVO;
import cn.vlabs.umt.services.session.SessionService;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

/** 
 * MyEclipse Struts
 * Creation date: 12-18-2009
 * 
 * XDoclet definition:
 * @struts.action path="/userSessions" name="userSessionsForm" input="/sessions.jsp" scope="request" validate="true"
 */
public class UserSessionsAction extends DispatchAction {
	public ActionForward load(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		SessionService service = (SessionService) factory.getBean("SessionService");
		UMTContext context= new UMTContext(request);
		Collection<LoginRecordVO> records = service.getAllSessions(context.getLoginInfo().getUser().getCstnetId());
		if (records.size()>0)
		{
			request.setAttribute("records", records);
		}
		return mapping.getInputForward();
	}
	
	public ActionForward logout(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		SessionService service = (SessionService) factory.getBean("SessionService");
		String idString = request.getParameter("id");
		int id=Integer.parseInt(idString);
		service.logout(id);
		return load(mapping, form, request, response);
	}
}
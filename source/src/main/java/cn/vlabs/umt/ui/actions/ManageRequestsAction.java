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

import cn.vlabs.umt.common.util.PageBean;
import cn.vlabs.umt.services.requests.RequestService;
import cn.vlabs.umt.services.requests.UserExist;
import cn.vlabs.umt.services.requests.UserRequest;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;


/** 
 * MyEclipse Struts
 * Creation date: 12-21-2009
 * 
 * XDoclet definition:
 * @struts.action path="/manageRequests" name="manageRequestsForm" input="/requests.jsp" parameter="act" scope="request" validate="true"
 */
public class ManageRequestsAction extends DispatchAction {

	public ActionForward approve(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		RequestService us = (RequestService) factory.getBean("RequestService");

		ManageRequestsForm requestForm = (ManageRequestsForm) form;
		
		UMTContext context = new UMTContext(request);
		try {
			us.approveRequest(requestForm.getRid(), context);
		} catch (UserExist e) {
			request.setAttribute("error", "message.registuser.exist");
		} catch (InvalidUserNameException e) {
			request.setAttribute("error", "message.registuser.exist");
		}
		requestForm.setTotal(null);
		return showRequests(mapping, form, request, response);
	}
	
	public ActionForward deny(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		RequestService us = (RequestService) factory.getBean("RequestService");

		ManageRequestsForm requestForm = (ManageRequestsForm) form;
		
		UMTContext context = new UMTContext(request);
		us.denyRequest(requestForm.getRid(), context);
		
		requestForm.setTotal(null);
		return showRequests(mapping, form, request, response);
	}
	
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		RequestService us = (RequestService) factory.getBean("RequestService");

		ManageRequestsForm requestForm = (ManageRequestsForm) form;
		
		UMTContext context = new UMTContext(request);
		us.removeRequest(requestForm.getRid(), context);
		
		requestForm.setTotal(null);
		return showRequests(mapping, form, request, response);
	}
	
	public ActionForward showRequests(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		RequestService us = (RequestService) factory.getBean("RequestService");

		ManageRequestsForm requestForm = (ManageRequestsForm) form;
		int total = 0;
		int page=0;
		if (requestForm.getTotal()!=null){
			try{
				total = Integer.parseInt(requestForm.getTotal());
				page= Integer.parseInt(requestForm.getPage());
			}catch (NumberFormatException e){
				total = us.getRequestCount(UserRequest.INIT);
			}
		}else{
			total = us.getRequestCount(UserRequest.INIT);
		}
		
		PageBean bean = new PageBean(total);
		bean.setCurrentPage(page);
		bean.setItems(us.getRequests(UserRequest.INIT, bean.getStart(), bean.getRecordPerPage()));
		
		request.setAttribute("PageBean", bean);
		return mapping.getInputForward();
	}
}
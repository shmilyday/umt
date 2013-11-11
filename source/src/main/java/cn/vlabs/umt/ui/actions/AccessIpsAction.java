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

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.service.IAccessIPService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;

/**
 * @author lvly
 * @since 2013-3-15
 */
public class AccessIpsAction extends DispatchAction{
	/**
	 * 显示所有ip
	 * */
	public ActionForward showAccessIps(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("ips",getIAccessIPService(request).getAllAccessIps());
		return mapping.findForward("ip.list");
	}
	private IAccessIPService getIAccessIPService(HttpServletRequest request){
		return (IAccessIPService) ServiceFactory.getBean(request, IAccessIPService.BEAN_ID);
	}
	/**
	 * 删除
	 * */
	public ActionForward delete(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		getIAccessIPService(request).deleteIp(Integer.valueOf(request.getParameter("ipId")));
		return showAccessIps(mapping,form,request,response);
	}
	/**
	 *增加 
	 * */
	public ActionForward add(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		getIAccessIPService(request).addAccessIp(SessionUtils.getUserId(request),CommonUtils.trim(request.getParameter("ip")));
		response.sendRedirect(request.getContextPath()+"/admin/accessIps.do?act=showAccessIps");
		return null;
	}
	

}
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

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.AssociateUser;
import cn.vlabs.umt.services.user.service.IAssociateUserService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;

/**
 * 账户管理
 * @author lvly
 * @since 2013-1-29
 */
public class UserManageAction extends DispatchAction{
	/**
	 * 显示账户管理页面
	 * */
	public ActionForward showManage(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("user.manage.show");
	}
	/**
	 * 显示更改密码页面
	 * */
	public ActionForward showChangePassword(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("user.manage.password.show");
	}
	/**
	 * 显示账户绑定页面
	 * */
	public ActionForward showBindAccount(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("bindInfos", ServiceFactory.getUserService(request).getBindInfosByUid(SessionUtils.getUserId(request)));
		return mapping.findForward("user.manage.bind.show");
	}
	/**
	 * 删除绑定
	 * */
	public ActionForward deleteBind(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ServiceFactory.getUserService(request).deleteBindById(Integer.valueOf(request.getParameter("bindId")));
		response.sendRedirect(RequestUtil.getContextPath(request)+"/user/manage.do?act=showBindAccount");
		return null;
	}
	/**
	 * 显示账户关联页面
	 * */
	public ActionForward showAssociate(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		IAssociateUserService associateService=ServiceFactory.getAssociateUserService(request);
		boolean isAssociated=associateService.isAssociated(SessionUtils.getUserId(request));
		request.setAttribute("isAssociated", isAssociated);
		AssociateUser user=associateService.getAssociate(SessionUtils.getUserId(request));
		request.setAttribute("associate",user);
		request.setAttribute("associatedUser", ServiceFactory.getUserService(request).getUserByUid(user.getAssociateUid()));
		return mapping.findForward("user.manage.associate");
	}
}
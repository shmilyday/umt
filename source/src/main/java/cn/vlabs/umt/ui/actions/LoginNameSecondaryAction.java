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
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 更改登录名操作,需要登陆
 * 
 * @author lvly
 * @since 2013-3-4
 */
public class LoginNameSecondaryAction extends DispatchAction {
	/**
	 * 显示设置辅助账号界面
	 * */
	public ActionForward setSecondaryEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return mapping.findForward("add.secondaryEmail.show");
	}
	/**
	 * 设置辅助账号
	 **/
	public ActionForward addSecondaryEmail(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response)throws Exception {
		String primaryEmail = request.getParameter("loginName");
		String password = request.getParameter("password");
		String newSecondaryEmail = request.getParameter("newSecondaryEmail");
		if (ServiceFactory.getLoginService(request).passwordRight(
				new UsernamePasswordCredential(primaryEmail, password))){
			if(!ServiceFactory.getLoginNameService(request).isUsedByMe(SessionUtils.getUserId(request), newSecondaryEmail)){
				int loginNameId=ServiceFactory.getLoginNameService(request).createLoginName(newSecondaryEmail, SessionUtils.getUserId(request), LoginNameInfo.LOGINNAME_TYPE_SECONDARY, LoginNameInfo.STATUS_TEMP);
				ServiceFactory.getUserService(request).sendActivicationSecondaryEmail(new UMTContext(request).getLocale(),
					SessionUtils.getUserId(request), newSecondaryEmail, ServiceFactory.getWebUrl(request),
					loginNameId,false);
			}
			request.setAttribute("email", newSecondaryEmail);
			
		} else {
			request.setAttribute("newSecondaryEmail", newSecondaryEmail);
			request.setAttribute("password_error", "security.email.password.error");
			return mapping.findForward("add.secondaryEmail.show");
		}
		response.sendRedirect(ShowPageAction.getSendSuccessUrl(request,newSecondaryEmail,"secondary","accountManage.secondaryMail.add"));
		return null;
	}
	/**
	 * 删除辅助账号，已激活的
	 * */
	public ActionForward deleteSecondary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String loginNameInfoId = request.getParameter("loginNameInfoId");
		String from = request.getParameter("from");
		if (!CommonUtils.isNull(loginNameInfoId)&&CommonUtils.isNumber(loginNameInfoId) && !CommonUtils.isNull(from)) {
			IUserLoginNameService loginNameService = ServiceFactory.getLoginNameService(request);
			loginNameService.removeLoginNameById(Integer.valueOf(loginNameInfoId));
			ServiceFactory.getUserService(request).updateValueByColumn(SessionUtils.getUserId(request),
					"secondary_email", loginNameService.getValidSecondaryEmailStr(SessionUtils.getUserId(request)));
			return mapping.findForward("delete.success." + from);
		} else {
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "delete.error"));
			return null;
		}
	}

	/**
	 * 显示修改辅助油箱，已激活
	 * */
	public ActionForward updateSecondary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		request.setAttribute("loginNameInfoId", request.getParameter("loginNameInfoId"));
		request.setAttribute("email", request.getParameter("email"));
		request.setAttribute("newSecondaryEmail", request.getParameter("newSecondaryEmail"));
		return mapping.findForward("update.secondary.show");
	}

	/**
	 * 修改辅助邮箱
	 * */
	public ActionForward saveSecondary(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String loginName = request.getParameter("loginName");
		String password = request.getParameter("password");
		String newSecondaryEmail = request.getParameter("newSecondaryEmail");
		if (!ServiceFactory.getLoginService(request).passwordRight(new UsernamePasswordCredential(loginName, password))) {
			request.setAttribute("password_error", "security.email.password.error");
			request.setAttribute("newSecondary", newSecondaryEmail);
			return updateSecondary(mapping, form, request, response);
		}
		
		
		String loginNameInfoId=request.getParameter("loginNameInfoId");
		IUserLoginNameService loginNameService=ServiceFactory.getLoginNameService(request);
		LoginNameInfo loginNameInfo=loginNameService.getLoginNameInfoById(Integer.valueOf(loginNameInfoId));
		if(loginNameInfo==null){
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "secondary.deleted"));
			return null;
		}
		if(loginNameInfo.getStatus().equals(LoginNameInfo.STATUS_ACTIVE)){
			loginNameService.updateToLoginName(SessionUtils.getUserId(request),loginNameInfo.getLoginName(), newSecondaryEmail);
		}else{
			loginNameService.updateLoginName(SessionUtils.getUserId(request),loginNameInfo.getLoginName(), newSecondaryEmail);
		}
		ServiceFactory.getUserService(request).sendActivicationSecondaryEmail(new UMTContext(request).getLocale()
				, SessionUtils.getUserId(request)
				, newSecondaryEmail
				, ServiceFactory.getWebUrl(request)
				,loginNameInfo.getId(),
				loginNameInfo.getStatus().equals(LoginNameInfo.STATUS_ACTIVE));
		response.sendRedirect(ShowPageAction.getSendSuccessUrl(request,newSecondaryEmail,"secondary","accountManage.secondaryMail.update"));
		return null;
	}

}
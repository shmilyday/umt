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

import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.ui.Attributes;

/**
 * 账户安全
 * @author lvly
 * @since 2013-1-29
 */
public class UserSafeAction extends DispatchAction{
	/**
	 * 显示账户安全
	 * */
	public ActionForward showSecurity(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		return mapping.findForward("safe.security.show");
	}
	/**
	 * 显示日志
	 * */
	public ActionForward showLog(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		int uid=SessionUtils.getUserId(request);
		request.setAttribute("loginMessage", getAccountService().getTopTenLogByEventType(uid, UMTLog.EVENT_TYPE_LOG_IN));
		request.setAttribute("changeSecurityEmailMessage", getAccountService().getTopTenLogByEventType(uid, UMTLog.EVENT_TYPE_CHANGE_SECURITY_EMAIL));
		request.setAttribute("changePasswordMessage", getAccountService().getTopTenLogByEventType(uid, UMTLog.EVENT_TYPE_CHANGE_PASSWORD));
		return mapping.findForward("safe.log.show");
	}
	private IAccountService getAccountService(){
		return (IAccountService)getBeanFactory().getBean(IAccountService.BEAN_ID);
	}
	private BeanFactory getBeanFactory(){
		return (BeanFactory) getServlet().getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
}
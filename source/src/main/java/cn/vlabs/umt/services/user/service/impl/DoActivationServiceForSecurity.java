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
package cn.vlabs.umt.services.user.service.impl;

import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.service.AbstractDoActivation;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.actions.ActivationForm;
import cn.vlabs.umt.ui.actions.ShowPageAction;

/**
 * @author lvly
 * @since 2013-3-25
 */
public class DoActivationServiceForSecurity extends AbstractDoActivation{

	/**
	 * @param request
	 * @param response
	 * @param token
	 * @param user
	 * @param data
	 */
	public DoActivationServiceForSecurity(HttpServletRequest request, HttpServletResponse response, Token token,
			User user, ActivationForm data) {
		super(request, response, token, user, data);
	}

	@Override
	public ActionForward toError() throws Exception {
		getResponse().sendRedirect(ShowPageAction.getMessageUrl(getRequest(), "active.security.email.fail"));
		return null;
	}

	@Override
	public ActionForward toSuccess() throws Exception {
		getResponse().sendRedirect(ShowPageAction.getMessageUrl(getRequest(), "active.security.email.success"));
		return null;
	}

	@Override
	public ActionForward hasLoginAndIsSelf() throws Exception {
		getTokenService().toUsed(getData().getTokenid());
		getUserService().updateValueByColumn(getToken().getUid(), "security_email", getToken().getContent());
		SessionUtils.setSessionVar(getRequest(), Attributes.USER_TEMP_SECURITY_EMAIL, null);
		ServiceFactory.getLogService(getRequest()).log(UMTLog.EVENT_TYPE_CHANGE_SECURITY_EMAIL, getUser().getId(), RequestUtil.getRemoteIP(getRequest()), RequestUtil.getBrowseType(getRequest()));
		getTokenService().removeTokensUnsed(getToken().getUid(),Token.OPERATION_ACTIVATION_SECURITY_EMAIL);
		return toSuccess();
	}

	@Override
	public ActionForward hasLoginAndNotSelf() throws Exception {
		String rtnUrl=URLEncoder.encode(getSecurityLoginUrl(getRequest(), getUser().getCstnetId(),getToken().getContent(),getData()),"UTF-8");
		String logOutUrl=RequestUtil.getContextPath(getRequest())+"/logout?"+Attributes.RETURN_URL+"="+rtnUrl;
		getResponse().sendRedirect(logOutUrl);
		return null;
	}
	private String getSecurityLoginUrl(HttpServletRequest request,String loginName,String securityEmail,ActivationForm data){
		String result=RequestUtil.getContextPath(request)+"/security/activation.do?act=doLoginSecurity&securityEmail="+securityEmail+"&loginName="+loginName;
		return addFormData(result, data);
	}

	@Override
	public ActionForward notLogin() throws Exception {
		getResponse().sendRedirect(getSecurityLoginUrl(getRequest(), getUser().getCstnetId(),getToken().getContent(),getData()));
		return null;
	}
	
	
}
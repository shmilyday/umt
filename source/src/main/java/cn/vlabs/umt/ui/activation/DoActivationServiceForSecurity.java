/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
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
package cn.vlabs.umt.ui.activation;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;

/**
 * @author lvly
 * @since 2013-3-25
 */
public class DoActivationServiceForSecurity extends AbstractDoActivation {

	/**
	 * @param request
	 * @param response
	 * @param token
	 * @param user
	 * @param data
	 */
	public DoActivationServiceForSecurity(HttpServletRequest request,
			HttpServletResponse response, Token token, User user,
			ActivationForm data) {
		super(request, response, token, user, data);
	}

	@Override
	public String toError() {
		return getMessageUrl("active.security.email.fail");
	}

	@Override
	public String toSuccess() {
		return getMessageUrl("active.security.email.success");
	}

	@Override
	public String hasLoginAndIsSelf() {
		getTokenService().toUsed(getData().getTokenid());
		getUserService().updateValueByColumn(getToken().getUid(),
				"security_email", getToken().getContent());
		SessionUtils.setSessionVar(getRequest(),
				Attributes.USER_TEMP_SECURITY_EMAIL, null);
		ServiceFactory.getLogService(getRequest()).log(
				UMTLog.EVENT_TYPE_CHANGE_SECURITY_EMAIL, getUser().getId(),
				RequestUtil.getRemoteIP(getRequest()),
				RequestUtil.getBrowseType(getRequest()));
		getTokenService().removeTokensUnsed(getToken().getUid(),
				Token.OPERATION_ACTIVATION_SECURITY_EMAIL);
		return toSuccess();
	}

	@Override
	public String hasLoginAndNotSelf() {
		String rtnUrl;
		try {
			rtnUrl = URLEncoder.encode(
					getSecurityLoginUrl(getUser().getCstnetId(),
							getToken().getContent(), getData()), "UTF-8");
			String logOutUrl = "/logout?" + Attributes.RETURN_URL + "="
					+ rtnUrl;
			return "redirect:" + logOutUrl;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return toError();
		}
	}
	private String getSecurityLoginUrl(String loginName, String securityEmail, ActivationForm data) {
		String result = ServiceFactory.getWebUrl(getRequest());
		String rawUrl = composeRawUrl(loginName, securityEmail, data);
		return result + rawUrl;
	}

	private String composeRawUrl(String loginName, String securityEmail,
			ActivationForm data) {
		String rawUrl = "/security/activation.do?act=doLoginSecurity&securityEmail="
				+ securityEmail + "&loginName=" + loginName;
		rawUrl = addFormData(rawUrl, data);
		return rawUrl;
	}

	@Override
	public String notLogin() {
		return "redirect:"
				+ composeRawUrl(getUser().getCstnetId(), getToken()
						.getContent(), getData());
	}

}
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
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
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
public class DoActivationServiceForPrimaryAndSecurity extends AbstractDoActivation{
	private LoginNameInfo nameInfo;
	/**
	 * @param request
	 * @param response
	 * @param token
	 * @param user
	 * @param data
	 */
	public DoActivationServiceForPrimaryAndSecurity(HttpServletRequest request, HttpServletResponse response, Token token,
			User user, ActivationForm data,LoginNameInfo nameInfo) {
		super(request, response, token, user, data);
		this.nameInfo=nameInfo;
	}

	@Override
	public ActionForward toError() throws Exception {
		getResponse().sendRedirect(ShowPageAction.getMessageUrl(getRequest(),  "active.login.email.fail"));
		return null;
	}
	@Override
	public ActionForward toSuccess() throws Exception {
		getResponse().sendRedirect(ShowPageAction.getMessageUrl(getRequest(),  "active.login.email.success"));
		return null;
	}
	
	

	@Override
	public ActionForward hasLoginAndIsSelf() throws Exception {
		getTokenService().toUsed(getToken().getId());
		if(nameInfo==null){
			return toError();
			
		}
		if(getUserService().isUsed(nameInfo.getLoginName())||getToken().getUid()!=nameInfo.getUid()){
			return toError();
		}else{
			getUserService().updateValueByColumn(getToken().getUid(),"cstnet_id", nameInfo.getLoginName());
			getUserService().updateValueByColumn(getToken().getUid(), "security_email", nameInfo.getLoginName());
			ServiceFactory.getLoginNameService(getRequest()).toActive(getData().getLoginNameInfoId());
			getUserService().removeUserExpectMe(nameInfo.getLoginName(), nameInfo.getUid());
		}
		SessionUtils.toActive(getRequest());
		return toSuccess();
	}

	@Override
	public ActionForward hasLoginAndNotSelf() throws Exception {
		String rtnUrl=URLEncoder.encode(getPrimaryLoginUrlOnlyActive(getData(),getRequest(), getUser().getCstnetId(),"activeLoginAndSecurity"),"UTF-8");
		String logOutUrl=RequestUtil.getContextPath(getRequest())+"/logout?"+Attributes.RETURN_URL+"="+rtnUrl;
		getResponse().sendRedirect(logOutUrl);
		return null;
	}
	private String getPrimaryLoginUrlOnlyActive(ActivationForm data,HttpServletRequest request,String oldPrimary,String requestAct){
		return addFormData(RequestUtil.getContextPath(request)+"/primary/activation.do?act=doLoginPrimaryOnlyActive&primaryEmail="+oldPrimary+"&requestAct="+requestAct,data);
	
	}

	@Override
	public ActionForward notLogin() throws Exception {
		getResponse().sendRedirect(getPrimaryLoginUrlOnlyActive(getData(),getRequest(),getUser().getCstnetId(), "activeLoginAndSecurity"));
		return null;
	}

}
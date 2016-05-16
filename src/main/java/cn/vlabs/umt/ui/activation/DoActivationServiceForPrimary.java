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

import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.Attributes;

/**
 * @author lvly
 * @since 2013-3-25
 */
public class DoActivationServiceForPrimary extends AbstractDoActivation{
	private LoginNameInfo nameInfo;
	@Override
	public String toSuccess() {
		return getMessageUrl("active.login.email.success");
	}
	/**
	 * @param request
	 * @param response
	 * @param token
	 * @param user
	 * @param data
	 */
	public DoActivationServiceForPrimary(HttpServletRequest request, HttpServletResponse response, Token token,
			User user, ActivationForm data,LoginNameInfo nameInfo) {
		super(request, response, token, user, data);
		this.nameInfo=nameInfo;
	}

	@Override
	public String toError() {
		return getMessageUrl("active.login.email.fail");
		
	}

	@Override
	public String hasLoginAndIsSelf() {
		getTokenService().toUsed(getToken().getId());
		if(getData().isChangeLoginName()){
			getRequest().setAttribute("_oldEmail", nameInfo.getLoginName());
			getRequest().setAttribute("_newPrimary", getToken().getContent());
			return "/activation/login_name_step3_check";
		}else{
			if(getUserService().isUsed(nameInfo.getLoginName())!=UserService.USER_NAME_UNUSED){
				return toError();
			}else{
				getUserService().updateValueByColumn(getToken().getUid(),"cstnet_id", nameInfo.getLoginName());
				getLoginNameService().toActive(getData().getLoginNameInfoId());
				getUserService().removeUserExpectMe(nameInfo.getLoginName(), nameInfo.getUid());
			}
		}
		SessionUtils.toActive(getRequest());
		return toSuccess();
		
	}

	@Override
	public String hasLoginAndNotSelf() {
		String rtnUrl;
		try {
			rtnUrl = URLEncoder.encode(getPrimaryLoginUrlOnlyActive(getData(), getUser().getCstnetId(),"activeLogin"),"UTF-8");
			String logOutUrl="/logout?"+Attributes.RETURN_URL+"="+rtnUrl;
			return "redirect:"+logOutUrl;
		} catch (UnsupportedEncodingException e) {
			return toError();
		}
	}

	@Override
	public String notLogin() {
		return "redirect:"+getPrimaryLoginUrlOnlyActive(getData(),getUser().getCstnetId(), "activeLogin");
	}
	private String getPrimaryLoginUrlOnlyActive(ActivationForm data,String oldPrimary,String requestAct){
		return addFormData("/primary/activation.do?act=doLoginPrimaryOnlyActive&primaryEmail="+oldPrimary+"&requestAct="+requestAct,data);
	
	}
	
}
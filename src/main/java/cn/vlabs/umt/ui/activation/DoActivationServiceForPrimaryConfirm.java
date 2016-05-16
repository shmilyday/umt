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
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.Attributes;

/**
 * @author lvly
 * @since 2013-3-25
 */
public class DoActivationServiceForPrimaryConfirm extends AbstractDoActivation{
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
	public DoActivationServiceForPrimaryConfirm(HttpServletRequest request, HttpServletResponse response, Token token,
			User user, ActivationForm data) {
		super(request, response, token, user, data);
	}

	@Override
	public String toError(){
		return getMessageUrl("active.login.email.fail");
		
	}

	@Override
	public String hasLoginAndIsSelf(){
		return "/user/primay/login_name_step2";
	}

	@Override
	public String hasLoginAndNotSelf() {
		String rtnUrl;
		try {
			rtnUrl = URLEncoder.encode(getPrimaryLoginUrl(getRequest(), getUser().getCstnetId()),"UTF-8");
			String logOutUrl=RequestUtil.getContextPath(getRequest())+"/logout?"+Attributes.RETURN_URL+"="+rtnUrl;
			return "redirect:"+logOutUrl;
		} catch (UnsupportedEncodingException e) {
			return toError();
		}
	}

	@Override
	public String notLogin() {
		return "redirect:"+getPrimaryLoginUrl(getRequest(),getUser().getCstnetId());
	}
	
	private String getPrimaryLoginUrl(HttpServletRequest request,String oldPrimary){
		return RequestUtil.getContextPath(request)+"/primary/activation.do?act=doLoginPrimary&primaryEmail="+oldPrimary;
	}
	
	
}
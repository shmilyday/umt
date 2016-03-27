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

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;

/**
 * @author lvly
 * @since 2013-3-25
 */
public class DoActivationServiceForSecondary extends AbstractDoActivation{
	/**
	 * @param request
	 * @param response
	 * @param token
	 * @param user
	 * @param data
	 */
	public DoActivationServiceForSecondary(HttpServletRequest request, HttpServletResponse response, Token token,
			User user, ActivationForm data){
		super(request, response, token, user, data);
	}
	@Override
	public String toError(){
		return getMessageUrl("active.login.email.fail");
	}
	@Override
	public String toSuccess(){
		return getMessageUrl("active.login.email.success");
	}

	@Override
	public String hasLoginAndIsSelf(){
		getTokenService().toUsed(getData().getTokenid());
		IUserLoginNameService loginNameService=ServiceFactory.getLoginNameService(getRequest());
		LoginNameInfo info=loginNameService.getLoginNameInfoById(getData().getLoginNameInfoId());
		if(info==null){
			return toError();
		}
		if(getData().isChangeLoginName()){
			if(info.getStatus().equals(LoginNameInfo.STATUS_ACTIVE)){
				if(CommonUtils.isNull(info.getTmpLoginName())||getUserService().isUsed(info.getTmpLoginName())!=UserService.USER_NAME_UNUSED){
					return toError();
				}
				loginNameService.updateLoginName(getToken().getUid(), info.getLoginName(), info.getTmpLoginName());
				loginNameService.updateToLoginName(getToken().getUid(), info.getLoginName(), null);
				loginNameService.toActive(info.getId());
			}else{
				if(info.getLoginName().equals(getToken().getContent())){
					loginNameService.toActive(info.getId());
				}else{
					return toError();
				}
			}
		}else{
			if(info.getStatus().equals(LoginNameInfo.STATUS_ACTIVE)||!info.getLoginName().equals(getToken().getContent())){
				return toError();
				
			}
			loginNameService.toActive(info.getId());
		}
		getUserService().updateValueByColumn(getUser().getId(), "secondary_email", loginNameService.getValidSecondaryEmailStr(getUser().getId()));
		getTokenService().toUsed(getToken().getId());
		return toSuccess();
	}

	@Override
	public String hasLoginAndNotSelf(){
		String rtnUrl;
		try {
			rtnUrl = URLEncoder.encode(getSecondaryLoginUrl(getUser().getCstnetId(),getData()),"UTF-8");
			String logOutUrl="/logout?"+Attributes.RETURN_URL+"="+rtnUrl;
			return "redirect:"+logOutUrl;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return toError();
		}
	}
	
	/**
	 * 获得辅助账号激活登陆页面
	 * */
	private String getSecondaryLoginUrl(String loginName,ActivationForm data){
		String rawUrl = composeRawUrl(loginName, data);
		return ServiceFactory.getWebUrl(getRequest())+rawUrl;
	}
	private String composeRawUrl(String loginName, ActivationForm data) {
		String rawUrl = "/secondary/activation.do?act=doLoginSecondary&primaryEmail="+loginName;
		rawUrl = addFormData(rawUrl, data);
		return rawUrl;
	}
	@Override
	public String notLogin(){
		return "redirect:"+composeRawUrl(getUser().getCstnetId(),getData());
	}

}
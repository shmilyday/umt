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
package cn.vlabs.umt.ui.tags;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.service.IAppAccessService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 刷新session里的用户信息，也就是去数据库查一遍
 * @author lvly
 * @since 2013-02-04
 * */
public class RefreshUserTag extends TagSupport {
	public int doStartTag() throws JspException {
		HttpServletRequest request=(HttpServletRequest)pageContext.getRequest();
		int uid=SessionUtils.getUserId(request);
		User user=getUserService(request).getUserByUid(uid);
		if(user!=null){
			UMTContext.getLoginInfo(request.getSession()).setUser(user);
			SessionUtils.setSessionVar(request,Attributes.USER_TEMP_SECURITY_EMAIL, getUserService(request).getTempSecurityEmail(user.getId()));
			SessionUtils.setSessionVar(request,Attributes.IS_USER_LOGIN_ACTIVE, getUserService(request).isActivePrimaryEmail(user.getCstnetId()));
			IUserLoginNameService loginNameService=ServiceFactory.getLoginNameService(request);
			SessionUtils.setSessionVar(request, Attributes.USER_PRIMARY_EMAIL,CommonUtils.first(loginNameService.getLoginNameInfo(uid, LoginNameInfo.LOGINNAME_TYPE_PRIMARY)));
			List<LoginNameInfo> secondaryEmails=loginNameService.getLoginNameInfo(uid, LoginNameInfo.LOGINNAME_TYPE_SECONDARY);
			SessionUtils.setSessionVar(request, Attributes.USER_SECONDARY_EMAIL,secondaryEmails);
			SessionUtils.setSessionVar(request, Attributes.MY_APP_LIST, getAccessService(request).getMyAppAccesses(user.getId()));
		}
		return EVAL_BODY_INCLUDE;
	}
	private IAppAccessService getAccessService(HttpServletRequest request){
		return ServiceFactory.getAppAccessService(request);
	}
	private UserService getUserService(HttpServletRequest request){
		return ServiceFactory.getUserService(request);
	}

	private static final long serialVersionUID = 145678765434567L;
}
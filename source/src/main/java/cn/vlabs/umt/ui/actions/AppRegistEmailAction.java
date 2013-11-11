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

import cn.vlabs.umt.common.util.APPUrlUtils;
import cn.vlabs.umt.common.util.CommonUtils;

/**
 * @author lvly
 * @since 2013-1-16
 */
public class AppRegistEmailAction extends DispatchAction{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		AppRegistEmailForm data=(AppRegistEmailForm)form;
		request.setAttribute("isFromApp", isFromApp(data));
		String loginUrl=data.getLoginURL();
		String appName=data.getAppname();
		APPUrlUtils.setURLtoRequest(request);
		request.setAttribute("loginURL", loginUrl);
		request.setAttribute("registURL", data.getRegistURL());
		request.setAttribute("appName", appName);
		return mapping.getInputForward();
	}
	private boolean isFromApp(AppRegistEmailForm data){
		return !CommonUtils.isNull(data.getAppname())&&!CommonUtils.isNull(data.getLoginURL())&&!CommonUtils.isNull(data.getRegistURL());
	}
}
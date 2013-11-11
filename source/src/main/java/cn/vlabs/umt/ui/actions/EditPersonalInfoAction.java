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

import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 更改自己的资料
 * @author lvly
 * @since 2013-1-28
 */
public class EditPersonalInfoAction extends DispatchAction{
	/**
	 * 更改displayName
	 * */
	public ActionForward updateTrueName(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		int uid=new UMTContext(request).getCurrentUMTUser().getId();
		getUserService().updateValueByColumn(uid, "truename", request.getParameter("truename"));
		return null;
	}
	
	private UserService getUserService() {
		return (UserService) getBeanFactory().getBean(UserService.BEAN_ID);
	}
	
	private BeanFactory getBeanFactory(){
		return (BeanFactory) getServlet().getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
}
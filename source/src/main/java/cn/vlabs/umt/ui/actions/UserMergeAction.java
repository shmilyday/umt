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

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.utils.ServiceFactory;

/**
 * 账户合并
 * @author lvly
 * @since 2013-2-4
 */
public class UserMergeAction extends DispatchAction {
	/**
	 * 显示合并页面
	 * */
	public ActionForward show(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		//检查是否是该用户真的需要合并？
		User user=SessionUtils.getUser(request);
		if(user.getType().equals(User.USER_TYPE_MAIL_AND_UMT)){
			return mapping.findForward("user.merge.show");
		}else{
			response.sendRedirect(ShowPageAction.getMessageUrl(request, "passport.merge.can.not"));
			return null;
		}
		
	}
	/**
	 * 合并用户
	 * */
	public ActionForward merge(ActionMapping mapping, ActionForm form, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String username=SessionUtils.getUser(request).getCstnetId();
		String password=request.getParameter("password");
		boolean flag=ICoreMailClient.getInstance().authenticate(username,password).isSuccess();
		if(flag){
			return success(request,response);
		}else{
			request.setAttribute("password_error", "current.password.error");
			return mapping.findForward("user.merge.show");
		}
	}
	private ActionForward success(HttpServletRequest request,HttpServletResponse response)throws Exception{
		ServiceFactory.getUserService(request).updateValueByColumn(SessionUtils.getUserId(request), "type", User.USER_TYPE_CORE_MAIL);
		response.sendRedirect(RequestUtil.addParam(RequestUtil.getContextPath(request)+"/index.jsp", "msg", "passport.merge.success"));
		return null;
	}
}
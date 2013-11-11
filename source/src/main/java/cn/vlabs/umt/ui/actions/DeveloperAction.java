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

import java.io.PrintWriter;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RandomUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 开发者平台
 * @author lvly
 * @since 2013-7-4
 */
public class DeveloperAction extends DispatchAction{
	/**
	 * 显示我自己申请过的oAuth参数
	 * */
	public ActionForward display(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response){
		List<OauthClientBean> params=getOauthService(request).findByUid(SessionUtils.getUserId(request));
		request.setAttribute("oauths", params);
		return mapping.findForward("developer.display");
		
	}
	/**
	 * 保存Oauth参数
	 * */
	public ActionForward addOauth(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		OauthClientBean bean=getOauthClientFromRequest(request);
		getOauthService(request).save(bean,false);
		UMTContext context=new UMTContext(request);
		getOauthService(request).sendAddMailtoAmin(context.getLocale(), bean, context.getCurrentUMTUser());
		response.sendRedirect(request.getContextPath()+"/user/developer.do?act=display");
		return null;
	}
	/**
	 * 保存Oauth参数
	 * */
	public ActionForward updateOauth(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		OauthClientBean beanAfter=getOauthClientFromRequest(request);
		OauthClientBean beanOrg=getOauthService(request).findById(beanAfter.getId());
		
		boolean isRedirectURIChanged=!beanOrg.getRedirectURI().equals(beanAfter.getRedirectURI());
		boolean isIndexChanged=!beanOrg.getClientWebsite().equals(beanAfter.getClientWebsite());
		boolean isAppTypChanged=!beanOrg.getAppType().equals(beanAfter.getAppType());
		if(isIndexChanged||isRedirectURIChanged||isAppTypChanged){
			beanAfter.setStatus(OauthClientBean.STATUS_APPLY);
			UMTContext context=new UMTContext(request);
			getOauthService(request).sendUpdateMailtoAmin(context.getLocale(), beanOrg, beanAfter, context.getCurrentUMTUser());
		}else{
			beanAfter.setStatus(beanOrg.getStatus());
		}
		getOauthService(request).updateDevelop(beanAfter);
		response.sendRedirect(request.getContextPath()+"/user/developer.do?act=display");
		
		return null;
	}
	/**删除oauth，参数*/
	public ActionForward deleteOauth(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)throws Exception{
		getOauthService(request).delete(Integer.parseInt(request.getParameter("id")));
		response.setContentType("json");
		PrintWriter writer = response.getWriter();
		writer.println(true);
		writer.flush();
		writer.close();
		return null;
	}
	
	public OauthClientBean getOauthClientFromRequest(HttpServletRequest request){
		OauthClientBean bean=new OauthClientBean();
		bean.setApplicant(request.getParameter("applicant"));
		bean.setClientName(request.getParameter("clientName"));
		bean.setClientWebsite(request.getParameter("clientWebsite"));
		bean.setRedirectURI(request.getParameter("redirectURI"));
		bean.setDescription(request.getParameter("description"));
		bean.setCompany(request.getParameter("company"));
		bean.setContactInfo(request.getParameter("contactInfo"));
		bean.setUid(SessionUtils.getUserId(request));
		bean.setClientId(RandomUtil.randomInt(5));
		bean.setClientSecret(RandomUtil.random(32));
		bean.setStatus(OauthClientBean.STATUS_APPLY);
		bean.setAppType(request.getParameter("appType"));
		String idStr=request.getParameter("id");
		if(!CommonUtils.isNull(idStr)){
			bean.setId(Integer.parseInt(idStr));
		}
		return bean;
	}
	private IOauthClientService getOauthService(HttpServletRequest request){
		return (IOauthClientService)ServiceFactory.getBean(request, IOauthClientService.BEAN_ID);
	}
}
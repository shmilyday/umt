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
package cn.vlabs.umt.ui.admin;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.service.ILdapService;
import cn.vlabs.umt.ui.UMTContext;
@Controller
@RequestMapping("/admin/ldap.do")
public class AdminLdapController {
	@Autowired
	private ILdapService ldapService;
	private LdapBean extractFromRequest(HttpServletRequest request){
		LdapBean bean = new LdapBean();
			String idStr=request.getParameter("id");
			if(!CommonUtils.isNull(idStr)){
				bean.setId(Integer.parseInt(idStr));
			}
			bean.setRdn(request.getParameter("rdn"));
			bean.setClientName(request.getParameter("clientName"));
			bean.setDescription(request.getParameter("description"));
			bean.setCompany(request.getParameter("company"));
			bean.setApplicant(request.getParameter("applicant"));
			bean.setContactInfo(request.getParameter("contactInfo"));
			bean.setPubScope(request.getParameter("pubScope"));
			String priv=request.getParameter("priv");
			if(!CommonUtils.isNull(priv)){
				bean.setPriv(priv);
			}
			bean.setType(request.getParameter("appType"));
			String appStatus=request.getParameter("appStatus");
			if(!CommonUtils.isNull(appStatus)){
				bean.setAppStatus(appStatus);
			}
			
			
			UMTContext context=new UMTContext(request);
			bean.setUid(context.getCurrentUMTUser().getId());
			bean.setUserName(context.getCurrentUMTUser().getTrueName());
			bean.setUserCstnetId(context.getCurrentUMTUser().getCstnetId());
			return bean;
	}
	@RequestMapping
	public String display(HttpServletRequest request,
			HttpServletResponse response) {
		List<LdapBean> ldapApps = ldapService.findAllApp();
		request.setAttribute("ldaps", ldapApps);
		return "/admin/ldap_beans_nav";
	}
	@RequestMapping(params = "act=getDetail")
	public void getDetail(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		int beanId = Integer.parseInt(request.getParameter("id"));
		LdapBean lb = ldapService.getLdapBeanById(beanId);
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().print(lb.toJson());
	}
	@RequestMapping(params = "act=saveLdap")
	public String saveLdap(HttpServletRequest request) {
		LdapBean bean = extractFromRequest(request);
		ldapService.updateByAdmin(bean);
		return "redirect:/admin/ldap.do?act=display";
	}
	@RequestMapping(params = "act=deleteLdap")
	public String deleteLdap(HttpServletRequest request,
			HttpServletResponse response) {
		int beanId = Integer.parseInt(request.getParameter("id"));
		ldapService.removeLdapApp(beanId);
		return "redirect:/admin/ldap.do?act=display";
	}

}

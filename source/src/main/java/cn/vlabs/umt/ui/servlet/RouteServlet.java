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
package cn.vlabs.umt.ui.servlet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.vmt.api.IRestOrgService;
import net.duckling.vmt.api.impl.OrgService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.rest.ServiceException;
import cn.vlabs.umt.common.FirstNameGraphicsUtils;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.service.IDomainService;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.ui.Attributes;

/**
 * @author lvly
 * @since 2013-9-13
 */
public class RouteServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4979947905914747000L;
	private static final Logger LOGGER = Logger.getLogger(RouteServlet.class);
	private IDomainService domainService;
	private RoleService roleService;
	private IOauthClientService clientService;
	private Config config;
	public RouteServlet() {
		super();
	}

	public void init() {
		BeanFactory factory = (BeanFactory) getServletContext().getAttribute(
				Attributes.APPLICATION_CONTEXT_KEY);
		domainService=(IDomainService)factory.getBean(IDomainService.BEAN_ID);
		roleService=(RoleService)factory.getBean(RoleService.BEAN_ID);
		config=(Config)factory.getBean(Config.BEAN_ID);
		clientService=(IOauthClientService)factory.getBean(IOauthClientService.BEAN_ID);
	}

	@Override
	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String to = request.getParameter("to");
		if (CommonUtils.isNull(to)) {
			response.getWriter().print("what do you wanna do?");
			response.setStatus(403);
			return;
		}
		User user=SessionUtils.getUser(request);
		if(!roleService.isMemberOf("admin", user.getId())){
			response.getWriter().print("what do you wanna do?<a href='logout'>logout</a>");
			response.setStatus(403);
			return;
		}
		
		if("sync".equals(to)){
			try {
				sync(request,response);
			} catch (ServiceException e) {
				LOGGER.error(e.getMessage(),e);
			}
		}else if("oauthLogo".equals(to)){
			oauthLogo(request, response);
		}
	}
	public void oauthLogo(HttpServletRequest request,HttpServletResponse response) throws IOException{
		List<OauthClientBean> list=clientService.getAll();
		for(OauthClientBean bean:list){
			FirstNameGraphicsUtils graphics=new FirstNameGraphicsUtils();
			File tmpFile=File.createTempFile(System.currentTimeMillis()+".fn.", ".png");
			graphics.generate(bean.getClientName(),new FileOutputStream(tmpFile));
			clientService.uploadLogoDefault(bean, tmpFile);
		}
	}
	
	public void sync(HttpServletRequest request,HttpServletResponse response) throws ServiceException, IOException{
		IRestOrgService orgService=new OrgService(config.getStringProp("vmt.api.url", ""));
		domainService.insertOrgDomain(orgService.getAllDomains());
		response.getWriter().print("success!");
		return;
		
	}

}
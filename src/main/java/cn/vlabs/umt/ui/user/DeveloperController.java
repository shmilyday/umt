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
package cn.vlabs.umt.ui.user;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.EmailUtil;
import cn.vlabs.umt.common.FirstNameGraphicsUtils;
import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.RandomUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.AppSecret;
import cn.vlabs.umt.services.user.bean.LdapBean;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.LogoUploadResult;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.service.IAppSecretService;
import cn.vlabs.umt.services.user.service.ILdapService;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 开发者平台
 * 
 * @author lvly
 * @since 2013-7-4
 */
@RequestMapping("/user/developer.do")
@Controller
public class DeveloperController {
	private static final Logger LOG = Logger.getLogger(DeveloperController.class);
	@Autowired
	private IOauthClientService clientService;
	@Autowired
	private ILdapService ldapService;
	@Autowired
	private IUserLoginNameService loginNameService;
	@Autowired
	private IAppSecretService sService;
	@Autowired
	private UserService us;
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
	/**
	 * 新建ldap应用
	 * */
	@RequestMapping(params = "act=addLdapApp")
	public void addLdapApp(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		LdapBean bean = extractFromRequest(request);
		if (ldapService.isRdnUsed(bean.getRdn())) {
			LOG.error("rdn[" + bean.getRdn() + "] is used!");
			return;
		}
		UMTContext context = new UMTContext(request);
		ldapService.addLdapApp(bean);
		ldapService.sendMailToSuperAdmin(context.getLocale(), bean,
				context.getCurrentUMTUser());
	}

	/**
	 * 保存Oauth参数
	 * 
	 * @throws MailException
	 * */
	@RequestMapping(params = "act=addOauth")
	public String addOauth(HttpServletRequest request,
			HttpServletResponse response) throws IOException, MailException {
		OauthClientBean bean = getOauthClientFromRequest(request);
		UMTContext context = new UMTContext(request);
		int currUid = context.getCurrentUMTUser().getId();
		if (currUid != bean.getUid()) {
			LOG.error("why do you(" + currUid
					+ ") want to add other people's oauth(" + bean.getId()
					+ ")?");
			return null;
		}

		OauthClientBean dbBean = clientService.findByClientId(bean
				.getClientId());
		if (dbBean != null) {
			LOG.error("why do you(" + currUid + ") want to add repeat oauth("
					+ bean.getClientId() + ")?");
			return null;
		}
		clientService.save(bean, false);
		FirstNameGraphicsUtils graphics = new FirstNameGraphicsUtils();
		File tmpFile = File.createTempFile(System.currentTimeMillis() + ".fn.",
				"png");
		graphics.generate(bean.getClientName(), new FileOutputStream(tmpFile));
		clientService.uploadLogoDefault(bean, tmpFile);
		clientService.sendAddMailtoAmin(context.getLocale(), bean,
				context.getCurrentUMTUser());
		return "redirect:/user/developer.do?act=display";
	}

	@RequestMapping(params = "act=deleteLdap")
	public String deleteLdap(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		int beanId = Integer.parseInt(request.getParameter("id"));
		UMTContext context = new UMTContext(request);
		ldapService.removeLdapApp(beanId, context.getCurrentUMTUser().getId());
		return "redirect:/user/developer.do?act=display&viewType=ldap";
	}

	/***
	 * 删除某个账号
	 * 
	 * @throws IOException
	 * */
	@RequestMapping(params = "act=deleteMember")
	public String deleteMember(HttpServletRequest request,
			HttpServletResponse response) {
		String appId = request.getParameter("ldapId");
		String secretId = request.getParameter("secretId");
		LdapBean bean = ldapService.getLdapBeanById(Integer.parseInt(appId));
		if (bean == null) {
			return null;
		}
		if (bean.getUid() != SessionUtils.getUserId(request)) {
			return null;
		}
		int sId = Integer.parseInt(secretId);
		AppSecret appSecret = sService.findAppSecretById(sId);
		User u =  us.getUserByUid(appSecret.getUid());
		String loginName;
		if (bean.isWifiApp()){
			loginName=EmailUtil.extractName(u.getCstnetId());
		}else{
			LoginNameInfo lni = loginNameService.getLdapLoginName(appSecret
					.getUid());
			loginName = lni.getLoginName();
		}
		sService.deleteMember(bean.getRdn(), loginName, sId);
		sService.sendToMember(bean,u, "delete");
		return "redirect:/user/developer.do?act=showMember&ldapId=" + appId;
	}
	/**
	 * 删除oauth，参数
	 * 
	 * @throws IOException
	 */
	@RequestMapping(params = "act=deleteOauth")
	public void deleteOauth(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		int beanId = Integer.parseInt(request.getParameter("id"));
		OauthClientBean bean = clientService.findById(beanId);
		UMTContext context = new UMTContext(request);
		int currUid = context.getCurrentUMTUser().getId();
		if (currUid != bean.getUid()) {
			LOG.error("why do you(" + currUid
					+ ") want to delete other people's oauth(" + bean.getId()
					+ ")?");
			return;
		}
		clientService.delete(beanId);
		response.setContentType("text/plain");
		PrintWriter writer = response.getWriter();
		writer.println(true);
		writer.flush();
		writer.close();
	}

	/**
	 * 显示我自己申请过的oAuth参数
	 * */
	@RequestMapping(params = "act=display")
	public String display(HttpServletRequest request,
			HttpServletResponse response) {
		String viewType = request.getParameter("viewType");
		if (CommonUtils.isNull(viewType)) {
			viewType = OauthClientBean.APP_TYPE_WEB_APP;
		}
		request.setAttribute("viewType", viewType);
		int currentUserId = SessionUtils.getUserId(request);
		switch (viewType) {
			case OauthClientBean.APP_TYPE_PHONE_APP : {
				List<OauthClientBean> params = clientService.findByUid(currentUserId, viewType);
				request.setAttribute("oauths", params);
				return "/user/developer_oauth_display";
			}
			case OauthClientBean.APP_TYPE_WEB_APP : {
				List<OauthClientBean> params = clientService.findByUid(currentUserId, viewType);
				request.setAttribute("oauths", params);
				return "/user/developer_oauth_display";
			}
			case "ldap" : {
				List<LdapBean> beans = ldapService.searchMyLdapApp(currentUserId);
				request.setAttribute("ldaps", beans);
				return "/user/developer_ldap_display";
			}
			case "wifi" : {
				List<LdapBean> beans = ldapService.searchMyWifiApps(currentUserId);
				request.setAttribute("ldaps", beans);
				return "/user/developer_ldap_display";
			}
		}
		return null;

	}

	@RequestMapping(params = "act=getLdapApp")
	public void getLdapApp(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		LdapBean bean = ldapService.getLdapBeanById(Integer.parseInt(request
				.getParameter("ldapId")));
		UMTContext context = new UMTContext(request);
		if (UMTContext.isAdminUser(request)
				|| bean.getUid() == context.getCurrentUMTUser().getId()) {
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
			response.getWriter().print(bean.toJson());
		}
	}

	private OauthClientBean getOauthClientFromRequest(HttpServletRequest request) {
		OauthClientBean bean = new OauthClientBean();
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
		bean.setEnableAppPwd(request.getParameter("enableAppPwd"));
		String idStr = request.getParameter("id");
		if (!CommonUtils.isNull(idStr)) {
			bean.setId(Integer.parseInt(idStr));
		}
		return bean;
	}

	/**
	 * 查看应用名称是否已被使用
	 * */
	@RequestMapping(params = "act=isLdapAppNameUsed")
	public void isLdapAppNameUsed(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String rdn = CommonUtils.trim(request.getParameter("rdn"));
		response.getWriter().print(!ldapService.isRdnUsed(rdn));
	}

	/**
	 * 启用某个用户
	 * */
	@RequestMapping(params = "act=openMember")
	public String openMember(HttpServletRequest request,
			HttpServletResponse response) {
		String appId = request.getParameter("ldapId");
		String secretId = request.getParameter("secretId");
		LdapBean bean = ldapService.getLdapBeanById(Integer.parseInt(appId));
		if (bean == null) {
			return null;
		}
		if (bean.getUid() != SessionUtils.getUserId(request)) {
			return null;
		}
		int sId = Integer.parseInt(secretId);
		AppSecret appSecret = sService.findAppSecretById(sId);
		User u = us.getUserByUid(appSecret.getUid());
		
		String loginName;
		if (bean.isWifiApp()){
			loginName =EmailUtil.extractName(u.getCstnetId());
		}else{
			LoginNameInfo lni = loginNameService.getLdapLoginName(appSecret.getUid());
			loginName=lni.getLoginName();
		}
		sService.openMember(bean, appSecret,loginName);
		sService.sendToMember(bean, u, "pass");
		return "redirect:/user/developer.do?act=showMember&ldapId=" + appId;
	}

	/**
	 * 显示LDAP成员列表
	 * 
	 * */
	@RequestMapping(params = "act=showMember")
	public String showMember(HttpServletRequest request,
			HttpServletResponse response) {
		String ldapId = request.getParameter("ldapId");
		LdapBean bean = ldapService.getLdapBeanById(Integer.parseInt(ldapId));
		if (bean == null) {
			return null;
		}
		if (bean.getUid() != SessionUtils.getUserId(request)) {
			return null;
		}
		List<AppSecret> secrets = sService.findMyAppMember(ldapId);
		request.setAttribute("secrets", secrets);
		request.setAttribute("bean", bean);
		return "/user/show_member_list";
	}

	/**
	 * 更新ldap应用
	 * */
	@RequestMapping(params = "act=updateLdapApp")
	public void updateLdapApp(HttpServletRequest request,
			HttpServletResponse response) {
		LdapBean bean = extractFromRequest(request);
		ldapService.updateByAppAdmin(bean);
	}

	/**
	 * 保存Oauth参数
	 * 
	 * @throws IOException
	 * @throws MailException
	 * */
	@RequestMapping(params = "act=updateOauth")
	public String updateOauth(HttpServletRequest request,
			HttpServletResponse response) throws IOException, MailException {
		OauthClientBean beanAfter = getOauthClientFromRequest(request);
		OauthClientBean beanOrg = clientService.findById(beanAfter.getId());
		UMTContext context = new UMTContext(request);
		int currUid = context.getCurrentUMTUser().getId();
		if (beanOrg.getUid() != currUid) {
			LOG.error("why do you(" + currUid
					+ ") want to update other people's oauth("
					+ beanOrg.getUid() + ")?");
			return null;
		}
		if (!beanOrg.getClientName().equals(beanAfter.getClientName())) {
			FirstNameGraphicsUtils graphics = new FirstNameGraphicsUtils();
			File tmpFile = File.createTempFile(System.currentTimeMillis()
					+ ".fn.", "png");
			graphics.generate(beanAfter.getClientName(), new FileOutputStream(
					tmpFile));
			clientService.updateDefaultLogoChange(beanAfter, tmpFile);
		}
		boolean isRedirectURIChanged = !beanOrg.getRedirectURI().equals(
				beanAfter.getRedirectURI());
		boolean isIndexChanged = !beanOrg.getClientWebsite().equals(
				beanAfter.getClientWebsite());
		boolean isAppTypChanged = !beanOrg.getAppType().equals(
				beanAfter.getAppType());
		if (isIndexChanged || isRedirectURIChanged || isAppTypChanged) {
			beanAfter.setStatus(OauthClientBean.STATUS_APPLY);

			clientService.sendUpdateMailtoAmin(context.getLocale(), beanOrg,
					beanAfter, context.getCurrentUMTUser());
		} else {
			beanAfter.setStatus(beanOrg.getStatus());
		}
		clientService.updateDevelop(beanAfter);
		return "redirect:/user/developer.do?act=display";
	}
	private File uploadFile(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// 判断提交过来的表单是否为文件上传菜单
		boolean isMultipart = ServletFileUpload.isMultipartContent(request);
		if (isMultipart) {
			// 构造一个文件上传处理对象
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			Iterator<?> items;
			try {
				// 解析表单中提交的所有文件内容
				items = upload.parseRequest(request).iterator();
				while (items.hasNext()) {
					FileItem item = (FileItem) items.next();
					if (!item.isFormField()) {
						// 上传文件
						File uploaderFile = File.createTempFile(
								System.currentTimeMillis() + ".tmp.", ".jpg");
						item.write(uploaderFile);
						return uploaderFile;
					}
				}
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
				return null;
			}

		} else {
			File file = File
					.createTempFile(System.currentTimeMillis() + "", "");
			IOUtils.copy(request.getInputStream(), new FileOutputStream(file));
			return file;
		}
		return null;

	}

	/**
	 * 上传文件
	 * 
	 * @throws IOException
	 * @throws ServletException
	 * */
	@RequestMapping(params = "act=uploadImg")
	public void uploadImg(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {
		String target = request.getParameter("target");
		int beanId = Integer.parseInt(request.getParameter("beanId"));
		OauthClientBean bean = clientService.findById(beanId);
		if (bean == null || bean.getUid() != SessionUtils.getUserId(request)) {
			LOG.error("bean is error,not yours or not found[beanId:" + beanId
					+ ",uid:" + bean.getUid() + "]");
			return;
		}
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		response.setCharacterEncoding("UTF-8");
		File tmpFile = uploadFile(request, response);
		LogoUploadResult result = clientService.uploadLogo(bean, tmpFile,
				request.getParameter("qqfile"), target);
		response.getWriter().println(result.toJson());
	}

}
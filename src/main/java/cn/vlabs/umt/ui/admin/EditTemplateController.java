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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.mail.EmailConfig;
import cn.vlabs.umt.common.mail.EmailTemplate;
import cn.vlabs.umt.common.mail.MessageFormatter;
import cn.vlabs.umt.common.mail.TemplateNotFound;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.common.util.PropertyWriter;

@Controller
@RequestMapping("/admin/editTemplate.do")
/**
 * TODO Wait to fix BUG
 *
 */
public class EditTemplateController {
	private static final Logger log = Logger.getLogger(EditTemplateController.class);
	@Autowired
	private Config config;
	
	@RequestMapping(params="act=approve")
	public String approve(HttpServletRequest request, HttpServletResponse response) {
		EmailTemplate email = getEmailTemplate(request, EmailTemplate.TARGET_APPROVE);
		request.setAttribute("template", email);
		return "/admin/emailtemplate";
	}
	@RequestMapping(params="act=deny")
	public String deny(HttpServletRequest request, HttpServletResponse response) {
		EmailTemplate email = getEmailTemplate(request, EmailTemplate.TARGET_DENY);
		request.setAttribute("template", email);
		return "/admin/emailtemplate";
	}
	
	private EmailConfig getEmailConfig() {
		EmailConfig emailConfig = new EmailConfig();
		String smtp = config.getStringProp(EmailTemplate.CONFIG_SMTP, "smtp.cnic.cn");
		String email = config.getStringProp(EmailTemplate.CONFIG_EMAIL, "document@cnic.cn");
		String pass = config.getStringProp(EmailTemplate.CONFIG_PASSWORD, "111111");
		emailConfig.setEmail(email);
		emailConfig.setSmtp(smtp);
		emailConfig.setPassword(pass);
		return emailConfig;	
	}
	private EmailTemplate getEmailTemplate(HttpServletRequest request, String templateName) {
		EmailTemplate email = new EmailTemplate();
		email.setTarget(templateName);
		String templatePath =config.getStringProp(EmailTemplate.TEMPLATE_DIR, "WEB-INF/message/");
		String realPath = request.getServletContext().getRealPath(templatePath);
		MessageFormatter formatter = new MessageFormatter(realPath);
		String content = "";
		String title = "";
		try {
			content = formatter.getContent(Locale.getDefault(), templateName, null);
			title = formatter.getTitle(Locale.getDefault(), templateName, null);
		} catch (TemplateNotFound e) {
			log.error(e.getLocalizedMessage());
		}
		email.setContent(content);
		email.setTitle(title);
		request.setAttribute("act",request.getParameter("act"));
		return email;
	}
	@RequestMapping(params="act=password")
	public String password(HttpServletRequest request, HttpServletResponse response) {
		EmailTemplate email = getEmailTemplate(request, EmailTemplate.TARGET_PASSWORD);
		request.setAttribute("template", email);
		return "/admin/emailtemplate";
	}
	@RequestMapping(params="act=register")
	public String register(HttpServletRequest request, HttpServletResponse response) {
		EmailTemplate email = getEmailTemplate(request, EmailTemplate.TARGET_REGISTER);
		request.setAttribute("template", email);
		return "/admin/emailtemplate";
	}
	@RequestMapping(params="act=saveConfig")
	public String saveConfig(HttpServletRequest request, HttpServletResponse response) {
		
		
		boolean succ = true;
		try {
			String file = request.getServletContext().getRealPath("/") 
					+ "WEB-INF/conf/umt.properties";
			PropertyWriter pw = new PropertyWriter();
			pw.load(new FileInputStream(file));
			pw.setProperty(EmailTemplate.CONFIG_EMAIL, request.getParameter("email"));
			pw.setProperty(EmailTemplate.CONFIG_PASSWORD, request.getParameter("password"));
			pw.setProperty(EmailTemplate.CONFIG_SMTP, request.getParameter("smtp"));
			pw.store(new FileOutputStream(file));
		} catch (Exception e) {
			succ = false;
			log.error(e.getLocalizedMessage());
		}

		EmailConfig emailConfig = new EmailConfig();
		emailConfig.setEmail(request.getParameter("email"));
		emailConfig.setPassword(request.getParameter("password"));
		emailConfig.setSmtp(request.getParameter("smtp"));
		request.setAttribute("config", emailConfig);
		request.setAttribute("tabtype", "parameter");
		if(succ) {
			request.setAttribute("succ", "emailtemp.update.success");
		} else {
			request.setAttribute("succ", "emailtemp.update.error");
		}
		request.setAttribute("act", request.getParameter("reAct"));
		return "/admin/emailtemplate";
	}
	
	@RequestMapping(params="act=saveTemplate")
	public String saveTemplate(HttpServletRequest request, HttpServletResponse response) {
		
		StringBuffer sb = new StringBuffer();
		sb.append(EmailTemplate.SIGN_TITLE).append(request.getParameter("title"));
		sb.append(EmailTemplate.SIGN_CONTENT).append(request.getParameter("content"));

		String templateFileDir = request.getSession().getServletContext().getRealPath("/");
		boolean succ = true;
		try {
			String path = config.getStringProp(EmailTemplate.TEMPLATE_DIR, "WEB-INF/message/");
			String localeddir = path + "/" + Locale.getDefault().toString();
			File f = new File(templateFileDir + localeddir);
			if (!f.exists()){
				localeddir = path + "/zh_CN";
			}
			templateFileDir += localeddir + "/" + request.getParameter("target");
			
			Writer writer = new OutputStreamWriter(new FileOutputStream(templateFileDir), "UTF-8");
			writer.write(sb.toString());
			writer.flush();
			writer.close();
			
		}catch (Exception e) {
			succ = false;
			log.error(e.getLocalizedMessage());
		} 
		
		EmailTemplate email = getEmailTemplate(request, request.getParameter("target"));
		request.setAttribute("template", email);
		if(succ) {
			request.setAttribute("succ", "emailtempt.update.success");
		} else {
			request.setAttribute("succ", "emailtempt.update.error");
		}
		request.setAttribute("act", request.getParameter("reAct"));
		return "/admin/emailtemplate";
	}
	@RequestMapping(params="act=setParameter")
	public String setParameter(HttpServletRequest request, HttpServletResponse response) {
		EmailConfig emailConfig = getEmailConfig();
		request.setAttribute("config", emailConfig);
		request.setAttribute("tabtype", "parameter");
		request.setAttribute("act",request.getParameter("act"));
		return "/admin/emailtemplate";
	}
}
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.mail.EmailConfig;
import cn.vlabs.umt.common.mail.EmailTemplate;
import cn.vlabs.umt.common.mail.MessageFormatter;
import cn.vlabs.umt.common.mail.TemplateNotFound;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.common.util.PropertyWriter;
import cn.vlabs.umt.ui.Attributes;

/** 
 * MyEclipse Struts
 * Creation date: 12-29-2009
 * 
 * XDoclet definition:
 * @struts.action path="/admin/editTemplate" name="editTemplateForm" input="/admin/emailtemplate.jsp" parameter="act" scope="request" validate="true"
 */
public class EditTemplateAction extends DispatchAction {
	public ActionForward setParameter(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		EmailConfig emailConfig = getEmailConfig();
		request.setAttribute("config", emailConfig);
		request.setAttribute("tabtype", "parameter");
		request.setAttribute("act",request.getParameter("act"));
		return mapping.getInputForward();
	}
	
	private EmailConfig getEmailConfig() {
		EmailConfig emailConfig = new EmailConfig();
		Config config = getConfig();
		String smtp = config.getStringProp(EmailTemplate.CONFIG_SMTP, "smtp.cnic.cn");
		String email = config.getStringProp(EmailTemplate.CONFIG_EMAIL, "document@cnic.cn");
		String pass = config.getStringProp(EmailTemplate.CONFIG_PASSWORD, "111111");
		emailConfig.setEmail(email);
		emailConfig.setSmtp(smtp);
		emailConfig.setPassword(pass);
		return emailConfig;	
	}
	
	public ActionForward register(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		EmailTemplate email = getEmailTemplate(request, EmailTemplate.TARGET_REGISTER);
		request.setAttribute("template", email);
		return mapping.getInputForward();
	}
	
	public ActionForward approve(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		EmailTemplate email = getEmailTemplate(request, EmailTemplate.TARGET_APPROVE);
		request.setAttribute("template", email);
		return mapping.getInputForward();
	}
	
	public ActionForward deny(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		EmailTemplate email = getEmailTemplate(request, EmailTemplate.TARGET_DENY);
		request.setAttribute("template", email);
		return mapping.getInputForward();
	}
	
	public ActionForward password(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		EmailTemplate email = getEmailTemplate(request, EmailTemplate.TARGET_PASSWORD);
		request.setAttribute("template", email);
		return mapping.getInputForward();
	}
	
	public ActionForward saveConfig(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		EditTemplateForm etForm = (EditTemplateForm) form;
		
		boolean succ = true;
		try {
			String file = request.getSession().getServletContext().getRealPath("/") 
					+ "WEB-INF/conf/umt.properties";
			PropertyWriter pw = new PropertyWriter();
			pw.load(new FileInputStream(file));
			pw.setProperty(EmailTemplate.CONFIG_EMAIL, etForm.getEmail());
			pw.setProperty(EmailTemplate.CONFIG_PASSWORD, etForm.getPassword());
			pw.setProperty(EmailTemplate.CONFIG_SMTP, etForm.getSmtp());
			pw.store(new FileOutputStream(file));
		} catch (Exception e) {
			succ = false;
			log.error(e.getLocalizedMessage());
		}

		EmailConfig emailConfig = new EmailConfig();
		emailConfig.setEmail(etForm.getEmail());
		emailConfig.setPassword(etForm.getPassword());
		emailConfig.setSmtp(etForm.getSmtp());
		request.setAttribute("config", emailConfig);
		request.setAttribute("tabtype", "parameter");
		if(succ) {
			request.setAttribute("succ", "emailtemp.update.success");
		} else {
			request.setAttribute("succ", "emailtemp.update.error");
		}
		request.setAttribute("act", request.getParameter("reAct"));
		return mapping.getInputForward();
	}
	
	public ActionForward saveTemplate(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		
		EditTemplateForm etForm = (EditTemplateForm) form;
		
		StringBuffer sb = new StringBuffer();
		sb.append(EmailTemplate.SIGN_TITLE).append(etForm.getTitle());
		sb.append(EmailTemplate.SIGN_CONTENT).append(etForm.getContent());

		String templateFileDir = request.getSession().getServletContext().getRealPath("/");
		boolean succ = true;
		try {
			String path = getConfig().getStringProp(EmailTemplate.TEMPLATE_DIR, "WEB-INF/message/");
			String localeddir = path + "/" + Locale.getDefault().toString();
			File f = new File(templateFileDir + localeddir);
			if (!f.exists()){
				localeddir = path + "/zh_CN";
			}
			templateFileDir += localeddir + "/" + etForm.getTarget();
			
			Writer writer = new OutputStreamWriter(new FileOutputStream(templateFileDir), "UTF-8");
			writer.write(sb.toString());
			writer.flush();
			writer.close();
			
		}catch (Exception e) {
			succ = false;
			log.error(e.getLocalizedMessage());
		} 
		
		EmailTemplate email = getEmailTemplate(request, etForm.getTarget());
		request.setAttribute("template", email);
		if(succ) {
			request.setAttribute("succ", "emailtempt.update.success");
		} else {
			request.setAttribute("succ", "emailtempt.update.error");
		}
		request.setAttribute("act", request.getParameter("reAct"));
		return mapping.getInputForward();
	}
	
	private EmailTemplate getEmailTemplate(HttpServletRequest request, String templateName) {
		EmailTemplate email = new EmailTemplate();
		email.setTarget(templateName);
		Config config = getConfig();
		MessageFormatter formatter = new MessageFormatter(config.getMappedPath(EmailTemplate.TEMPLATE_DIR, "WEB-INF/message/"));
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
	
	private Config getConfig(){
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		Config config = (Config)factory.getBean("Config");
		return config;
	}
	
}
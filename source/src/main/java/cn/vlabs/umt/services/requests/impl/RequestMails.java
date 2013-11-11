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
package cn.vlabs.umt.services.requests.impl;

import java.util.Collection;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.log4j.Logger;

import cn.vlabs.umt.common.mail.EmailTemplate;
import cn.vlabs.umt.common.mail.MailException;
import cn.vlabs.umt.common.mail.MessageSender;
import cn.vlabs.umt.services.requests.UserRequest;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.UMTContext;

public class RequestMails {
	public final static String BEAN_ID="RequestEmail";
	public RequestMails(MessageSender sender){
		this.sender=sender;
	}
	public void sendAcceptMail(UserRequest request, UMTContext context){
		Properties prop = new Properties();
		setProperty(prop,"username", request.getUsername());
		setProperty(prop,"truename", request.getTruename());
		setProperty(prop,"orgnization", request.getOrgnization());
		setProperty(prop,"phonenumber", request.getPhonenumber());
		setProperty(prop,"createtime", DateFormatUtils.format(request.getCreateTime(),"yyyy-MM-dd"));
		setProperty(prop,"CurrentDate", DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
		setProperty(prop,"site", context.getSiteURL());
		try {
			sender.send(context.getLocale(), request.getEmail(), EmailTemplate.TARGET_APPROVE, prop);
		} catch (MailException e) {
			log.error(e.getMessage());
			log.debug("information", e);
		}
	}
	public void sendDenyUser(UserRequest request, UMTContext context){
		Properties prop = new Properties();
		setProperty(prop,"username", request.getUsername());
		setProperty(prop,"truename", request.getTruename());
		setProperty(prop,"orgnization", request.getOrgnization());
		setProperty(prop,"phonenumber", request.getPhonenumber());
		setProperty(prop,"createtime", DateFormatUtils.format(request.getCreateTime(),"yyyy-MM-dd"));
		setProperty(prop,"CurrentDate", DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
		
		setProperty(prop,"UserTrueName", request.getTruename());
		
		try {
			sender.send(context.getLocale(), request.getEmail(), EmailTemplate.TARGET_DENY, prop);
		} catch (MailException e) {
			log.error(e.getMessage());
			log.debug("information", e);
		}
	}
	public void sendRegistUser(UserRequest request, UMTContext context){
		Collection<User> admins= UMTContext.getAdminUsers();
		if (admins==null || admins.size()==0){
			log.error("没有找到管理员帐户，无法发送通知邮件。");
			return;
		}
		
		Properties prop = new Properties();
		
		setProperty(prop, "username", request.getUsername());
		setProperty(prop,"truename", request.getTruename());
		setProperty(prop,"orgnization", request.getOrgnization());
		setProperty(prop,"phonenumber", request.getPhonenumber());
		setProperty(prop,"createtime", DateFormatUtils.format(request.getCreateTime(),"yyyy-MM-dd"));
		setProperty(prop,"CurrentDate", DateFormatUtils.format(new Date(),"yyyy-MM-dd"));
		
		String to="";
		boolean first=true;
		for (User u:admins){
			if (first){
				to=u.getCstnetId();
				first=false;
			}else{
				to=to+","+u.getCstnetId();
			}
			
		}
		try {
			sender.send(context.getLocale(), to, EmailTemplate.TARGET_REGISTER, prop);
		} catch (MailException e) {
			log.error(e.getMessage());
			log.debug("information", e);
		}
	}
	
	private void setProperty(Properties prop , String key , String value){
		String v=value;
		if (v==null){
			v="";
		
		}
		prop.setProperty(key, v);
	}
	private static final Logger log = Logger.getLogger(RequestMails.class);
	private MessageSender sender;
}
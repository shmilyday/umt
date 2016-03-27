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
package cn.vlabs.umt.common.mail;

import java.util.Arrays;
import java.util.Locale;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import cn.vlabs.umt.common.util.Config;

public class  MessageSender {
	public MessageSender(Config config) {
		EmailConfig emailConfig = getEmailConfig(config);
		String username = emailConfig.getEmail();
		String password = emailConfig.getPassword();
		this.authenticator = new EmailAuthenticator(username, password);
		this.mailhost = emailConfig.getSmtp();
		this.formatter=new MessageFormatter(config.getMappedPath("mail.temlate.dir", "/WEB-INF/message")); 
		this.mailbox = config.getStringProp("mail.boxname", "document@cnic.cn");
	}
	
	private EmailConfig getEmailConfig(Config config) {
		EmailConfig emailConfig = new EmailConfig();
		String smtp = config.getStringProp(EmailTemplate.CONFIG_SMTP, "smtp.cnic.cn");
		String email = config.getStringProp(EmailTemplate.CONFIG_EMAIL, "document@cnic.cn");
		String pass = config.getStringProp(EmailTemplate.CONFIG_PASSWORD, "111111");
		emailConfig.setEmail(email);
		emailConfig.setSmtp(smtp);
		emailConfig.setPassword(pass);
		return emailConfig;
	}
	public void send(Locale locale, String to, String emailTemplateTarget, Properties parameters) throws MailException {
		send(locale, new String[]{to}, emailTemplateTarget, parameters);
	}
	
	public void send(Locale locale, String[] to, String emailTemplateTarget, Properties parameters) throws MailException {
		if(to==null||to.length==0){
			log.error("the address is null! ");
			return;
		}
		log.info("ready send email to :"+Arrays.toString(to));
		String content = formatter.getContent(locale, emailTemplateTarget, parameters);
		String title = formatter.getTitle(locale, emailTemplateTarget, parameters);
		
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.host", mailhost);
		props.setProperty("mail.transport.protocol", "smtp");
		
		Address[] addressArray=new Address[to.length];
		int index=0;
		for(String str:to){
			try {
				addressArray[index++]=new InternetAddress(str);
			} catch (AddressException e) {
				log.error(e.getMessage(),e);
			}
		}
		Session mailSession = Session.getInstance(props, authenticator);
		MimeMessage mimemessage = new javax.mail.internet.MimeMessage(
				mailSession);
		//设置发信人
		try {
			try{
				mimemessage.setFrom(new InternetAddress(mailbox));
			}catch (AddressException e) {
				log.error("无法识别地址"+mailbox);
				throw new MailConfigError("配置的邮箱地址"+mailbox+"格式错误");
			} 
			//设置收信人
			mimemessage.setRecipients(javax.mail.Message.RecipientType.TO,
					addressArray);
			//设置邮件主题
			mimemessage.setSubject(title, "UTF-8");
			
			//设置邮件内容
			mimemessage.setContent(content, "text/html;charset=UTF-8");
			
			//设置Cheat
			cheat(mimemessage,"localhost");
			
			Transport.send(mimemessage);
			log.info("mail has been sent to "+Arrays.toString(to));
		} catch (MessagingException e) {
			log.error("发送邮件失败.");
			log.error(e.getMessage(),e);
			log.debug("详细信息",e);
		}
	}
    private void cheat(MimeMessage mimeMessage, String serverDomain) throws MessagingException {
        mimeMessage.saveChanges();
        mimeMessage.setHeader("User-Agent", "Thunderbird 2.0.0.16 (Windows/20080708)");
        String messageid = mimeMessage.getHeader("Message-ID", null);
        messageid=messageid.replaceAll("\\.JavaMail.*","@"+serverDomain+">");
        mimeMessage.setHeader("Message-ID", messageid);
    }
    
    private static final class EmailAuthenticator extends Authenticator
    {
        private final PasswordAuthentication m_passwordAuthentication;

        private EmailAuthenticator( String userid, String password )
        {
            m_passwordAuthentication = new PasswordAuthentication(userid, password);
        }

        protected PasswordAuthentication getPasswordAuthentication()
        {
            return m_passwordAuthentication;
        }
    }
    private static Logger log = Logger.getLogger(MessageSender.class);
	private MessageFormatter formatter ;
	
	private EmailAuthenticator authenticator;

	private String mailhost;

	private String mailbox;

}
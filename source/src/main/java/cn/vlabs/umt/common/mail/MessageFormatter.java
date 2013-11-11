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
package cn.vlabs.umt.common.mail;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import cn.vlabs.umt.common.util.CommonUtils;

public class MessageFormatter {
	public MessageFormatter(String path){
		this.path=path;
	}
	
	public String getContent(Locale locale, String templateName, Properties props) throws TemplateNotFound {
		String all = getFormattedMessage(locale, templateName, props);
		String content = "";
		if(all == null || "".equals(all.trim())) {
			return content;
		}
		int index = all.indexOf(EmailTemplate.SIGN_CONTENT);
		if(index > 0) {
			content = all.substring(index + EmailTemplate.SIGN_CONTENT.length());
		}
		return CommonUtils.trim(content);
	}
	
	public String getTitle(Locale locale, String templateName, Properties props) throws TemplateNotFound {
		String all = getFormattedMessage(locale, templateName, props);
		String title = "";
		if(all == null || "".equals(all.trim())) {
			return title;
		}
		int indexBegin = all.indexOf(EmailTemplate.SIGN_TITLE);
		int indexEnd = all.indexOf(EmailTemplate.SIGN_CONTENT);
		if(indexBegin >= 0) {
			if(indexEnd > indexBegin) {
				title = all.substring(indexBegin + EmailTemplate.SIGN_TITLE.length(), indexEnd);
			} else {
				title = all.substring(indexBegin + EmailTemplate.SIGN_TITLE.length());
			}
		}
		return CommonUtils.trim(title);
	}
	
	public String getFormattedMessage(Locale locale, String emailTemplateTarget, Properties prop) throws TemplateNotFound {
		String content = readTemplate(locale, emailTemplateTarget);
		if (prop!=null){
			Enumeration<Object> iter = prop.keys();
			while (iter.hasMoreElements()){
				String key = (String) iter.nextElement();
				String value= prop.getProperty(key);
				if (value==null)
				{
					value="";
				}
				content = StringUtils.replace(content, "%"+key+"%", value);
			}
		}
		return CommonUtils.trim(content);
	}
	
	private String readTemplate(Locale locale, String templateName) throws TemplateNotFound {
		StringBuffer content = new StringBuffer();
		Reader reader = null;
		String templateFileDir = path;
		try {
			templateFileDir = path + "/" + locale.toString();
			File f = new File(templateFileDir);
			if (!f.exists()){
				templateFileDir = path + "/en_US";
			}
			templateFileDir = templateFileDir + "/" + templateName;
			reader = new InputStreamReader(new FileInputStream(templateFileDir), "UTF-8");
			
			int num=0;
			while ((num=reader.read(buff)) != -1){
				content.append(buff, 0, num);
			}
		} catch (FileNotFoundException e) {
			throw new TemplateNotFound(templateFileDir);
		} catch (UnsupportedEncodingException e) {
			log.error("从操作系统读取模板文件时发生错误。");
			log.debug("详细信息",e);
		} catch (IOException e) {
			log.error("从操作系统读取模板文件时发生错误。");
			log.debug("详细信息",e);
		} finally {
			if (reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					log.debug("关闭模板文件时发生错误。");
				}
			}
		}
		return content.toString();
	}
	private static Logger log = Logger.getLogger(MessageFormatter.class);
	private String path;
	private char[] buff=new char[1024];
}
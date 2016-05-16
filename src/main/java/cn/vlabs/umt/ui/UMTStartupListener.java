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
package cn.vlabs.umt.ui;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import cn.vlabs.umt.common.datasource.CreateTable;
import cn.vlabs.umt.common.datasource.CreateOauthTokenTable;
import cn.vlabs.umt.common.job.JobThread;
import cn.vlabs.umt.common.util.ReflectUtils;

import com.octo.captcha.CaptchaFactory;
import com.octo.captcha.engine.image.gimpy.DefaultGimpyEngine;
import com.octo.captcha.module.servlet.image.SimpleImageCaptchaServlet;
import com.octo.captcha.service.image.ImageCaptchaService;

public class UMTStartupListener implements ServletContextListener {
	private JobThread thread;
	private static final Logger LOG=Logger.getLogger(UMTStartupListener.class);
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		context.removeAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		thread.interrupt();
	}

	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		WebApplicationContext factory = ContextLoader.getCurrentWebApplicationContext();
		
		CreateTable createTable = (CreateTable) factory.getBean("CreateTable");
		if (!createTable.isTableExist()){
			createTable.createTable(context);
		}
		CreateOauthTokenTable createTokenTable = (CreateOauthTokenTable) factory.getBean("CreateOauthTokenTable");
		if (!createTokenTable.isTableExist()){
			createTokenTable.createTable(context);
		}
		
		factory.getBean("UMTCredUtil");
		context.setAttribute(Attributes.APPLICATION_CONTEXT_KEY, factory);
		UMTContext.setFactory(factory);
		//set jcatana gimmy engine
		ImageCaptchaService obj=(ImageCaptchaService)(SimpleImageCaptchaServlet.service);
		Field engineF;
		try {
			engineF = obj.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("engine");
			engineF.setAccessible(true);
			DefaultGimpyEngine engine=(DefaultGimpyEngine)engineF.get(obj);
			CaptchaFactory facs=engine.getFactories()[0];
			ReflectUtils.setValue(facs, "caseSensitive", false);
		} catch (NoSuchFieldException e) {
			LOG.error("",e);
		} catch (SecurityException e) {
			LOG.error("",e);
		} catch (IllegalArgumentException e) {
			LOG.error("",e);
		} catch (IllegalAccessException e) {
			LOG.error("",e);
		}
		//start jobthread
		thread=new JobThread();
		thread.start();
	}
}
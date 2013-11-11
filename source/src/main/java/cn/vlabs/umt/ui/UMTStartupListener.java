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
package cn.vlabs.umt.ui;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import cn.vlabs.umt.common.datasource.CreateTable;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.account.ICoreMailClient;

public class UMTStartupListener implements ServletContextListener {
	public void contextDestroyed(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		FileSystemXmlApplicationContext factory= (FileSystemXmlApplicationContext)context.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		if (factory!=null){
			context.removeAttribute(Attributes.APPLICATION_CONTEXT_KEY);
			factory.close();
			factory=null;
		}
	}

	public void contextInitialized(ServletContextEvent event) {
		ServletContext context = event.getServletContext();
		PropertyConfigurator.configure(context.getRealPath("/WEB-INF/conf/log4j.properties"));
		String contextxml = context.getInitParameter("contextConfigLocation");
		if (contextxml==null){
			contextxml="/WEB-INF/conf/UMTContext.xml";
		}
		//FIX the bug in linux
		String realpath = context.getRealPath(contextxml);
		if (realpath!=null && realpath.startsWith("/")){
			realpath="/"+realpath;
		}
		FileSystemXmlApplicationContext  factory= new FileSystemXmlApplicationContext(realpath);
		PathMapper mapper = (PathMapper) factory.getBean("PathMapper");
		mapper.setContext(context);
		
		CreateTable createTable = (CreateTable) factory.getBean("CreateTable");
		if (!createTable.isTableExist()){
			createTable.createTable();
		}
		factory.getBean("UMTCredUtil");
		context.setAttribute(Attributes.APPLICATION_CONTEXT_KEY, factory);
		UMTContext.setFactory(factory);
		
	}
}
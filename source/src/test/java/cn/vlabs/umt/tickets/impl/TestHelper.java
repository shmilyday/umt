package cn.vlabs.umt.tickets.impl;

import java.net.URLDecoder;

import org.springframework.context.support.FileSystemXmlApplicationContext;

public class TestHelper {
	private static FileSystemXmlApplicationContext context;
	public static FileSystemXmlApplicationContext  getBeanFactory(){
		return context;
	}
	
	public static void close(){
		context.close();;
	}
	static{
		context=new FileSystemXmlApplicationContext("WebRoot/WEB-INF/UMTContext.xml"); 
	}
	
}

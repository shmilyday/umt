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
package cn.vlabs.duckling.umt.spi.thirdparty.auth.smtpcstnet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.Map;

import org.apache.log4j.Logger;

import cn.vlabs.duckling.common.util.Base64;

public class SmtpCstnet{
	private static Logger log = Logger.getLogger(SmtpCstnet.class);
	private  String host = "smtp.cstnet.cn";
	private String ehlo = "cstnet.cn";
	private  int port = 25;
	private  int timeout = 5000;
	private  int interval = 1000;
	private  int retry = 3;
    private  Socket socket = null;
    private BufferedReader reader = null;
    private OutputStream out = null;
    private String user;
    private String password;
    public void fillContext(Map<String,String> context)
    {
    	host = context.get(Constants.AUTH_HOST_KEY)!=null?context.get(Constants.AUTH_HOST_KEY):host;
    	ehlo = context.get(Constants.AUTH_EHLO_KEY)!=null?context.get(Constants.AUTH_EHLO_KEY):ehlo;
    	port = context.get(Constants.AUTH_HOST_PORT_KEY)!=null?Integer.parseInt(context.get(Constants.AUTH_HOST_PORT_KEY)):port;
    	timeout = context.get(Constants.AUTH_HOST_TIMEOUT_KEY)!=null?Integer.parseInt(context.get(Constants.AUTH_HOST_TIMEOUT_KEY)):timeout;
    	interval = context.get(Constants.AUTH_HOST_CONNECT_ERROR_WAITING_KEY)!=null?Integer.parseInt(context.get(Constants.AUTH_HOST_CONNECT_ERROR_WAITING_KEY)):interval;
    	retry = context.get(Constants.AUTH_HOSTS_CONNECT_TIMES_KEY)!=null?Integer.parseInt(context.get(Constants.AUTH_HOSTS_CONNECT_TIMES_KEY)):retry;
    }
    private boolean connect()
    {
    	for (int i = 0;; i++) {
			try {
				if(log.isDebugEnabled())
				{
					log.debug("连接: 主机:\"" + host + "\" 端口:\"" + port+ "\"");
				}
				socket = new Socket(host, port);
				break;
			} catch (IOException e) {
				log.error(("错误: 连接失败" + i + "次"));
				if (i == retry) {
					log.error(("错误: 连接失败" + i + "次"),e);
					return false;
				}
				try {
					Thread.sleep(interval);
				} catch (InterruptedException ie) {
				}
			}
		}
    	try {
			reader = new BufferedReader(new InputStreamReader(socket
					.getInputStream()));
			out = socket.getOutputStream();
			
			reandLine(reader);
			
		} catch (IOException e) {
			log.error("获取输入输出流发生错误!",e);
			return false;
		}
		try {
			socket.setSoTimeout(timeout);
		} catch (SocketException e) {
			log.error("设置连接Timeout时发生错误",e);
			return false;
		}  
    	return true;
    }
    private boolean ehlo()
    {
    	try {
        	sendCommand("EHLO "+ehlo,out);
			sendEmptyline(out);
			String response = reandLine(reader);
			if("250-mail".equals(response.subSequence(0, 8)))
	         {
				 reandLine(reader);
				 reandLine(reader);
				 reandLine(reader);
				 reandLine(reader);
				 reandLine(reader);
				 reandLine(reader);
	         }else if("250-app-03".equals(response.subSequence(0, 10)))
			 {
	        	 reandLine(reader);
				 reandLine(reader);
				 reandLine(reader);
				 reandLine(reader);
				 reandLine(reader);
			 }else
			 {
				 return false;
			 } 
		} catch (IOException e) {
			return false;
		}  
		return true;
    }
    private boolean authlogin()
    {
    	try {
			sendCommand("AUTH LOGIN",out);
			sendEmptyline(out); 
			if(getStateFromResponse(reandLine(reader))!=334)   
			{
				return false;
			}
			sendCommand(user,out);   
			sendEmptyline(out);   
			if(getStateFromResponse(reandLine(reader))!=334)   
			{
				return false;
			}
			sendCommand(password,out);   
			sendEmptyline(out);   
	        if(getStateFromResponse(reandLine(reader))!=235)   
			{
				return false;
			}
		} catch (IOException e) {
			return false;
		}  
		return true;
    }

	public boolean auth(String user, String password) {
		try {
			this.user = SmtpCstnet.encodeBase64(user);
			this.password = SmtpCstnet.encodeBase64(password);
			if (connect()&&ehlo()&&authlogin()) {
				sendCommand("QUIT",out);   
		        sendEmptyline(out);   
				return true;
			}
		} catch (IOException e) {
			log.error(e.getMessage(),e);
		}finally{
			if(socket!=null)
			{
				try {
					socket.close();
				} catch (IOException e) {
					log.error("关闭连接错误",e);
				}
			}
		}
		return false;
	}
	private static void sendCommand(String str,OutputStream out) throws IOException   
    {   
		if(log.isDebugEnabled())
		{
			log.debug("   send: "+str);
		}
        if(str==null)   
        {   
            str="";   
        }   
        out.write(str.getBytes());   
        out.flush();    
    } 
	public static String encodeBase64(String content) {
		return Base64.encode(content.getBytes());
	}

	public int getStateFromResponse(String response) throws IOException {
		return Integer.parseInt(response.substring(0, 3));
	}
	private static String reandLine(BufferedReader reader) throws IOException
	{
		String line = reader.readLine();
		if(log.isDebugEnabled())
		{
			log.debug("receive: "+line);
		}
		return line;
	}
	private static void sendEmptyline(OutputStream out) throws IOException   
    {   
        out.write('\r');   
        out.write('\n');   
        out.flush();   
    }   
}
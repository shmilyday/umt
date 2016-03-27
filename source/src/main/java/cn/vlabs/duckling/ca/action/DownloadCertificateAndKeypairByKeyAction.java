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
package cn.vlabs.duckling.ca.action;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class DownloadCertificateAndKeypairByKeyAction implements HttpAction {
	private String getLocalIPAddress(){
		Enumeration<NetworkInterface> interfaces;
		try {
			interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()){
				NetworkInterface current = interfaces.nextElement();
				if (!current.isUp() || current.isLoopback() || current.isVirtual()) continue;
				Enumeration<InetAddress> addresses = current.getInetAddresses();
				while (addresses.hasMoreElements()){
					InetAddress current_addr = addresses.nextElement();
					if (current_addr.isLoopbackAddress()) continue;
					if (current_addr instanceof Inet4Address){
						return current_addr.getHostAddress();
					}
				}
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return null;
	}
	private MultipartEntityBuilder buildGetCert(String xsrfToken,
			String requestKey,String password,String dn,String keyFormat) throws UnsupportedEncodingException {
		
		String ipAddress = getLocalIPAddress();
		String port = "40065";
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		String fullScript = "pki?format_sendcert=cer;format_send_cert_key=openssl;Submit=Download;"
							+"GET_PARAMS_CMD=send_cert_key;cmd=getParams;dataType=VALID_CERTIFICATE;name=PUBLIC;"
							+"HIDDEN_key={key};xsrf_protection_token={xsrfToken};key={key};passwd={password};"
							+"signature=;format=;text=;new_dn=;dn={dn};"
							+"HTTP_REQUEST_METHOD=POST;HTTP_USER_AGENT=Mozilla%2F5.0%20%28Macintosh%3B%20Intel%20Mac%20OS%20X%2010_9_5%29%20AppleWebKit%2F537.36%20%28KHTML%2C%20like%20Gecko%29%20Chrome%2F45.0.2454.93%20Safari%2F537.36;"
							+"AGENT_NAME=Safari;AGENT_VERSION=537.36;AGENT_OS_NAME=KHTML;AGENT_OS_VERSION=%2C;REMOTE_ADDR={remoteAddr};REMOTE_PORT={remotePort};HTTP_CGI_SCRIPT=pki";
		fullScript = fullScript.replaceAll("\\{xsrfToken\\}", xsrfToken);
		fullScript = fullScript.replaceAll("\\{remoteAddr\\}", ipAddress);
		fullScript = fullScript.replaceAll("\\{remotePort\\}", port);
		fullScript = fullScript.replaceAll("\\{key\\}", requestKey);
		fullScript = fullScript.replaceAll("\\{dn\\}", escape(dn));

		builder
				.addTextBody("GET_PARAMS_STEP", "0")
				.addTextBody("GET_PARAMS_CMD", "send_cert_key")
				.addTextBody("AGENT_OS_NAME", "KHTML")
				.addTextBody("dataType", "VALID_CERTIFICATE")
				.addTextBody("key", requestKey)
				.addTextBody("xsrf_protection_token", xsrfToken)
				.addTextBody("HTTP_FULL_CGI_SCRIPT", fullScript)
				.addTextBody("REMOTE_PORT", port)
				.addTextBody(
						"HTTP_USER_AGENT",
						"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.130 Safari/537.36")
				.addTextBody("HTTP_ACCEPT_LANGUAGE", "zh-CN,zh;q=0.8")
				.addTextBody("cmd", "getParams")
				.addTextBody("name", "PUBLIC")
				.addTextBody("HIDDEN_key", requestKey)
				.addTextBody("OPENCA_AC_CHANNEL_SERVER_SOFTWARE", "Apache/2.2.15 (CentOS)")
				.addTextBody("signature", "")
				.addTextBody("HTTP_REQUEST_METHOD", "POST")
				.addTextBody("Submit", "Download")
				.addTextBody("AGENT_OS_VERSION", "")
				.addTextBody("HTTP_ACCEPT_ENCODING", "gzip, deflate")
				.addTextBody("OPENCA_AC_CHANNEL_REMOTE_ADDRESS", ipAddress)
				.addTextBody("HTTP_CGI_SCRIPT", "pki")
				.addTextBody("text", "")
				.addTextBody("OPENCA_AC_INTERFACE", "public")
				.addTextBody("new_dn", "")
				.addTextBody("format_sendcert", "cer")
				.addTextBody("REMOTE_ADDR", ipAddress)
				.addTextBody("AGENT_VERSION", "537.36")
				.addTextBody("passwd", password)
				.addTextBody("format", "")
				.addTextBody("format_send_cert_key", keyFormat)
				.addTextBody("AGENT_NAME", "Safari")
				.addTextBody("dn", dn);
		return builder;
	}
	@Override
	public ActionResult execute(HttpClient httpClient, Map<String, String> context)
			throws IOException {
		String xsrfToken = context.get(XSRF_TOKEN);
		String baseUrl = context.get(BASE_URL);
		String requestKey = context.get(REQUEST_KEY);
		String password = context.get(PASSWORD);
		String dn= context.get("dn");
		String keyFormat=context.get(CERT_KEY_Format);
		MultipartEntityBuilder builder = buildGetCert(xsrfToken, requestKey,password,dn,keyFormat);
		HttpPost post = new HttpPost(baseUrl + "/cgi-bin/pki/pub/pki");
		post.setEntity(builder.build());
		HttpResponse response = httpClient.execute(post);
		ActionResult result=new ActionResult(response);
		post.releaseConnection();
		return result;
	}
	
	
	private String  escape (String src)  
	 {  
	  int i;  
	  char j;  
	  StringBuffer tmp = new StringBuffer();  
	  tmp.ensureCapacity(src.length()*6);  
	  
	  for (i=0;i<src.length() ;i++ )  
	  {  
	  
	   j = src.charAt(i);  
	  
	   if (Character.isDigit(j) || Character.isLowerCase(j) || Character.isUpperCase(j))  
	    tmp.append(j);  
	   else  
	    if (j<256)  
	    {  
	    tmp.append( "%" );  
	    if (j<16)  
	     tmp.append( "0" );  
	    tmp.append( Integer.toString(j,16) );  
	    }  
	    else  
	    {  
	    tmp.append( "%u" );  
	    tmp.append( Integer.toString(j,16) );  
	    }  
	  }  
	  return tmp.toString().toUpperCase();  
	 }  

}

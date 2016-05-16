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
package cn.vlabs.umt.services.ca;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.lang.StringUtils;
import org.apache.http.client.HttpClient;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipOutputStream;

import cn.vlabs.duckling.ca.HttpClientFactory;
import cn.vlabs.duckling.ca.XPathParser;
import cn.vlabs.duckling.ca.action.ActionResult;
import cn.vlabs.duckling.ca.action.CsrRequestAction;
import cn.vlabs.duckling.ca.action.DownloadCertificateAndKeypairByKeyAction;
import cn.vlabs.duckling.ca.action.GetCertificateByKeyAction;
import cn.vlabs.duckling.ca.action.GetKeyByDnAction;
import cn.vlabs.duckling.ca.action.HttpAction;
import cn.vlabs.duckling.ca.action.XsrfTokenAction;
import cn.vlabs.umt.common.util.UMTStringUtils;

public class CaService {
	
	public static String CERT_FORMAT_CRT="crt";
	public static String CERT_FORMAT_TXT="txt";
	public static String CERT_FORMAT_PEM="pem";
	public static String CERT_FORMAT_DER="der";
	public static String CERT_FORMAT_CER="cer";
	
	public static String CERT_FORRMAT_CERT_KEY_OPENSSL="openssl";
	public static String CERT_FORRMAT_CERT_KEY_PKCS8="pkcs8";
	public static String CERT_FORRMAT_CERT_KEY_PKCS12="pkcs12";
	
	private static final String REQUEST_KEY_XPATH = "/HTML/BODY/FORM/CENTER/TABLE/TR/TD/CENTER/TABLE/TR[2]/TD[1]/A";

	
	private String endDN;
	private HttpClient httpClient;
	private String serverUrl;
	
	


	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	public void setEndDN(String endDN) {
		this.endDN = endDN;
	}

	public CaService() {
		this.httpClient = HttpClientFactory.getHttpClient();
	}
	
	public String getToken() throws IOException{
		Map<String, String> context = new HashMap<String, String>();
		context.put("baseUrl", serverUrl);
		
		XsrfTokenAction xsrf = new XsrfTokenAction();
		xsrf.execute(httpClient, context);
		ActionResult actionResult=xsrf.execute(httpClient, context);
		String xsrfToken = parseToken(actionResult);
		return xsrfToken;
	}
	
	
	public boolean submitCsrRequest(String cn, String password, String email,String dn) throws IOException{
		String token=getToken();
		return submitCsrRequest(cn,password,email,dn,token);
	}
	
	public boolean submitCsrRequest(String cn, String password, String email,String dn,String token)
			throws IOException {
		Map<String, String> context = new HashMap<String, String>();
		context.put(HttpAction.BASE_URL, serverUrl);
		context.put(HttpAction.XSRF_TOKEN, token);
		 context.put(HttpAction.DN, dn);
		 context.put(HttpAction.CN, cn);
		 context.put(HttpAction.PASSWORD, password);
		 context.put(HttpAction.EMAIL, email);
		
		CsrRequestAction request = new CsrRequestAction();
		ActionResult actionResult=request.execute(httpClient, context);
		return (actionResult.getResponseCode()== 200);
	}

	
	public InputStream getCert(String dn,String certFormat) throws IOException, DownloadCaException, KeyEmptyException{
		String token=getToken();
		String key=getKey(dn,token);
		return getCertByKey(key,token,certFormat);
	}
	
	public InputStream getCertByKey(String requestKey,String certFormat) throws IOException, DownloadCaException{
		String token=getToken();
		return getCertByKey(requestKey,token,certFormat);
	}
	
	public InputStream getCertByKey(String key,String token,String certFormat) throws IOException, DownloadCaException{
	  	Map<String, String> context = new HashMap<String, String>();
		context.put(HttpAction.BASE_URL, serverUrl);
		context.put(HttpAction.REQUEST_KEY, key);
		context.put(HttpAction.XSRF_TOKEN, token);
		context.put(HttpAction.CERT_FORMAT, certFormat);
		
		GetCertificateByKeyAction download=new GetCertificateByKeyAction();
		ActionResult actionResult=download.execute(httpClient, context);
		if (actionResult.getResponseCode()== 200&&!actionResult.isHtml()) {
			return actionResult.getIn();
		}
		
		throw new DownloadCaException(key);
	}	
	
	public InputStream downloadCertAndKeypairByKey(String requestKey,String password,String dn,String certKeyFormat) throws IOException, DownloadCaException{
		
		String token=getToken();
		return downloadCertAndKeypairByKey(requestKey,password,dn,token,certKeyFormat);
		
	}
	
	public InputStream downloadCertAndKeypairByKey(String password,String dn,String certKeyFormat) throws IOException, DownloadCaException, KeyEmptyException{
		
		String token=getToken();
		String key=getKey(dn,token);
		return downloadCertAndKeypairByKey(key,password,dn,token,certKeyFormat);
		
	}
	
	
	public String downloadKeypair(String password,String dn) throws IOException, DownloadCaException, KeyEmptyException{
		
		String token=getToken();
		String key=getKey(dn,token);
		InputStream in= downloadCertAndKeypairByKey(key,password,dn,token,CERT_FORRMAT_CERT_KEY_OPENSSL);
		String encode = "UTF-8";
		InputStreamReader reader = new InputStreamReader(in, encode);
		try {
			String certAndKey=UMTStringUtils.readToString(reader);
			String keypair=StringUtils.substring(certAndKey, StringUtils.indexOf(certAndKey, "-----BEGIN ENCRYPTED PRIVATE KEY-----"), certAndKey.length());
			
			return keypair;
		} finally {
			reader.close();
		}
	}
	
	  
	public InputStream downloadCertAndKeypairByKey(String key,String password,String dn,String token,String certKeyFormat) throws IOException, DownloadCaException{
		  Map<String, String> context = new HashMap<String, String>();
		  context.put(HttpAction.BASE_URL, serverUrl);
		  context.put(HttpAction.REQUEST_KEY, key);
		  context.put(HttpAction.PASSWORD, password);
		  context.put(HttpAction.DN, dn);
		  context.put(HttpAction.XSRF_TOKEN, token);
		  context.put(HttpAction.CERT_KEY_Format, certKeyFormat);
		  
		  
		  DownloadCertificateAndKeypairByKeyAction download=new DownloadCertificateAndKeypairByKeyAction();
		  ActionResult actionResult=download.execute(httpClient, context);
		  if (actionResult.getResponseCode()== 200&&!actionResult.isHtml()) {
				return actionResult.getIn();
			}
		  throw new DownloadCaException(key);
	  }
	
	public String getKey(String dn) throws IOException, KeyEmptyException{
		String token=getToken();
		return  getKey(dn,token);
	}
	
	public String getKey(String dn,String token) throws IOException, KeyEmptyException{
		  Map<String, String> context = new HashMap<String, String>();
		  context.put(HttpAction.BASE_URL, serverUrl);
		  context.put(HttpAction.DN, dn);
		  context.put(HttpAction.XSRF_TOKEN, token);
		  GetKeyByDnAction download=new GetKeyByDnAction();
		 ActionResult actionResult=download.execute(httpClient, context);
		  if (actionResult.getResponseCode()== 200) {
				BufferedReader reader = new BufferedReader(new InputStreamReader(actionResult.getIn(),"UTF-8"));
				String href=XPathParser.getNodeValue(reader, REQUEST_KEY_XPATH,"href");
				String result=getKeyFormHrefUrl(href);
				if(StringUtils.isBlank(result)){
					throw new KeyEmptyException(dn);
				}
			}
		  return "";
	}
	
	
	public InputStream getCertAll(String password,String dn,String cn) throws IOException, DownloadCaException, KeyEmptyException{
		
		String token=this.getToken();
		String key=getKey(dn);
		
		InputStream cerIn=getCertByKey(key,token, CERT_FORMAT_CER);
		InputStream p12In=downloadCertAndKeypairByKey(key,password, dn,token, CERT_FORRMAT_CERT_KEY_PKCS12);
		InputStream keypair=new ByteArrayInputStream(downloadKeypair(password, dn).getBytes());
		ByteArrayOutputStream zipBaseOut = new ByteArrayOutputStream();
		ZipOutputStream zipOut=new ZipOutputStream(zipBaseOut);
		
		zipCert(cerIn,zipOut,cn+".cer");
		zipCert(p12In,zipOut,cn+".p12"); 
		zipCert(keypair,zipOut,cn+".pem");
		zipOut.close();
		return new ByteArrayInputStream(zipBaseOut.toByteArray());
		
		
	}
	
	
	private void zipCert(InputStream in,ZipOutputStream zipOut,String fileName) throws IOException{
		zipOut.putNextEntry(new ZipEntry(fileName));
		 int count;  
        byte data[] = new byte[100];  
        while ((count = in.read(data, 0, 100)) != -1) {  
            zipOut.write(data, 0, count);  
        }  
        in.close();  
	}
	
	
	
	
	private String getKeyFormHrefUrl(String href){
		
		if(href==null||href.equals("")){
			return "";
		}
		int keyStart=StringUtils.indexOf(href, "key=");
		int keyEnd=StringUtils.indexOf(StringUtils.substring(href, keyStart),";");
		return StringUtils.substring(href, keyStart+4, keyStart+keyEnd);
		
		
		
	}
	
	private String parseToken(ActionResult result)
			throws UnsupportedOperationException, IOException {
		try {
			List<String> lines = IOUtils.readLines(result.getIn());
			Pattern pattern = Pattern
					.compile(".*\";xsrf_protection_token=(.*)\".*");

			for (String line : lines) {
				Matcher matcher = pattern.matcher(line);
				if (matcher.matches()) {
					return matcher.group(1);
				}
			}
			return "";
		} finally {
			result.getIn().close();
		}
	}
	
	public String buildDN(String cn){
		return "CN="+cn+endDN;
	}
	  
}
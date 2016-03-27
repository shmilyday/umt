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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;

public class ActionResult {
	
	public ActionResult(HttpResponse response){
		isHtml=this.isResponseHtml(response);
		isFile=this.isResponseFile(response);
		isTxt=this.isResponseTxt(response);
		in=copyResponseInputStream(response);
		responseCode=this.getResponseCode(response);
	}

	private boolean result;
	private InputStream in;
	private boolean isHtml;
	private boolean isTxt;
	private boolean isFile;
	private int responseCode;
	public boolean isResult() {
		return result;
	}
	public void setResult(boolean result) {
		this.result = result;
	}
	public InputStream getIn() {
		return in;
	}
	public void setIn(InputStream in) {
		this.in = in;
	}
	public boolean isHtml() {
		return isHtml;
	}
	public void setHtml(boolean isHtml) {
		this.isHtml = isHtml;
	}
	
	public boolean isTxt() {
		return isTxt;
	}
	public void setTxt(boolean isTxt) {
		this.isTxt = isTxt;
	}
	public boolean isFile() {
		return isFile;
	}
	public void setFile(boolean isFile) {
		this.isFile = isFile;
	}
	
	
	private InputStream copyResponseInputStream(HttpResponse response){
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			InputStream in=response.getEntity().getContent();
			IOUtils.copy(in, out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		 ByteArrayInputStream result = new ByteArrayInputStream(out.toByteArray());
		
		
		return result;
	}
	
	
	
	
	
	private boolean isResponseHtml(HttpResponse response){
		String responseType=getResponseContentType(response);
		if(responseType==null||"".equals(responseType)){
			return false;
		}
		
		if("text/html".equalsIgnoreCase(responseType)){
			return true;
		}
		return false;
	}
	
	private boolean isResponseFile(HttpResponse response){
		String responseType=getResponseContentType(response);
		if(responseType==null||"".equals(responseType)){
			return false;
		}
		
		if(responseType.startsWith("application")){
			return true;
		}
		return false;
	}
	
	private boolean isResponseTxt(HttpResponse response){
		String responseType=getResponseContentType(response);
		if(responseType==null||"".equals(responseType)){
			return false;
		}
		
		if("text/plain".equalsIgnoreCase(responseType)){
			return true;
		}
		return false;
	}
	
	
	public static String getResponseContentType(HttpResponse response){
		Header contentHeader = response.getFirstHeader("Content-Type");  
		
		if(contentHeader==null){
			return "";
		}
		
		
		HeaderElement[] values = contentHeader.getElements();  
        if (values.length == 1) {  
            String paramName = values[0].getName();  
            return paramName;
        }
        return null;
		
	}
	
	
	
	
	public int getResponseCode() {
		return responseCode;
	}
	public void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	private int getResponseCode(HttpResponse response){
		return response.getStatusLine().getStatusCode();
	}
	
	
	
	
}

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
package cn.vlabs.duckling.ca;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpRequestBase;

public class RawFileResponseImpl implements RawFileResponse {
	private HttpRequestBase request;
	private HttpResponse response;
	public RawFileResponseImpl(HttpRequestBase request, HttpResponse response){
		this.request  = request;
		this.response = response;
	}
	
	private String readFromHeader() {
		Header[] headers = response.getHeaders("Content-Disposition");
		if (headers!=null && headers.length>0){
			HeaderElement[] values = headers[0].getElements();
			for (HeaderElement element:values){
				if (element.getParameterByName("filename")!=null){
					return element.getParameterByName("filename").getValue();
				}
			}
		}
		return null;
	}
	
	@Override
	public String getFileName() {
		String filename =  readFromHeader();
		if (filename==null){
			String fullpath = request.getURI().getPath();
			int lastSlashIndex = fullpath.lastIndexOf('/');
			if (lastSlashIndex!=-1){
				filename = fullpath.substring(lastSlashIndex+1);
			}else{
				filename = fullpath;
			}
		}
		return filename;
	}


	@Override
	public long getFileSize() {
		Header[] headers = response.getHeaders("Content-Length");
		if (headers!=null && headers.length>0){
			return Long.parseLong(headers[0].getValue());
		}
		return -1;
	}

	@Override
	public InputStream getInputStream() throws IOException {
		return response.getEntity().getContent();
	}
	
	@Override
	public void close() {
		request.releaseConnection();
	}
}
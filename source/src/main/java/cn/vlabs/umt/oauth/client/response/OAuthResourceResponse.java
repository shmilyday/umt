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
package cn.vlabs.umt.oauth.client.response;

import cn.vlabs.umt.oauth.common.exception.OAuthProblemException;

public class OAuthResourceResponse  extends OAuthClientResponse {
	
    public String getBody() {
        return body;
    }	
 
	public int getResponseCode() {
		return responseCode;
	}
	
	public String getContentType(){
		return contentType;
	}
    
	@Override
	protected void setBody(String body) throws OAuthProblemException {
		 this.body = body;
	}

	@Override
	protected void setContentType(String contentType) {
		this.contentType = contentType;	
	}

	@Override
	protected void setResponseCode(int responseCode) {
		this.responseCode = responseCode;
	}
	
	@Override
	protected void init(String body, String contentType, int responseCode) throws OAuthProblemException {
        this.setBody(body);
        this.setContentType(contentType);
        this.setResponseCode(responseCode);
	}

}
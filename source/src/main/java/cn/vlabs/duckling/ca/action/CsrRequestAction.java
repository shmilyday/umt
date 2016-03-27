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
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class CsrRequestAction implements HttpAction {
	public CsrRequestAction() throws IOException {
	}
	private MultipartEntityBuilder buildForm(Map<String, String> context)
			throws IOException {
		
		String xsrfToken=context.get("xsrfToken");
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("status", "finished-client-filled-form")
				.addTextBody("cn", context.get(CN))
				.addTextBody("xsrf_protection_token", xsrfToken)
				.addTextBody("strength", "Base")
				.addTextBody("ra", "Trustcenter itself")
				.addTextBody("passwd2", context.get(PASSWORD))
				.addTextBody("operation", "client-filled-form")
				.addTextBody("bits", "1024")
				.addTextBody("genkey", "Server (Our Server)")
				.addTextBody("request", "")
				.addTextBody("EMAIL_ATTRIBUTE_0", context.get(EMAIL))
				.addTextBody("loa", "Medium")
				.addTextBody("CSR_TYPE", "BASIC")
				.addTextBody("cmd", "advanced_csr")
				.addTextBody("ADDITIONAL_ATTRIBUTE_CN", context.get(CN))
				.addTextBody("passwd1", context.get(PASSWORD))
				.addTextBody("keytype", "RSA")
				.addTextBody("ADDITIONAL_ATTRIBUTE_EMAIL", context.get(EMAIL))
				.addTextBody("role", "User")
				.addTextBody("dn", context.get(DN))
				
				
				
				.addTextBody("ADDITIONAL_ATTRIBUTE_CITY", "")
				.addTextBody("ADDITIONAL_ATTRIBUTE_UID", "")
				.addTextBody("ADDITIONAL_ATTRIBUTE_DEPARTMENT", "")
				.addTextBody("ADDITIONAL_ATTRIBUTE_LASTNAME", "")
				.addTextBody("ADDITIONAL_ATTRIBUTE_BIRTHDATE", "")
				.addTextBody("ADDITIONAL_ATTRIBUTE_STATE", "")
				.addTextBody("ADDITIONAL_ATTRIBUTE_FIRSTNAME", "")
				.addTextBody("ADDITIONAL_ATTRIBUTE_ADDRESS", "")
				.addTextBody("OTHER_NAME_1", "")
				.addTextBody("ADDITIONAL_ATTRIBUTE_TEL", "")
				.addTextBody("ou", "Users")
				.addTextBody("ADDITIONAL_ATTRIBUTE_COUNTRY", "");
		
		
		return builder;
	}

	@Override
	public ActionResult execute(HttpClient httpClient, Map<String, String> context)
			throws IOException {
		String baseUrl = context.get(BASE_URL);
		MultipartEntityBuilder builder = buildForm(context);
		HttpPost post = new HttpPost(baseUrl + "/cgi-bin/pki/pub/pki");
		post.setEntity(builder.build());
		HttpResponse response = httpClient.execute(post);
		ActionResult result=new ActionResult(response);
		post.releaseConnection();
		return result;
	}
	
	
}

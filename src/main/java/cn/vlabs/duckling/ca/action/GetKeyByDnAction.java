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

public class GetKeyByDnAction implements HttpAction {
	public GetKeyByDnAction() throws IOException {
	}
	private MultipartEntityBuilder buildForm(Map<String, String> context)
			throws IOException {
		
		String xsrfToken=context.get("xsrfToken");
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();
		builder.addTextBody("name_3", "DN")
				.addTextBody("name_5", "KEY")
				.addTextBody("name_1", "CN")
				.addTextBody("cmd", "search")
				.addTextBody("dataType", "CERTIFICATE")
				.addTextBody("name_4", "ROLE")
				.addTextBody("pcounter", "5")
				.addTextBody("xsrf_protection_token", xsrfToken)
				.addTextBody("name_2", "emailAddress")
				.addTextBody("value_1", "")
				.addTextBody("value_2", "")
				.addTextBody("value_3", context.get(DN))
				.addTextBody("value_4", "")
				.addTextBody("value_5", "")
				
				;
		
		
		return builder;
	}

	@Override
	public ActionResult execute(HttpClient httpClient, Map<String, String> context)
			throws IOException {
		String baseUrl = context.get(BASE_URL);
		MultipartEntityBuilder builder = buildForm(context);
		HttpPost post = new HttpPost(baseUrl + "/cgi-bin/pki/pub/pki");
//		try {
//			post.setEntity(builder.build());
//			HttpResponse response = httpClient.execute(post);
//			if (response.getStatusLine().getStatusCode() == 200) {
//				HttpEntity entity = response.getEntity();
//				String str=EntityUtils.toString(entity, "utf-8");
//				System.out.println(str);
//			}
//		} finally {
//			post.releaseConnection();
//		}
		
		post.setEntity(builder.build());
		HttpResponse response = httpClient.execute(post);
		ActionResult result=new ActionResult(response);
		post.releaseConnection();
		return result;
		
		
	}
	
	
}

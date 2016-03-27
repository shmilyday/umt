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

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntityBuilder;

public class GetCertificateByKeyAction implements HttpAction {
	
	private MultipartEntityBuilder buildGetCert(String xsrfToken,
			String requestKey,String certFormat) {
		
		MultipartEntityBuilder builder = MultipartEntityBuilder.create();

		builder.addTextBody("format_send_cert_key", "openssl")
				.addTextBody("format_sendcert", certFormat)
				.addTextBody("Submit", "Get")
				.addTextBody("GET_PARAMS_CMD", "")
				.addTextBody("cmd", "sendcert")
				.addTextBody("dataType", "VALID_CERTIFICATE")
				.addTextBody("name", "PUBLIC")
				.addTextBody("HIDDEN_key", requestKey)
				.addTextBody("xsrf_protection_token", xsrfToken)
				.addTextBody("key", requestKey)
				.addTextBody("password", "")
				.addTextBody("signature", "")
				.addTextBody("format", "")
				.addTextBody("text", "")
				.addTextBody("new_dn", "")
				.addTextBody("dn", "CN=xzj1 xzj1,OU=Users,O=CSTNET CA");

		return builder;
	}
	@Override
	public ActionResult execute(HttpClient httpClient, Map<String, String> context)
			throws IOException {
		String xsrfToken = context.get(XSRF_TOKEN);
		String baseUrl = context.get(BASE_URL);
		String requestKey = context.get(REQUEST_KEY);
		String certFormat= context.get(CERT_FORMAT);
		MultipartEntityBuilder builder = buildGetCert(xsrfToken, requestKey,certFormat);
		HttpPost post = new HttpPost(baseUrl + "/cgi-bin/pki/pub/pki");
		post.setEntity(builder.build());
		HttpResponse response = httpClient.execute(post);
		ActionResult result=new ActionResult(response);
		post.releaseConnection();
		return result;
	}
	
	
	public static String getFileName(HttpResponse response) {  
        Header contentHeader = response.getFirstHeader("Content-Disposition");  
        String filename = null;  
        if (contentHeader != null) {  
            HeaderElement[] values = contentHeader.getElements();  
            if (values.length == 1) {  
                NameValuePair param = values[0].getParameterByName("filename");  
                    try {  
                        //filename = new String(param.getValue().toString().getBytes(), "utf-8");  
                        //filename=URLDecoder.decode(param.getValue(),"utf-8");  
                        filename = param.getValue();  
                    } catch (Exception e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        return filename;  
    }  
	
	public static String getRandomFileName() {  
        return String.valueOf(System.currentTimeMillis());  
    }  
	
	
	

}

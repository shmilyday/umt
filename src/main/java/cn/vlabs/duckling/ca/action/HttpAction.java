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

public interface HttpAction {
	static final String BASE_URL = "baseUrl";
	static final String XSRF_TOKEN = "xsrfToken";
	static final String TELEPHONE = "telephone";
	static final String DEPARTMENT = "department";
	static final String EMAIL = "email";
	static final String CN = "cn";
	static final String PASSWORD = "password";
	static final String REQUEST_KEY = "requestKey";
	static final String CERTIFICATE = "certificate";
	static final String LAST_NAME = "lastName";
	static final String FIRST_NAME = "firstName";
	static final String DN = "dn";
	static final String CERT_FORMAT = "certFormat";
	static final String CERT_KEY_Format = "certKeyFormat";
	
	ActionResult execute(HttpClient httpClient, Map<String, String> context)
			throws IOException;
}

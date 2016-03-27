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
package cn.vlabs.umt.ui.jsapi;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public abstract class APIBaseServlet  {
	private static final int CODE_FORBIDDEN = 403;
	private static final int CODE_MISSING_PARAM = 501;
	private static final int CODE_NOT_FOUND = 404;
	private static final int CODE_OK = 200;
	private static final int CODE_WRONG_URL = 502;

	protected boolean ensureParamExist(HttpServletRequest request,
			HttpServletResponse response, String... params) throws IOException {
		for (String param : params) {
			if (request.getParameter(param) == null) {
				sayMissingParam(response, param);
				return false;
			}
		}
		return true;
	}

	protected void printJSON(HttpServletResponse response, JSONObject object)
			throws IOException {
		response.setContentType("text/javascript");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(object.toJSONString());
		writer.flush();
		writer.close();
	}

	protected void sayMissingParam(HttpServletResponse response, String param)
			throws IOException {
		JSONObject object = new JSONObject();
		object.put("stauts", "fail");
		object.put("code", CODE_MISSING_PARAM);
		object.put("message", "必须提供参数" + param);
		printJSON(response, object);
	}

	protected void sayNoAccess(HttpServletResponse response, String message)
			throws IOException {
		JSONObject object = new JSONObject();
		object.put("stauts", "fail");
		object.put("code", CODE_FORBIDDEN);
		object.put("message", message);
		printJSON(response, object);
	}

	protected void sayNotFound(HttpServletResponse response, String message)
			throws IOException {
		JSONObject object = new JSONObject();
		object.put("stauts", "fail");
		object.put("code", CODE_NOT_FOUND);
		object.put("message", message);
		printJSON(response, object);
	}

	protected void saySuccess(HttpServletResponse response, JSONArray message)
			throws IOException {
		JSONObject object = new JSONObject();
		object.put("stauts", "success");
		object.put("code", CODE_OK);
		object.put("result", message);
		printJSON(response, object);
	}
	
	protected void saySuccess(HttpServletResponse response, JSONObject message)
			throws IOException {
		JSONObject object = new JSONObject();
		object.put("stauts", "success");
		object.put("code", CODE_OK);
		object.put("result", message);
		printJSON(response, object);
	}

	protected void sayWrongURL(HttpServletResponse response, String message)
			throws IOException {
		JSONObject object = new JSONObject();
		object.put("status", "fail");
		object.put("code", CODE_WRONG_URL);
		object.put("message", message);
		printJSON(response, object);
	}
}

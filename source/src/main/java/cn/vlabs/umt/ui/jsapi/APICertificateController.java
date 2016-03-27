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
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.vlabs.umt.services.certificate.DucklingCertificate;
import cn.vlabs.umt.services.certificate.ICertificateService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 用户证书管理Servlet
 */
@Controller
@RequestMapping("/api/certificate/**")
public class APICertificateController extends APIBaseServlet {
	@Autowired
	private ICertificateService certs ;
	
	private String checkURI(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String[] parts = getURI(request).split("/");
		if (parts.length != 3) {
			sayWrongURL(response, "URL错误，必须使用/api/certificate/{username}形式的URL");
			return null;
		}
		String cstnetId = parts[2];
		LoginInfo loginInfo = UMTContext
				.getLoginInfo(request.getSession(false));
		if (loginInfo == null || loginInfo.getUser() == null
				|| !cstnetId.equals(loginInfo.getUser().getCstnetId())) {
			sayNoAccess(response,  "用户没有权限访问这个内容。");
			return null;
		}
		return cstnetId;
	}

	private void doGetCertificate(HttpServletResponse response,
			String cstnetId, boolean isFull) throws IOException {
		DucklingCertificate certificate = certs
				.getCertificate(cstnetId);
		if (certificate == null) {
			sayNotFound(response, "没有找到该用户的证书。");
			return;
		}
		JSONObject object = new JSONObject();
		object.put("cstnetId", certificate.getCstnetId());
		object.put("dn", certificate.getDn());
		object.put("registTime", certificate.getRegistTime().getTime());
		object.put("pubCert", certificate.getPubCert());
		if (isFull) {
			object.put("fullCert", certificate.getFullCert());
		}
		saySuccess(response, object);
	}

	private String getURI(HttpServletRequest request){
		int startIndex = request.getRequestURI().indexOf("api/certificate");
		return request.getRequestURI().substring(startIndex);
	}

	/**
	 * 删除证书（只允许更新用户自己的证书） /api/certificate/{username} 删除用户的所有证书
	 */
	@RequestMapping(method=RequestMethod.DELETE)
	public void doDelete(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String cstnetId = checkURI(request, response);
		if (cstnetId != null) {
			certs.deleteCertificate(cstnetId);
			saySuccess(response, new JSONObject());
		}
	}

	/**
	 * 下载证书 /api/certificate/{username} 下载公钥 /aip/certificate/{username}/full
	 * 下载完整版本的证书
	 */
	@RequestMapping(method=RequestMethod.GET)
	public void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String uri = getURI(request);
		String[] parts = uri.split("/");
		switch (parts.length) {
		case 4:
			if (!parts[3].equals("full")) {
				sayWrongURL(response,
						"URL错误，必须使用/api/certificate/{username}/full形式的URL");
				break;
			}
			LoginInfo loginInfo = UMTContext.getLoginInfo(request
					.getSession(false));
			if (loginInfo == null || loginInfo.getUser() == null
					|| !parts[2].equals(loginInfo.getUser().getCstnetId())) {
				sayNoAccess(response,  "用户没有权限访问这个内容。");
				break;
			}
			doGetCertificate(response, parts[2], true);
			break;
		case 3:
			doGetCertificate(response, parts[2], false);
			break;
		default:
			sayWrongURL(response,
					"URL错误,没有找到URL中的username部分（/api/certificate/{username}");
			break;
		}
	}

	/**
	 * 上传证书，如果已存在则进行更新（只允许更新自己的证书） /api/certificate/{username}
	 * 上传证书（只有公钥的部分和完整的部分）
	 */
	@RequestMapping(method=RequestMethod.POST)
	public void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String cstnetId = checkURI(request, response);

		if (cstnetId != null
				&& ensureParamExist(request, response, "dn", "pubCert",
						"fullCert")) {
			DucklingCertificate cert = new DucklingCertificate();
			cert.setCstnetId(cstnetId);
			cert.setDn(request.getParameter("dn"));
			cert.setFullCert(request.getParameter("fullCert"));
			cert.setPubCert(request.getParameter("pubCert"));
			cert.setRegistTime(new Date());
			certs.saveCertificate(cert);
			JSONObject message = new JSONObject();
			message.put("registTime", cert.getRegistTime().getTime());
			saySuccess(response, message);
		}
	}

}

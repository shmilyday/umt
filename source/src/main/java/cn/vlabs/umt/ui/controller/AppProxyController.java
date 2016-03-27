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
package cn.vlabs.umt.ui.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.vlabs.commons.principal.AppPrincipal;
import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;
import cn.vlabs.duckling.common.transmission.SignedEnvelope;
import cn.vlabs.duckling.common.util.Base64;
import cn.vlabs.duckling.common.util.Base64.DecodingException;
import cn.vlabs.umt.common.util.RandomUtil;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.proxy.impl.AppCredContent;
import cn.vlabs.umt.services.site.AppService;
import cn.vlabs.umt.services.site.Application;
@Controller
@RequestMapping("/appproxy")
@Deprecated
public class AppProxyController {
	@Autowired
	private AppService service;
	@RequestMapping(method=RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String act = request.getParameter("act");
		try {
			if (act != null) {
				if (validRandom(request)) {
					genHalfProxy(request, response);
				}
			} else {
				sendRandom(request, response);
			}
		} catch (DecodingException e) {
			throw new ServletException(e);
		}
	}
	@RequestMapping(method=RequestMethod.POST)
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void genHalfProxy(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String appname = request.getParameter("appname");
		Application app = service.getApplication(appname);

		AppCredContent content = new AppCredContent();
		content.setAppKeyId(app.getKeyid());
		content.setIpAddress(RequestUtil.getRemoteIP(request));
		content.setPrincipal(new AppPrincipal(appname));
		Date tommorow = DateUtils.addDays(new Date(), 1);
		content.setValidTime(DateFormatUtils.format(tommorow,
				"yyyy-MM-dd hh:mm:ss"));

		Application umt = service.getApplication("umt");
		content.setUMTKeyId(umt.getKeyid());

		SignedEnvelope env = new SignedEnvelope(content.toXML());

		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println(env.toXML());
		out.flush();
		out.close();
	}

	private boolean validRandom(HttpServletRequest request)
			throws UnsupportedEncodingException, DecodingException {
		String appname = request.getParameter("appname");
		String random = (String) request.getSession().getAttribute(
				"proxy_random");
		String signed = request.getParameter("signed");
		if (appname != null && random != null && signed != null) {
			byte[] decoded = Base64.decode(signed);

			Application app = service.getApplication(appname);
			if (app != null) {
				KeyFile kf = new KeyFile();
				RSAKey key = kf.loadFromString(app.getPublicKey());
				return key.verify(random.getBytes("UTF-8"), decoded);
			}
		}

		return false;
	}

	private void sendRandom(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		RandomUtil ru = new RandomUtil();
		String random = ru.getRandom(20);
		request.getSession().setAttribute("proxy_random", random);
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.print(random);
		out.flush();
		out.close();
	}

}
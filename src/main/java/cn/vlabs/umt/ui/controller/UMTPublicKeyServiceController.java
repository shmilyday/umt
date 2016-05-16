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

import cn.vlabs.duckling.common.crypto.HexUtil;
import cn.vlabs.duckling.common.transmission.PublicKeyEnvelope;
import cn.vlabs.umt.ui.servlet.login.UMTCredential;
@Controller
@RequestMapping("/getUMTPublicKey")
@Deprecated
//旧的登录接口中用于开放证书下载用，已逐渐放弃旧接口
public class UMTPublicKeyServiceController  {
	@Autowired
	private UMTCredential cred;
	@RequestMapping(method=RequestMethod.GET)
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PublicKeyEnvelope opublickey = new PublicKeyEnvelope();
		opublickey.setAppId(cred.getUMTId());
		Date month=DateUtils.addMonths(new Date(cred.getCreateTime()), 1);
		opublickey.setValidTime(DateFormatUtils.format(month, "yyyy-MM-dd hh:mm:ss"));
		opublickey.setPublicKey(HexUtil.toHexString(cred.getUMTKey().getRSAPublic().getEncoded()));
		response.setContentType("text/xml");
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.write(opublickey.toXML());
		out.flush();
		out.close();
	}
}
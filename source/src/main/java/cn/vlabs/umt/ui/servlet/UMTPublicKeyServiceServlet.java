/*
 * Copyright (c) 2008-2013 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
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
package cn.vlabs.umt.ui.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.duckling.common.crypto.HexUtil;
import cn.vlabs.duckling.common.transmission.PublicKeyEnvelope;
import cn.vlabs.umt.ui.Attributes;

public class UMTPublicKeyServiceServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		UMTCredential cred= (UMTCredential)factory.getBean("UMTCredUtil");
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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	public void init() throws ServletException {
		factory = (BeanFactory) getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
	private BeanFactory factory;
}
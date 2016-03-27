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
package cn.vlabs.umt.ui.admin;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;
import cn.vlabs.umt.common.util.PageBean;
import cn.vlabs.umt.services.site.AppService;
import cn.vlabs.umt.services.site.Application;

@Controller
@RequestMapping("/admin/manageApplication")
@Deprecated
public class ManageApplicationController {
	private static Logger log = Logger.getLogger(ManageApplicationController.class);
	@Autowired
	private AppService service;
	@RequestMapping(params = "act=add")
	public String add(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute Application app) {
		service.createApplication(app);
		String forward = showApps(request, response);
		PageBean bean = (PageBean) request.getAttribute("PageBean");
		bean.setCurrentPage(bean.getPageCount() - 1);
		return forward;
	}
	private String escape(String val) {
		if (val != null)
			return val;
		else
			return "";
	}
	@RequestMapping(params = "act=loadApp")
	public void loadApp(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		String idStr = request.getParameter("q");
		int appid = Integer.parseInt(idStr);
		Application app = service.getApplication(appid);
		String json = "";
		if (app != null) {
			json = "[{" + "appname:'" + escape(app.getName()) + "'" + ",url:'"
					+ escape(app.getUrl()) + "'" + ",description:'"
					+ escape(app.getDescription()) + "'" + ",serverType:'"
					+ escape(app.getServerType()) + "'" + ",allowOperate:"
					+ app.isAllowOperate() + "}]";
		}
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.close();
		writer.flush();
	}
	@RequestMapping(params = "act=modifyApp")
	public String modifyApp(HttpServletRequest request,
			HttpServletResponse response, @RequestParam Application app) {
		service.updateApplcation(app);

		String forward = showApps(request, response);
		PageBean bean = (PageBean) request.getAttribute("PageBean");
		bean.setCurrentPage(bean.getPageCount() - 1);
		return forward;
	}
	private String readToString(MultipartFile file)
			throws UnsupportedEncodingException, FileNotFoundException,
			IOException {
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(
				file.getInputStream(), "UTF-8"));
		try {
			String line = null;
			boolean first = true;
			while ((line = reader.readLine()) != null) {
				if (!first) {
					buffer.append("\n");
				} else {
					first = false;
				}
				buffer.append(line);
			}
		} finally {
			reader.close();
		}
		return buffer.toString();
	}

	@RequestMapping(params = "act=remove")
	public String remove(HttpServletRequest request,
			HttpServletResponse response) {
		String[] appids = request.getParameterValues("appids");
		if (appids != null) {
			for (String id : appids) {
				try {
					int appid = Integer.parseInt(id);
					service.deleteApplication(appid);
				} catch (NumberFormatException e) {

				}
			}
		}
		return showApps(request, response);
	}

	@RequestMapping
	public String showApps(HttpServletRequest request,
			HttpServletResponse response) {
		int currentPage = 0;
		String currentPageString = request.getParameter("page");
		if (currentPageString != null) {
			try {
				currentPage = Integer.parseInt(currentPageString);
			} catch (NumberFormatException e) {
				currentPage = 0;
			}
		}
		int total = service.getApplicationCount();

		PageBean bean = new PageBean(total);
		bean.setCurrentPage(currentPage);
		bean.setItems(service.getAllApplication(bean.getStart(),
				bean.getRecordPerPage()));

		request.setAttribute("PageBean", bean);
		return "/admin/appmanage";
	}

	@RequestMapping(params = "act=updateKey")
	public String updatekey(HttpServletRequest request,
			HttpServletResponse response, @RequestParam int appid,
			@RequestParam("keyfile") MultipartFile file) {

		if (file.getSize() <= 20000) {
			String publickey;
			try {
				publickey = readToString(file);
				KeyFile kf = new KeyFile();
				try {
					RSAKey key = kf.loadFromString(publickey);
					if (key != null) {
						service.updatePublicKey(appid, publickey);
					} else {
						request.setAttribute("error",
								"appmanage.error.keyfile.wrongformat");
					}
				} catch (Exception e) {
					request.setAttribute("error",
							"appmanage.error.keyfile.wrongformat");
					log.debug("information", e);
				}
			} catch (UnsupportedEncodingException e) {
				// Imporsible
			} catch (IOException e) {
				request.setAttribute("error", "appmanage.error.exception");
				log.debug("information", e);
			}
		}
		return showApps(request, response);
	}
}
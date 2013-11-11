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
package cn.vlabs.umt.ui.actions;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.duckling.common.crypto.KeyFile;
import cn.vlabs.duckling.common.crypto.impl.RSAKey;
import cn.vlabs.umt.common.util.PageBean;
import cn.vlabs.umt.services.site.AppService;
import cn.vlabs.umt.services.site.Application;
import cn.vlabs.umt.ui.Attributes;

/** 
 * MyEclipse Struts
 * Creation date: 12-18-2009
 * 
 * XDoclet definition:
 * @struts.action path="/manageApplication" name="manageApplicationForm" input="/appmanage.jsp" scope="request" validate="true"
 */
public class ManageApplicationAction extends DispatchAction {
	public ActionForward remove(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		AppService service = getAppService();
		String[] appids = request.getParameterValues("appids");
		if (appids!=null){
			for (String id:appids){
				try{
					int appid = Integer.parseInt(id);
					service.deleteApplication(appid);
				}catch (NumberFormatException e){
					
				}
			}
		}
		return showApps(mapping, form, request, response);
	}
	
	public ActionForward modifyApp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		AppService service = getAppService();
		ManageApplicationForm manageApplicationForm = (ManageApplicationForm) form;
		
		Application app = manageApplicationForm.getApplication();
		service.updateApplcation(app);
		
		ActionForward forward= showApps(mapping, form, request, response);
		PageBean bean = (PageBean)request.getAttribute("PageBean");
		bean.setCurrentPage(bean.getPageCount()-1);
		return forward;
	}
	private String escape(String val){
		if (val!=null)
			return val;
		else
			return "";
	}
	public ActionForward loadApp(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		String idStr=request.getParameter("q");
		AppService service = getAppService();
		int appid=Integer.parseInt(idStr);
		Application app = service.getApplication(appid);
		String json ="";
		if (app!=null){
			json ="[{" 
					+"appname:'"+escape(app.getName())+"'"
					+",url:'"+escape(app.getUrl())+"'"
					+",description:'"+escape(app.getDescription())+"'"
					+",serverType:'"+escape(app.getServerType())+"'"
					+",allowOperate:"+app.isAllowOperate()
					+"}]";
		}
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(json);
		writer.close();
		writer.flush();
		return null;
	}
	public ActionForward add(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		AppService service = getAppService();
		ManageApplicationForm manageApplicationForm = (ManageApplicationForm) form;
		
		Application app = manageApplicationForm.getApplication();
		service.createApplication(app);
		ActionForward forward= showApps(mapping, form, request, response);
		PageBean bean = (PageBean)request.getAttribute("PageBean");
		bean.setCurrentPage(bean.getPageCount()-1);
		return forward;
	}

	private AppService getAppService() {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		AppService service = (AppService) factory.getBean("ApplicationService");
		return service;
	}
	
	public ActionForward updatekey(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		AppService service = getAppService();
		ManageApplicationForm manageApplicationForm = (ManageApplicationForm) form;
		
		int appid = manageApplicationForm.getAppid();
		FormFile file = manageApplicationForm.getKeyfile();
		if (file.getFileSize()<=20000){
			String publickey;
			try {
				publickey = readToString(file);
				KeyFile kf = new KeyFile();
				try{
					RSAKey key = kf.loadFromString(publickey);
					if (key!=null){
						service.updatePublicKey(appid, publickey);
					}else{
						request.setAttribute("error", "appmanage.error.keyfile.wrongformat");
					}
				}catch (Exception e){
					request.setAttribute("error", "appmanage.error.keyfile.wrongformat");
					log.debug("information", e);
				}
			} catch (UnsupportedEncodingException e) {
				//Imporsible
			} catch (IOException e) {
				request.setAttribute("error","appmanage.error.exception");
				log.debug("information", e);
			}
		}
		return showApps(mapping, form, request, response);
	}
	
	private String readToString(FormFile file) throws UnsupportedEncodingException, FileNotFoundException, IOException{
		StringBuffer buffer = new StringBuffer();
		BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), "UTF-8"));
		try{
			String line =null;
			boolean first=true;
			while ((line=reader.readLine())!=null){
				if (!first){
					buffer.append("\n");
				}else{
					first=false;
				}
				buffer.append(line);
			}
		}finally{
			reader.close();
		}
		return buffer.toString();
	}
	public ActionForward showApps(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		ManageApplicationForm manageApplicationForm = (ManageApplicationForm) form;
		AppService service = getAppService();
		int total = 0;
		int currentPage=0;
		if (manageApplicationForm.getPage()!=null){
			try{
				currentPage =Integer.parseInt(manageApplicationForm.getPage());
			}catch (NumberFormatException e){
				currentPage=0;
			}
		}
		total = service.getApplicationCount();
		
		PageBean bean = new PageBean(total);
		bean.setCurrentPage(currentPage);
		bean.setItems(service.getAllApplication(bean.getStart(), bean.getRecordPerPage()));
		
		request.setAttribute("PageBean", bean);
		return mapping.getInputForward();
	}
	private static Logger log = Logger.getLogger(ManageApplicationAction.class);
}
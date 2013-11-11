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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.apache.struts.upload.FormFile;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.PageBean;
import cn.vlabs.umt.common.xls.RecordFile;
import cn.vlabs.umt.common.xls.UserVO;
import cn.vlabs.umt.common.xls.UserXLSParser;
import cn.vlabs.umt.common.xls.XLSException;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.MessagePage;

/** 
 * MyEclipse Struts
 * Creation date: 12-28-2009
 * 
 * XDoclet definition:
 * @struts.action path="/batchCreate" name="batchCreateForm" input="/admin/batch.jsp" parameter="act" scope="request" validate="true"
 */
public class BatchCreateAction extends DispatchAction {
	public ActionForward save(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BatchCreateForm batchCreateForm = (BatchCreateForm) form;
		FormFile formfile = batchCreateForm.getFile();
		if (formfile!=null){
			try {
				readAllToUser(request.getSession(), formfile);
			} catch (Exception e) {
				request.setAttribute("message", e.getMessage());
				return mapping.getInputForward();
			}
		}
		return mapping.findForward("changePage");
	}
	
	public ActionForward discard(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		cleanup(request);
		return mapping.getInputForward();
	}

	private void cleanup(HttpServletRequest request) {
		HttpSession session = request.getSession();
		RecordFile records = (RecordFile)session.getAttribute("batch.records");
		if (records!=null){
			String datadir=records.getDatadir();
			try {
				FileUtils.deleteDirectory(new File(datadir));
			} catch (IOException e) {
				log.info("删除临时文件失败:"+e.getMessage());
			}
			session.removeAttribute("batch.records");
			session.removeAttribute("batch.total");
		}
	}
	
	public ActionForward create(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		RecordFile records = (RecordFile)session.getAttribute("batch.records");
		for (int i=0;i<records.getPageCount();i++){
			try {
				ArrayList<UserVO> users = records.load(i);
				ArrayList<User> toCreate=new ArrayList<User>();
				for (UserVO user:users){
					if (!user.isExists()){
						toCreate.add(user);
					}
				}
				UserService us = getUserService();	
				us.create(toCreate);
			} catch (IOException e) {
			}
		}
		cleanup(request);
		return mapping.getInputForward();
	}
	public ActionForward changePage(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession();
		Integer total = (Integer)session.getAttribute("batch.total");
		RecordFile records = (RecordFile)session.getAttribute("batch.records");
		if (total==null || records==null){
			return MessagePage.showMenuMessagePage("batch.datanotfound", request, mapping);
		}
		
		PageBean bean = new PageBean(total);
		String page = request.getParameter("page");
		try{
			bean.setCurrentPage(Integer.parseInt(page));
		}catch(NumberFormatException e){
			bean.setCurrentPage(0);
		}
		
		try {
			bean.setItems(records.load(bean.getCurrentPage()));
			request.setAttribute("PageBean", bean);
			return mapping.findForward("displayusers");
		} catch (IOException e) {
			request.setAttribute("message", e.getMessage());
			return mapping.findForward("displayusers");
		}
	}
	
	private UserService getUserService(){
		BeanFactory factory = (BeanFactory)getServlet().getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		return (UserService) factory.getBean("UserService");
	}
	
	private void readAllToUser(HttpSession session, FormFile file) throws IOException, XLSException{
		
		UserService us = getUserService();
		RecordFile records = new RecordFile(getServlet().getServletContext());
		
		int realsize = 0;
		InputStream in = null;
		try{
			in = file.getInputStream();
			UserXLSParser parser = new UserXLSParser(in);
			int total = parser.getCount();
			int page = total/recordPerFile+((total%recordPerFile)>0?1:0);
			for (int i=0;i<page;i++){
				List<UserVO> users = parser.readUsers(i*recordPerFile, recordPerFile);
				checkExist(users, us);
				records.save(i, users);
				realsize = realsize+users.size();
			}
		}finally{
			if (in!=null)
			{
				in.close();
			}
		}
		records.setPageCount(realsize/recordPerFile+((realsize%recordPerFile)>0?1:0));
		session.setAttribute("batch.records", records);
		session.setAttribute("batch.total", realsize);
	}
	
	private void checkExist(List<UserVO> users, UserService us){
		String[] usernames = new String[users.size()];
		for (int i=0;i<users.size();i++){
			usernames[i]=users.get(i).getUmtId();
		}
		
		Set<String> exists= us.isExist(usernames);
		
		for (UserVO user:users){
			if (exists.contains(user.getUmtId())){
				user.setExists(true);
			}else{
				user.setExists(false);
			}
		}
	}
	
	private static final Logger log = Logger.getLogger(BatchCreateAction.class);
	private int recordPerFile=20;
}
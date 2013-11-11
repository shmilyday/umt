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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.actions.DispatchAction;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.PageBean;
import cn.vlabs.umt.common.xls.RecordFile;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.MessagePage;

public class ManageUserAction extends DispatchAction {
	private static Logger LOG=Logger.getLogger(ManageUserAction.class);
	public ActionForward createUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		UserService us = (UserService) factory.getBean("UserService");
		ManageUserForm userform = (ManageUserForm) form;
		try {
			int uid=us.create(userform.getUser(),LoginNameInfo.STATUS_ACTIVE);
			us.updateValueByColumn(uid, "security_email", userform.getUsername());
			
		} catch (InvalidUserNameException e) {
			LOG.error("User "+e.getUser().getCstnetId()+" can't be created, for wrong user name format.");
		}
		return showUsers(mapping, form, request, response);
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
	
	public ActionForward search(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		String query = request.getParameter("q");
		if (query!=null){
			ServletContext context = getServlet().getServletContext();
			BeanFactory factory = (BeanFactory)context.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
			UserService us = (UserService) factory.getBean("UserService");
			Collection<User> users = us.search(query, 0, 10);
			
			//转锟斤拷锟斤拷JSON
			StringBuffer json = new StringBuffer();
			boolean first=true;
			json.append("[");
			if (users!=null){
				for (User user:users){
					if (!first){
						json.append(",");
					}else{
						first=false;
					}
					json.append("{u:'"+user.getUmtId()+"'");
					json.append(", t:'"+user.getTrueName()+"'}");
				}
			}
			json.append("]");
			
			//锟斤拷锟絁SON
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			PrintWriter writer = response.getWriter();
			writer.print(json.toString());
			writer.flush();
			writer.close();
		}
		return null;
	}
	public ActionForward removeUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserService us = ServiceFactory.getUserService(request);
		String[] usernames = request.getParameterValues("userids");
		us.remove(CommonUtils.stringArray2IntArray(usernames));
		return showUsers(mapping, form, request, response);
	}
	
	public ActionForward modify(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserService us = ServiceFactory.getUserService(request);
		ManageUserForm userform = (ManageUserForm) form;
		User user=us.getUserByLoginName(userform.getUsername());
		us.updateValueByColumn(user.getId(), "true_name", userform.getTruename());
		return showUsers(mapping, form, request, response);
	}
	
	public ActionForward resetPassword(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		UserService us = ServiceFactory.getUserService(request);
		ManageUserForm userform = (ManageUserForm) form;
		User user=us.getUserByLoginName(userform.getUsername());
		if(user==null||user.getType()==null){
			throw new RuntimeException("user not found or auth by is unkown");
		}else if(User.USER_TYPE_UMT.equals(user.getType())||User.USER_TYPE_MAIL_AND_UMT.equals(user.getType())){
			us.updatePassword(user.getId(), userform.getPassword());
		}
		return showUsers(mapping, form, request, response);
	}
	
	public ActionForward loadUser(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		BeanFactory factory = (BeanFactory) getServlet().getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		UserService us = (UserService) factory.getBean("UserService");
		String username=request.getParameter("q");
		User u = us.getUserByUid(Integer.valueOf(username));
		
		//转锟斤拷锟斤拷JSON
		StringBuffer buffer = new StringBuffer();
		buffer.append("[{");
		buffer.append("username:'"+u.getCstnetId()+"'");
		buffer.append(",truename:'"+u.getTrueName()+"'");
		buffer.append(",email:'"+u.getCstnetId()+"'");
		buffer.append("}]");
		
		//锟斤拷锟�
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(buffer.toString());
		writer.flush();
		writer.close();
		return null;
	}
	
	public ActionForward showUsers(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String query = request.getParameter("query");
		Pagable pagable = createPagable(query);
		
		ManageUserForm userform = (ManageUserForm) form;
			int total=0;
			int currentPage=0;
			if (userform.getTotal() != null) {
				try {
					total = Integer.parseInt(userform.getTotal());
					currentPage=Integer.parseInt(userform.getPage());
				} catch (NumberFormatException e) {
					total = pagable.getCount();
				}
			}else{
				total=pagable.getCount();
			}
		PageBean bean = new PageBean(total);
		bean.setQuery(query);
		
		bean.setCurrentPage(currentPage);
		bean.setItems(pagable.getRecords(bean.getStart(), bean.getRecordPerPage()));
		request.setAttribute("PageBean", bean);
		return mapping.getInputForward();
	}
	
	private Pagable createPagable(String query){
		BeanFactory factory = (BeanFactory) getServlet().getServletContext()
		.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		UserService us = (UserService) factory.getBean("UserService");
		if (query!=null && query.trim().length()>0){
			return new QueryUsers(us, query);
		}else{
			return new AllUsers(us);
		}
	}
	private static interface Pagable{
		int getCount();
		Collection<User> getRecords(int start, int count);
	}
	
	private static class AllUsers implements Pagable{
		public AllUsers(UserService us){
			this.us=us;
		}
		public int getCount() {
			return us.getUserCount();
		}

		public Collection<User> getRecords(int start, int count) {
			return us.getUsers(start, count);
		}
		
		private UserService us;
	}
	private static class QueryUsers implements Pagable{
		public QueryUsers(UserService us, String query){
			this.us=us;
			this.query=query;
		}
		public int getCount() {
			return us.searchCount(query);
		}

		public Collection<User> getRecords(int start, int count) {
			return us.search(query, start, count);
		}
		private String query;
		private UserService us;
	}
}
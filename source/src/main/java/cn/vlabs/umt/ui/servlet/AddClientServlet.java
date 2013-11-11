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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;
import cn.vlabs.umt.ui.UMTContext;

public class AddClientServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8669326740449452809L;
	private IOauthClientService oauthClientServer;
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		doGet(req, resp);
	}
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String act = req.getParameter("act");
		resp.setCharacterEncoding("utf-8");
		if("add".equals(act)){
			addClient(req,resp);
			return;
		}else if("clientIdUsed".equals(act)){
			clientIdUsed(req,resp);
			return;
		}else if("update".equals(act)){
			updateClient(req,resp);
			return;
		}else if("getClientInfo".equals(act)){
			getClientInfo(req,resp);
			return;
		}else if("delete".equals(act)){
			deleteClient(req,resp);
		}else if("refresh".equals(act)){
			refreshClient(req);
		}
		req.setAttribute("clients", getAllClient());
		req.getRequestDispatcher("/admin/oauthaddcliend.jsp").forward(req, resp);
	}
	private void clientIdUsed(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, IOException{
		String clientId=request.getParameter("client_id");
		String type=request.getParameter("type");
		boolean flag=false;
		if("update".equals(type)){
			flag=true;
		}else{
			flag=getClientServer().findByClientId(clientId)==null;
		}
		response.getWriter().print(flag);
	}
	
	private void getClientInfo(HttpServletRequest req, HttpServletResponse resp){
		String clientId = req.getParameter("client_id");
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(clientId)){
			result.put("result", false);
			result.put("message", "client_id不能为空");
			writeJSONObject(resp, result);
			return;
		}
		OauthClientBean b=getClientServer().findByClientId(clientId);
		if(b==null){
			result.put("result", false);
			result.put("message", "client_id "+clientId+" 不已存在");
			writeJSONObject(resp, result);
			return;
		}else{
			result.put("result", true);
			result.put("client_id",b.getClientId());
			result.put("client_secret",b.getClientSecret());
			result.put("redirect_URI",b.getRedirectURI());
			result.put("client_name",b.getClientName());
			result.put("third_party",b.getThirdParty());
			result.put("client_status", b.getStatus());
			result.put("client_website",b.getClientWebsite());
			result.put("app_type",b.getAppType());
			writeJSONObject(resp, result);
		}
	}
	
	private void refreshClient(HttpServletRequest req) {
		String id = req.getParameter("id");
		OauthClientBean bean = getClientServer().findById(Integer.valueOf(id));
		if(bean!=null){
			getClientServer().deleteFromCache(bean);
		}
	}
	private void deleteClient(HttpServletRequest req, HttpServletResponse resp) {
		String id = req.getParameter("id");
		getClientServer().delete(Integer.parseInt(id));
	}

	private void addClient(HttpServletRequest req, HttpServletResponse resp) {
		JSONObject result = new JSONObject();
		String clientId = trim(req.getParameter("client_id"));
		String cleintSecret = trim(req.getParameter("client_secret"));
		String redirectURI  = trim(req.getParameter("redirect_URI"));
		String clientName = trim(req.getParameter("client_name"));
		String thirdParty = trim(req.getParameter("third_party"));
		String clientWebsite = trim(req.getParameter("client_website"));
		String appType=trim(req.getParameter("app_type"));
		StringBuilder sb = null;
		if(StringUtils.isNotEmpty(cleintSecret)&&StringUtils.isNotEmpty(clientId)&&StringUtils.isNotEmpty(redirectURI)){
			OauthClientBean b=getClientServer().findByClientId(clientId);
			if(b!=null){
				result.put("result", false);
				result.put("message", "client_id "+clientId+" 已存在");
				writeJSONObject(resp, result);
				return;
			}else{
				OauthClientBean bean = new OauthClientBean();
				bean.setClientId(clientId);
				bean.setClientSecret(cleintSecret);
				bean.setRedirectURI(redirectURI);
				bean.setClientName(clientName);
				bean.setStatus(OauthClientBean.STATUS_ACCEPT);
				bean.setThirdParty(thirdParty);
				bean.setClientWebsite(clientWebsite);
				bean.setUid(SessionUtils.getUserId(req));
				bean.setAppType(appType);
				int i=getClientServer().save(bean,true);
				if(i<0){
					result.put("result", false);
					result.put("message", "保存失败");
					writeJSONObject(resp, result);
				}else{
					result.put("result", true);
					result.put("message", "保存失败");
					writeJSONObject(resp, result);
					return;
				}
			}
		}
		if(sb==null){
			sb = new StringBuilder();
		}
		if(StringUtils.isEmpty(clientId)){
			sb.append("client_id不能为空 ");
		}
		if(StringUtils.isEmpty(cleintSecret)){
			sb.append("cleint_secret不能为空 ");
		}
		if(StringUtils.isEmpty(redirectURI)){
			sb.append("redirect uri 不能为空 ");
		}
		if(StringUtils.isEmpty(clientName)){
			sb.append(" client_name 不能为空");
		}
		if(sb!=null&&sb.length()>0){
			result.put("result", false);
			result.put("message", sb.toString());
			writeJSONObject(resp, result);
			return;
		}
	}
	private String trim(String s){
		if(StringUtils.isEmpty(s)){
			return s;
		}
		return s.trim();
	}
	
	public void updateClient(HttpServletRequest req, HttpServletResponse resp){
		JSONObject result = new JSONObject();
		String clientId = trim(req.getParameter("client_id"));
		String cleintSecret = trim(req.getParameter("client_secret"));
		String redirectURI  = trim(req.getParameter("redirect_URI"));
		String clientName = trim(req.getParameter("client_name"));
		String thirdParty = trim(req.getParameter("third_party"));
		String clientWebsite = trim(req.getParameter("client_website"));
		String clientStatus=trim(req.getParameter("client_status"));
		String appType=trim(req.getParameter("app_type"));
		StringBuilder sb = null;
		if(StringUtils.isNotEmpty(cleintSecret)&&StringUtils.isNotEmpty(clientId)&&StringUtils.isNotEmpty(redirectURI)){
			OauthClientBean bean=getClientServer().findByClientId(clientId);
			if(bean==null){
				result.put("result", false);
				result.put("message", "client_id "+clientId+" 不已存在，不能进行更新");
				writeJSONObject(resp, result);
				return;
			}else{
				String beforeStatus=bean.getStatus();
				bean.setClientId(clientId);
				bean.setClientSecret(cleintSecret);
				bean.setRedirectURI(redirectURI);
				bean.setClientName(clientName);
				bean.setStatus(clientStatus);
				bean.setThirdParty(thirdParty);
				bean.setClientWebsite(clientWebsite);
				bean.setAppType(appType);
				getClientServer().update(bean);
				if(!clientStatus.equals(beforeStatus)){
					UMTContext context=new UMTContext(req);
					getClientServer().sendAdminToDevelop(context.getLocale(), bean, ServiceFactory.getUserService(req).getUserByUid(bean.getUid()));
				}
				result.put("result", true);
				result.put("message", "保存成功");
				writeJSONObject(resp, result);
				return;
			}
		}
		if(sb==null){
			sb = new StringBuilder();
		}
		if(StringUtils.isEmpty(clientId)){
			sb.append("client_id不能为空 ");
		}
		if(StringUtils.isEmpty(cleintSecret)){
			sb.append("client_secret不能为空 ");
		}
		if(StringUtils.isEmpty(redirectURI)){
			sb.append("redirect uri 不能为空 ");
		}
		if(StringUtils.isEmpty(clientName)){
			sb.append(" client_name 不能为空");
		}
		if(sb!=null&&sb.length()>0){
			result.put("result", false);
			result.put("message", sb.toString());
			writeJSONObject(resp, result);
			return;
		}
	}
	
	
	private List<OauthClientBean> getAllClient(){
		IOauthClientService client = getClientServer();
		return client.getAll();
		
	}
	private IOauthClientService getClientServer(){
		if(oauthClientServer ==null){
			initClientServer();
		}
		return oauthClientServer;
	}
	
	private synchronized void initClientServer() {
		if(oauthClientServer ==null){
			oauthClientServer = (IOauthClientService)getBeanFactory().getBean(IOauthClientService.BEAN_ID);
		}
	}

	private BeanFactory getBeanFactory(){
		return (BeanFactory) getServletContext()
				.getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
	}
	public static void writeJSONObject(HttpServletResponse response,Object object) {
		PrintWriter writer = null;
		try {
			//为了兼容IE系浏览器，特意设置成text/html格式
			response.setContentType("text/html");
			writer = response.getWriter();
			writer.write(object.toString());
		} catch (IOException e) {
		}finally {
			if (writer!=null){
				writer.flush();
				writer.close();
			}
		}
	}
}
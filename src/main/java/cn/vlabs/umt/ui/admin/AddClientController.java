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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.UMTContext;

@Controller
@RequestMapping("/admin/addClient")
public class AddClientController {
	@Autowired
	private IOauthClientService oauthClientServer;
	@RequestMapping
	public String service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		req.setAttribute("clients", getAllClient());
		return "/admin/oauthaddcliend";
	}
	@RequestMapping(params="act=removeLogo")
	public void removeLogo(HttpServletRequest request, HttpServletResponse response) {
		int beanId=Integer.parseInt(request.getParameter("beanId"));
		OauthClientBean ocb=new OauthClientBean();
		ocb.setId(beanId);
		String target=request.getParameter("target");
		switch(target){
			case "100p":{
				oauthClientServer.removeLogo(ocb, true, false, false, false);
				break;
			}
			case "64p":{
				oauthClientServer.removeLogo(ocb, false,true, false, false);
				break;
			}
			case "32p":{
				oauthClientServer.removeLogo(ocb, false,false, true, false);
				break;
			}
			case "16p":{
				oauthClientServer.removeLogo(ocb, false,false, false, true);
				break;
			}
		}
	}
	@RequestMapping(params="act=clientIdUsed")
	public void clientIdUsed(HttpServletRequest request, HttpServletResponse response) throws NumberFormatException, IOException{
		response.setCharacterEncoding("utf-8");
		String clientId=request.getParameter("client_id");
		String type=request.getParameter("type");
		boolean flag=false;
		if("update".equals(type)){
			flag=true;
		}else{
			flag=oauthClientServer.findByClientId(clientId)==null;
		}
		response.getWriter().print(flag);
	}
	@RequestMapping(params="act=getClientInfo")
	public void getClientInfo(HttpServletRequest req, HttpServletResponse resp){
		resp.setCharacterEncoding("utf-8");
		String clientId = req.getParameter("client_id");
		JSONObject result = new JSONObject();
		if(StringUtils.isEmpty(clientId)){
			result.put("result", false);
			result.put("message", "client_id不能为空");
			writeJSONObject(resp, result);
			return;
		}
		OauthClientBean b=oauthClientServer.findByClientId(clientId);
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
			result.put("pwd_type", b.getPwdType());
			result.put("app_type",b.getAppType());
			result.put("scope", b.getScope());
			result.put("needOrgInfo",b.getNeedOrgInfo());
			result.put("enableAppPwd", b.getEnableAppPwd());
			result.put("compulsionStrongPwd", b.isCompulsionStrongPwd());
			writeJSONObject(resp, result);
		}
	}
	@RequestMapping(params="act=refresh")
	public String refreshClient(HttpServletRequest req) {
		String id = req.getParameter("id");
		OauthClientBean bean =oauthClientServer.findById(Integer.valueOf(id));
		if(bean!=null){
			oauthClientServer.deleteFromCache(bean);
		}
		req.setAttribute("clients", getAllClient());
		return "/admin/oauthaddcliend";
	}
	@RequestMapping(params="act=delete")
	public String deleteClient(HttpServletRequest req, HttpServletResponse resp) {
		String id = req.getParameter("id");
		oauthClientServer.delete(Integer.parseInt(id));
		req.setAttribute("clients", getAllClient());
		return "/admin/oauthaddcliend";
	}
	@RequestMapping(params="act=add")
	public void addClient(HttpServletRequest req, HttpServletResponse resp) {
		resp.setCharacterEncoding("utf-8");
		JSONObject result = new JSONObject();
		String clientId = trim(req.getParameter("client_id"));
		String cleintSecret = trim(req.getParameter("client_secret"));
		String redirectURI  = trim(req.getParameter("redirect_URI"));
		String clientName = trim(req.getParameter("client_name"));
		String thirdParty = trim(req.getParameter("third_party"));
		String clientWebsite = trim(req.getParameter("client_website"));
		String appType=trim(req.getParameter("app_type"));
		String pwdType=trim(req.getParameter("pwd_type"));
		String[] scope=req.getParameterValues("scope");
		String needOrgInfo=req.getParameter("need_org_info");
		String enableAppPassword=req.getParameter("enableAppPwd");
		String compulsionStrongPwd=req.getParameter("compulsionStrongPwd");
		String scopeStr=null;
		if(!CommonUtils.isNull(scope)){
			scopeStr=Arrays.toString(scope);
			scopeStr=scopeStr.substring(1,scopeStr.length()-1);
		}
		StringBuilder sb = null;
		if(StringUtils.isNotEmpty(cleintSecret)&&StringUtils.isNotEmpty(clientId)&&StringUtils.isNotEmpty(redirectURI)){
			OauthClientBean b=oauthClientServer.findByClientId(clientId);
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
				bean.setScope(scopeStr);
				bean.setClientName(clientName);
				bean.setStatus(OauthClientBean.STATUS_ACCEPT);
				bean.setThirdParty(thirdParty);
				bean.setClientWebsite(clientWebsite);
				bean.setUid(SessionUtils.getUserId(req));
				bean.setAppType(appType);
				bean.setPwdType(pwdType);
				bean.setEnableAppPwd(enableAppPassword);
				if(needOrgInfo!=null){
					bean.setNeedOrgInfo(Integer.parseInt(needOrgInfo));
				}else{
					bean.setNeedOrgInfo(0);
				}
				if(!CommonUtils.isNull(compulsionStrongPwd)){
					bean.setCompulsionStrongPwd(Boolean.parseBoolean(compulsionStrongPwd));
				}
				int i=oauthClientServer.save(bean,true);
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
	@RequestMapping(params="act=update")
	public void updateClient(HttpServletRequest req, HttpServletResponse resp){
		resp.setCharacterEncoding("utf-8");
		JSONObject result = new JSONObject();
		String clientId = trim(req.getParameter("client_id"));
		String cleintSecret = trim(req.getParameter("client_secret"));
		String redirectURI  = trim(req.getParameter("redirect_URI"));
		String clientName = trim(req.getParameter("client_name"));
		String thirdParty = trim(req.getParameter("third_party"));
		String clientWebsite = trim(req.getParameter("client_website"));
		String clientStatus=trim(req.getParameter("client_status"));
		String appType=trim(req.getParameter("app_type"));
		String pwdType=trim(req.getParameter("pwd_type"));
		String[] scope=req.getParameterValues("scope");
		String needOrgInfo=req.getParameter("need_org_info");
		String enableAppPwd=req.getParameter("enableAppPwd");
		String compulsionStrongPwd=req.getParameter("compulsionStrongPwd");
		String scopeStr=null;
		if(!CommonUtils.isNull(scope)){
			if(!CommonUtils.isNull(scope)){
				scopeStr=Arrays.toString(scope);
				scopeStr=scopeStr.substring(1,scopeStr.length()-1);
			}
		}
		StringBuilder sb = null;
		if(StringUtils.isNotEmpty(cleintSecret)&&StringUtils.isNotEmpty(clientId)&&StringUtils.isNotEmpty(redirectURI)){
			OauthClientBean bean=oauthClientServer.findByClientId(clientId);
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
				bean.setPwdType(pwdType);
				bean.setScope(scopeStr);
				bean.setEnableAppPwd(enableAppPwd);
				if(needOrgInfo!=null){
					bean.setNeedOrgInfo(Integer.parseInt(needOrgInfo));
				}else{
					bean.setNeedOrgInfo(0);
				}
				if(!CommonUtils.isNull(compulsionStrongPwd)){
					bean.setCompulsionStrongPwd(Boolean.parseBoolean(compulsionStrongPwd));
				}
				oauthClientServer.update(bean);
				if(!clientStatus.equals(beforeStatus)){
					UMTContext context=new UMTContext(req);
					oauthClientServer.sendAdminToDevelop(context.getLocale(), bean, ServiceFactory.getUserService(req).getUserByUid(bean.getUid()));
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
		return oauthClientServer.getAll();
		
	}

	private static void writeJSONObject(HttpServletResponse response,Object object) {
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
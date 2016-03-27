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
package cn.vlabs.umt.ui.user;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.common.util.UMTStringUtils;
import cn.vlabs.umt.common.util.UserAgent;
import cn.vlabs.umt.common.util.UserAgentUtil;
import cn.vlabs.umt.services.ca.CaService;
import cn.vlabs.umt.services.ca.DownloadCaException;
import cn.vlabs.umt.services.ca.KeyEmptyException;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.bean.CaApplication;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.service.impl.CaApplicationService;
import cn.vlabs.umt.services.user.service.impl.FileSaverBridge;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 账户管理
 * 
 * @author lvly
 * @since 2013-1-29
 */
@Controller
@RequestMapping("/user/digitalCertificate.do")
public class DigitalCertificateController {
	private static final Logger LOG=Logger.getLogger(DigitalCertificateController.class);
	
	@Autowired
	private CaApplicationService caApplicationService;
	@Autowired
	private CaService caService;
	@Autowired
	private LoginService loginService;
	/**
	 * 显示账户管理页面
	 * */
	@RequestMapping
	public String index(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		int uid=SessionUtils.getUserId(request);
		List<CaApplication> list=caApplicationService.getCaApplicationByUidAndType(uid, CaApplication.TYPE_EDUROMA, CaApplication.STATUS_NORMAL);
		request.setAttribute("caList", list);
		
		return "/user/digitalCertificate_index";
	}
	@RequestMapping(params = "act=manage")
	public String showManage(HttpServletRequest request,
			HttpServletResponse response) {
		
		int uid=SessionUtils.getUserId(request);
		List<CaApplication> list=caApplicationService.getCaApplicationByUidAndType(uid, CaApplication.TYPE_EDUROMA, CaApplication.STATUS_NORMAL);
		request.setAttribute("caList", list);
		
		return "/user/digitalCertificate_manage";
	}
	@RequestMapping(params = "act=help")
	public String help(HttpServletRequest request,
			HttpServletResponse response) {
		return "/user/digitalCertificate_help";
	}
	@RequestMapping(params = "act=record")
	public String record(HttpServletRequest request,
			HttpServletResponse response) {
		return "/user/digitalCertificate_record";
	}
	@RequestMapping(params = "act=applyView")
	public String applyView(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		String cn=createCN();
		String dn=caService.buildDN(cn);
		request.setAttribute("cn", cn);
		request.setAttribute("dn", dn);
		request.setAttribute("randPassword", UMTStringUtils.getRandString(6).toLowerCase());
		
		
		return "/user/digitalCertificate_manage_apply";
	}
	@RequestMapping(params = "act=apply")
	public String apply(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		
		String dn=request.getParameter("dn");
		String cn=request.getParameter("cn");
		String password=request.getParameter("password");
		User user=SessionUtils.getUser(request);
		
		
		boolean result=caService.submitCsrRequest(cn, password, user.getCstnetId(), dn);
		
		if(result){
			CaApplication ca=new CaApplication();
			ca.setCn(cn);
			ca.setDn(dn);
			ca.setExpirationOn(new Date());
			ca.setPassword(password);
			ca.setStatus(1);
			ca.setType(1);
			ca.setUid(SessionUtils.getUserId(request));
			ca.setValiFrom(new Date());
			caApplicationService.createCaApplication(ca);
			
			response.sendRedirect(RequestUtil.getContextPath(request)+"/user/digitalCertificate.do?act=manage");
			return null;
		}
		
		request.setAttribute("errorType", "applyError");
		
		
		
		return "/user/digitalCertificate_manage_error";
	}
	
	@RequestMapping(params = "act=remove")
	public String remove(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		
		int caId=Integer.parseInt(StringUtils.defaultIfEmpty(request.getParameter("caId"), "-1"));
		if(caId<=0){
			request.setAttribute("errorType", "paramError");
			
			return "/user/digitalCertificate_manage_error";
		}
		
		
		CaApplication ca=caApplicationService.getCaApplication(caId);
		
		if(ca!=null&&ca.getId()>0){
			int uid=SessionUtils.getUserId(request);
			if(uid!=ca.getUid()){
				request.setAttribute("errorType", "noPermission");
				return "/user/digitalCertificate_manage_error";
			}
			
			
			caApplicationService.removeCaApplication(caId);
		}
		
		
		
		
		response.sendRedirect(RequestUtil.getContextPath(request)+"/user/digitalCertificate.do?act=manage");
		return null;
	}
	
	
	
	
	
	private String createCN() {
		String uuid=UUID.randomUUID().toString();
		
		return StringUtils.replaceChars(uuid, "-", "");
		
	}
	
	
	@RequestMapping(params = "act=downloadView")
	public String downloadView(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		int caId=Integer.parseInt(StringUtils.defaultIfEmpty(request.getParameter("caId"), "-1"));
		if(caId<=0){
			request.setAttribute("errorType", "paramError");
			
			return "/user/digitalCertificate_manage_error";
		}
		
		CaApplication ca=caApplicationService.getCaApplication(caId);
		
		if(ca==null||ca.getId()<=0){
			request.setAttribute("errorType", "notExist");
			
			return "/user/digitalCertificate_manage_error";
		}
		
		int uid=SessionUtils.getUserId(request);
		if(uid!=ca.getUid()){
			request.setAttribute("errorType", "noPermission");
			return "/user/digitalCertificate_manage_error";
		}
		
		
		request.setAttribute("ca", ca);
		UserAgent userAgent=UserAgentUtil.getUserAgent(request.getHeader("user-agent"));
		if(userAgent!=null){
			request.setAttribute("userAgent", userAgent);
		}
		
		return "/user/digitalCertificate_manage_download";
	}
	
	@RequestMapping(params = "act=getPass")
	public void getPass(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String password=request.getParameter("password");
		if(StringUtils.isBlank(password)){
			JSONObject result=new JSONObject();
			result.put("result", "password_error");
			writeJSON(response, result);
			return;
		}
		
		UMTContext context=new UMTContext(request);
		User u=context.getCurrentUMTUser();
		boolean isPass = loginService.passwordRight(new UsernamePasswordCredential(u.getCstnetId(), password));
		
		if(!isPass){
			JSONObject result=new JSONObject();
			result.put("result", "password_error");
			writeJSON(response, result);
			return;
		}
		
		int caId= Integer.parseInt(StringUtils.defaultIfEmpty(request.getParameter("caId"), "0"));
		
		if(caId<1){
			JSONObject result=new JSONObject();
			result.put("result", "error");
			writeJSON(response, result);
			return;
		}
		
		CaApplication ca=caApplicationService.getCaApplication(caId);
		JSONObject result=new JSONObject();
		result.put("result", "true");
		result.put("password", ca.getPassword());
		writeJSON(response, result);
	}
	
	@RequestMapping(params = "act=download")
	public String download(HttpServletRequest request,
			HttpServletResponse response) {
		
		
		int caId=Integer.parseInt(StringUtils.defaultIfEmpty(request.getParameter("caId"), "-1"));
		if(caId<=0){
			request.setAttribute("errorType", "paramError");
			
			return "/user/digitalCertificate_manage_error";
		}
		
		CaApplication ca=caApplicationService.getCaApplication(caId);
		
		if(ca==null||ca.getId()<=0){
			request.setAttribute("errorType", "notExist");
			
			return "/user/digitalCertificate_manage_error";
		}
		
		
		
		try {
			if(StringUtils.equals(request.getParameter("type"), "cert")){
				InputStream in=caService.getCert(ca.getDn(), CaService.CERT_FORMAT_CER);
				FileSaverBridge saver=new FileSaverBridge(response,request);
				saver.save(ca.getCn()+".cer", in);
			}else if(StringUtils.equals(request.getParameter("type"), "key")){
				String keypair=caService.downloadKeypair(ca.getPassword(), ca.getDn());
				InputStream in=new ByteArrayInputStream(keypair.getBytes());
				FileSaverBridge saver=new FileSaverBridge(response,request);
				saver.save(ca.getCn()+".pem", in);
			}else if(StringUtils.equals(request.getParameter("type"), "all")){
				InputStream in=caService.getCertAll(ca.getPassword(), ca.getDn(), ca.getCn());
				FileSaverBridge saver=new FileSaverBridge(response,request);
				saver.save(ca.getCn()+".zip", in);
			}else{
				InputStream in=caService.downloadCertAndKeypairByKey(ca.getPassword(), ca.getDn(), CaService.CERT_FORRMAT_CERT_KEY_PKCS12);
				FileSaverBridge saver=new FileSaverBridge(response,request);
				saver.save(ca.getCn()+".p12", in);
			}
		} catch (IOException |DownloadCaException e) {
			LOG.error("下载证书文件出错",e);
			request.setAttribute("errorType", "downLoadError");
			return "/user/digitalCertificate_manage_error";
		} catch (KeyEmptyException e) {
			LOG.error("根据Dn获取证书key错误!",e);
			request.setAttribute("errorType", "getKeyError");
			return "/user/digitalCertificate_manage_error";
		}
		
		
		
		return null;
	}
	
	
	private void writeJSON(HttpServletResponse response,JSONObject result) throws IOException{
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(result.toString());
		writer.close();
		writer.flush();
	}
	
	
	
}
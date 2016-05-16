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

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginInfo;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 账户安全
 * @author lvly
 * @since 2013-1-29
 */
@Controller
@RequestMapping("/user/safe.do")
public class SafeController {
	@Autowired
	private IAccountService accountService;
	@Autowired
	private UserService userService;
	/**
	 * 显示账户安全
	 * */
	@RequestMapping
	public String showSecurity(HttpServletRequest request, HttpServletResponse response){
		return "/user/accountSafty_security";
	}
	//插入常用地
	@RequestMapping(params="act=addCommon")
	public void addCommon(HttpServletRequest request, HttpServletResponse response) throws IOException{
		UMTContext context=new UMTContext(request);
		int uid=context.getCurrentUMTUser().getId();
		String country=request.getParameter("country");
		String province=request.getParameter("province");
		String city=request.getParameter("city");
		UMTLog umtLog=new UMTLog();
		umtLog.setUid(uid);
		umtLog.setCountry(country);
		umtLog.setProvince(province);
		umtLog.setCity(city);
		String result="";
		if(CommonUtils.isNull(country)||CommonUtils.isNull(province)||CommonUtils.isNull(city)){
			result="param.error";
		}else if(accountService.isExitsCommonGEO(umtLog)){
			result="geo.exists";
		}else if(accountService.countCommonGEO(uid)>=5){
			result="too.many";
		}else{
			accountService.addCommonGEO(umtLog);
			result="add.success";
		}
		response.setContentType("text/html");
		response.getWriter().print(result);
	}
	
	private void removeEquals(List<UMTLog> list,List<UMTLog> exits){
		if(CommonUtils.isNull(list)){
			return;
		}
		if(CommonUtils.isNull(exits)){
			return;
		}
		Set<String> set=new HashSet<String>();
		for(UMTLog exit:exits){
			set.add(exit.getCountry()+"."+exit.getProvince()+"."+exit.getCity());
		}
		for(Iterator<UMTLog> it=list.iterator();it.hasNext();){
			UMTLog log=it.next();
			String key=log.getCountry()+"."+log.getProvince()+"."+log.getCity();
			if(set.contains(key)){
				it.remove();
			}
		}
	}
	//查询常用地
	@RequestMapping(params="act=searchCommon")
	public void searchCommon(HttpServletRequest request, HttpServletResponse response) throws IOException{
		UMTContext context=new UMTContext(request);
		int uid=context.getCurrentUMTUser().getId();
		List<UMTLog> list=accountService.readCommonGEO(uid);
		List<UMTLog> exits=accountService.getMyPreCommonUseGeos(uid);
		removeEquals(list, exits);
		String result="";
		JSONArray jsonArray=new JSONArray();
		if(CommonUtils.isNull(list)){
			result=jsonArray.toJSONString();
		}else{
			for(UMTLog log:list){
				if(log.isNullGEO()){
					continue;
				}
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("country",log.getCountry());
				jsonObject.put("province",log.getProvince());
				jsonObject.put("city",log.getCity());
				jsonObject.put("display", log.displayGEO());
				jsonArray.add(jsonObject);
			}
			result=jsonArray.toJSONString();
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		response.getWriter().print(result);
	}
	//删除常用地
	@RequestMapping(params="act=deleteCommon")
	public void deleteCommon(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String idStr=request.getParameter("id");
		boolean result=false;
		if(!CommonUtils.isNull(idStr)){
			int id=Integer.parseInt(idStr);
			UMTContext context=new UMTContext(request);
			int uid=context.getCurrentUMTUser().getId();
			accountService.removeCommonGEO(id,uid);
			result=true;
		}

		response.setContentType("text/html");
		response.getWriter().print(result);
	}
	/**
	 * 显示日志
	 * */
	@RequestMapping(params="act=showLog")
	public String showLog(HttpServletRequest request, HttpServletResponse response){
		int uid=SessionUtils.getUserId(request);
		List<UMTLog> logs=accountService.getTopTenLogByEventType(uid, UMTLog.EVENT_TYPE_LOG_IN);
		if(!CommonUtils.isNull(logs)){
			for(UMTLog log:logs){
				if(log.isSendWarnEmail()){
					request.setAttribute("warnLog", log);
				}
			}
		}
		request.setAttribute("commonGEOs", accountService.getMyPreCommonUseGeos(uid));
		request.setAttribute("loginMessage", logs);
		request.setAttribute("changeSecurityEmailMessage", accountService.getTopTenLogByEventType(uid, UMTLog.EVENT_TYPE_CHANGE_SECURITY_EMAIL));
		request.setAttribute("changePasswordMessage", accountService.getTopTenLogByEventType(uid, UMTLog.EVENT_TYPE_CHANGE_PASSWORD));
		return "/user/accountSafty_log";
	}
	
	//更改邮件提醒
	@RequestMapping(params="act=switchGEOEmail")
	public String switchGEOEmail(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String value=request.getParameter("value");
		boolean sendGEOEmailSwitch=Boolean.parseBoolean(value);
		UMTContext context=new UMTContext(request);
		LoginInfo info=context.getLoginInfo();
		info.getUser().setSendGEOEmailSwitch(sendGEOEmailSwitch);
		UMTContext.saveUser(request.getSession(), info);
		userService.switchGEOInfo(info.getUser());
		return "redirect:/user/safe.do?act=showLog";
	}

}
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
package cn.vlabs.umt.ui.admin;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.duckling.cloudy.common.DateUtils;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.PageBean;
import cn.vlabs.umt.common.xls.RecordFile;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IAccountService;
import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.UnitDomain;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.service.IUnitDomainService;
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.services.user.utils.ServiceFactory;

@Controller
@RequestMapping("/admin/manageUnit")
public class ManageUnitDomainController {
	
	private static interface Pagable{
		int getCount();
		Collection<UnitDomain> getRecords(int start, int count);
	}
	
	private static class AllUnits implements Pagable{
		private IUnitDomainService us;
		public AllUnits(IUnitDomainService us){
			this.us=us;
		}

		public int getCount() {
			return us.getUnitsCount();
		}
		
		public Collection<UnitDomain> getRecords(int start, int count) {
			return us.getUnits(start, count);
		}
	}

	private static class QueryUnits implements Pagable{
		private String query;
		private IUnitDomainService us;

		public QueryUnits(IUnitDomainService us, String query){
			this.us=us;
			this.query=query;
		}
		public int getCount() {
			return us.searchCount(query);
		}
		public Collection<UnitDomain> getRecords(int start, int count) {
			return us.search(query, start, count);
		}
	}
	private static Logger LOG=Logger.getLogger(ManageUserController.class);
	@Autowired
	private IAccountService accountService;
	
	@Autowired
	private IUserLoginNameService loginNameService;
	
	@Autowired
	private RoleService roleService;
	@Autowired
	private UserService userService ;
	@Autowired
	private IUnitDomainService unitDomainService ;
	
	
	@RequestMapping
	public String showUnits(HttpServletRequest request, HttpServletResponse response) {
		String query = request.getParameter("query");
		Pagable pagable = createPagable(query);
		String totalString = request.getParameter("total");
		String pageString =request.getParameter("page");
			int total=0;
			int currentPage=0;
			if (totalString != null) {
				try {
					total = Integer.parseInt(totalString);
					currentPage=Integer.parseInt(pageString);
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
		return "/admin/manageUnit";
	}
	
	
	
	@RequestMapping(params="act=checkName")
	public void checkName(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("json");
		PrintWriter writer = response.getWriter();
		boolean isUsed=unitDomainService.isNameUsed(request.getParameter("name"));
		writer.println(!isUsed);
		writer.flush();
		writer.close();
	}
	@RequestMapping(params="act=checkRootDomain")
	public void checkRootDomain(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("json");
		PrintWriter writer = response.getWriter();
		boolean isUsed=unitDomainService.isRootDomainUsed(request.getParameter("rootDomain"));
		writer.println(!isUsed);
		writer.flush();
		writer.close();
	}
	
	
	@RequestMapping(params="act=createUnit")
	public String createUser( HttpServletRequest request, HttpServletResponse response) {
		String name=request.getParameter("name");
		String enName=request.getParameter("enName");
		String symbol=request.getParameter("symbol");
		String rootDomain=request.getParameter("rootDomain");
		String mailDomain=request.getParameter("mailDomain");
		UnitDomain unitDomain = new UnitDomain();
		unitDomain.setName(name);
		unitDomain.setEnName(enName);
		unitDomain.setSymbol(symbol);
		unitDomain.setRootDomain(rootDomain);
		unitDomain.setMailDomain(mailDomain);
		int id=unitDomainService.add(unitDomain);
		if(id<=0){
			LOG.equals("create new unit domain error!");
		}
		
		return showUnits(request, response);
	}
	
	@RequestMapping(params="act=loadUnit")
	public void loadUnit(HttpServletRequest request, HttpServletResponse response) throws IOException {
		JSONObject result=new JSONObject();
		int id=Integer.parseInt(StringUtils.defaultIfEmpty(request.getParameter("id"), "-1"));
		if(id<1){
			result.put("result", false);
			writeJSONObject(response,result);
			return;
		}
		
		
		UnitDomain unitDomain=unitDomainService.findById(id);
		if(unitDomain==null||unitDomain.getId()<1){
			result.put("result", false);
			writeJSONObject(response,result);
			return;
		}
		
		result.put("result", true);
		JSONObject data=new JSONObject();
		data.put("id", unitDomain.getId());
		data.put("name", unitDomain.getName());
		data.put("enName", unitDomain.getEnName());
		data.put("rootDomain", unitDomain.getRootDomain());
		data.put("mailDomain", unitDomain.getMailDomain());
		data.put("symbol", unitDomain.getSymbol());
		result.put("data", data);
		writeJSONObject(response,result);
	}
	
	@RequestMapping(params="act=modify")
	public String modify(HttpServletRequest request, HttpServletResponse response) {
		int id=Integer.parseInt(StringUtils.defaultIfEmpty(request.getParameter("id"), "-1"));
		if(id<1){
			LOG.error("modify unit domain error! id<1");
			return showUnits(request, response);
		}
		UnitDomain unitDomain=unitDomainService.findById(id);
		if(unitDomain==null||unitDomain.getId()<1){
			LOG.error("modify unit domain error! id:"+id+" not found!");
			return showUnits(request, response);
		}
		String name=request.getParameter("name");
		String enName =request.getParameter("enName");
		String rootDomain=request.getParameter("rootDomain");
		String mailDomain=request.getParameter("mailDomain");
		String symbol=request.getParameter("symbol");
		
		unitDomain.setEnName(enName);
		unitDomain.setMailDomain(mailDomain);
		unitDomain.setName(name);
		unitDomain.setRootDomain(rootDomain);
		unitDomain.setSymbol(symbol);
		
		unitDomainService.update(unitDomain);
		
		return showUnits(request, response);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@RequestMapping(params="act=changePage")
	public String changePage(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        Integer total = (Integer)session.getAttribute("batch.total");
        RecordFile records = (RecordFile)session.getAttribute("batch.records");
        if (total==null || records==null){
        	return null;
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
            return "/admin/displayuser";
        } catch (IOException e) {
            request.setAttribute("message", e.getMessage());
            return "/admin/displayuser";
        }
    }
	private Pagable createPagable(String query){
		if (query!=null && query.trim().length()>0){
			return new QueryUnits(unitDomainService, query);
		}else{
			return new AllUnits(unitDomainService);
		}
	}
	

	@RequestMapping(params="act=loadDetailUserInfo")
	public void loadDetailUserInfo(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String uidStr=request.getParameter("uid");
		if(CommonUtils.isNull(uidStr)){
			return ;
		}
		int uid=Integer.parseInt(uidStr);
		User u=userService.getUserByUid(uid);
		if(u==null){
			return ;
		}
		List<LoginNameInfo> primaryLoginNameInfo=loginNameService.getLoginNameInfo(uid, LoginNameInfo.LOGINNAME_TYPE_PRIMARY);
		List<LoginNameInfo> secondaryLoginNameInfo=loginNameService.getLoginNameInfo(uid, LoginNameInfo.LOGINNAME_TYPE_SECONDARY);
		List<UMTLog> logs=accountService.getTopTenLogByEventType(uid, UMTLog.EVENT_TYPE_LOG_IN);
		JSONObject userObj=new JSONObject();
		userObj.put("cstnetId", u.getCstnetId());
		userObj.put("trueName",u.getTrueName());
		userObj.put("umtId", u.getUmtId());
		userObj.put("createTime",DateUtils.getDateStr(u.getCreateTime()));
		userObj.put("securityEmail",u.getSecurityEmail());
		if(!CommonUtils.isNull(logs)){
			JSONArray sLoginNameJson=new JSONArray();
			for(UMTLog log:logs){
				JSONObject pJson=new JSONObject();
				pJson.put("appName", log.getAppName());
				pJson.put("occurTime", DateUtils.getDateStr(log.getOccurTime()));
				pJson.put("ip", log.getUserIp());
				pJson.put("country", log.getCountry());
				pJson.put("city",log.getCity());
				sLoginNameJson.add(pJson);
			}
			userObj.put("logs", sLoginNameJson);
		}
		if(!CommonUtils.isNull(primaryLoginNameInfo)){
			LoginNameInfo pLoginName=CommonUtils.first(primaryLoginNameInfo);
			JSONObject pJson=new JSONObject();
			pJson.put("loginName", pLoginName.getLoginName());
			pJson.put("status", pLoginName.getStatusDisplay());
			userObj.put("primaryLoginName", pJson);
		}
		if(!CommonUtils.isNull(secondaryLoginNameInfo)){
			JSONArray sLoginNameJson=new JSONArray();
			for(LoginNameInfo loginName:secondaryLoginNameInfo){
				JSONObject pJson=new JSONObject();
				pJson.put("loginName", loginName.getLoginName());
				pJson.put("status", loginName.getStatusDisplay());
				sLoginNameJson.add(pJson);
			}
			userObj.put("secondaryLoginName", sLoginNameJson);
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		PrintWriter writer = response.getWriter();
		writer.write(userObj.toString());
		writer.flush();
		writer.close();
	}
	
	@RequestMapping(params="act=loadUser")
	public void loadUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String username=request.getParameter("q");
		User u = userService.getUserByUid(Integer.valueOf(username));
		boolean isAdmin=ServiceFactory.getRoleService(request).isMemberOf("admin", u.getId());
		
		//转锟斤拷锟斤拷JSON
		StringBuffer buffer = new StringBuffer();
		buffer.append("[{");
		buffer.append("username:'"+u.getCstnetId()+"'");
		buffer.append(",truename:'"+u.getTrueName()+"'");
		buffer.append(",email:'"+u.getCstnetId()+"'");
		buffer.append(",isAdmin:"+isAdmin+"");
		buffer.append("}]");
		
		//锟斤拷锟�
		response.setContentType("text/javascript");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		writer.write(buffer.toString());
		writer.flush();
		writer.close();
	}
	
	
	@RequestMapping(params="act=removeUnit",method=RequestMethod.POST)
	public String removeUser(HttpServletRequest request, HttpServletResponse response) {
		String[] idStrs=StringUtils.defaultIfEmpty(request.getParameter("removeIds"), "").split(",");
		unitDomainService.delete(CommonUtils.stringArray2IntArray(idStrs));
		return showUnits(request, response);
	}
	@RequestMapping(params="act=resetPassword")
	public String resetPassword(HttpServletRequest request, HttpServletResponse response) {
		User user=userService.getUserByLoginName(request.getParameter("username"));
		if(user==null||user.getType()==null){
			throw new RuntimeException("user not found or auth by is unkown");
		}else if(User.USER_TYPE_UMT.equals(user.getType())||User.USER_TYPE_MAIL_AND_UMT.equals(user.getType())){
			userService.updatePassword(user.getId(), request.getParameter("password"));
		}
		return showUnits(request, response);
	}
	@RequestMapping(params="act=search")
	public void search(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String query = request.getParameter("q");
		if (query!=null){
			Collection<User> users = userService.search(query, 0, 10);
			
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
			
			response.setCharacterEncoding("UTF-8");
			response.setContentType("text/json");
			PrintWriter writer = response.getWriter();
			writer.print(json.toString());
			writer.flush();
			writer.close();
		}
	}
	
	
	@RequestMapping(params="act=updateAccountStatus")
	public void updateAccountStatus(HttpServletRequest request, HttpServletResponse response){
		String[] uids=request.getParameterValues("userIds[]");
		if(CommonUtils.isNull(uids)){
			return;
		}
		String toStatus=request.getParameter("toStatus");
		int[] uidsInt=new int[uids.length];
		int index=0;
		for(String uid:uids){
			uidsInt[index++]=Integer.parseInt(uid);
		}
		userService.updateValueByColumn(uidsInt, "account_status", toStatus);
	}
	/**
	 * 验证用户是否存在，逻辑是先去数据库差，如果数据库米有，就去邮件系统查
	 * */
	@RequestMapping(params="act=usercheck")
	public void usercheck(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		response.setContentType("json");
		PrintWriter writer = response.getWriter();
		int rtnCode=userService.isUsed(request.getParameter("username"));
		if(!CommonUtils.isNull(request.getParameter("returnCode"))){
			writer.print(rtnCode!=UserService.USER_NAME_UNUSED?UserService.USER_NAME_USED:UserService.USER_NAME_UNUSED);
		}else{
			writer.println(rtnCode==UserService.USER_NAME_UNUSED);
		}
		writer.flush();
		writer.close();
	}
	
	private static void writeJSONObject(HttpServletResponse response,Object object) {
		PrintWriter writer = null;
		try {
			//为了兼容IE系浏览器，特意设置成text/html格式
			response.setContentType("text/html");
			response.setCharacterEncoding("UTF-8");
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
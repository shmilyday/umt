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
/**
 * 
 */
package cn.vlabs.umt.services.account;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;

import org.apache.log4j.Logger;

import tebie.applib.api.APIContext;
import tebie.applib.api.IClient;
import cn.vlabs.duckling.api.umt.rmi.userv7.SearchField;
import cn.vlabs.umt.common.datasource.CoreMailDBException;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.user.bean.CoreMailUserInfo;
import cn.vlabs.umt.services.user.bean.User;

/**
 * 用户远程调用API的Client
 * 
 * @author lvly
 * @since 2013-1-10
 */
public class CoreMailUseableClient extends ICoreMailClient{
	/**
	 * API信息，得去CoreMail那里用管理员账号，开通，并配置到umt.properties
	 * */
	private Config umtConfig;
	
	private ICoreMailDBDAO coremailDAO;

	
	
	/**
	 * 获得配置文件，umt.properties
	 * */
	public Config getUmtConfig(){
		return this.umtConfig;
	}
	/**
	 * 构造方法
	 * @param config 配置文件实体类
	 * */
	public CoreMailUseableClient(Config config,ICoreMailDBDAO coremailDAO) {
		this.umtConfig = config;
		this.coremailDAO=coremailDAO;
	}
	public static final String BEAN_ID="coreMailClient";
	private static final Logger LOG = Logger.getLogger(CoreMailUseableClient.class);
	private static final String KEY_API_IP = "umt.coremail.api.ip";
	private static final String KEY_API_PORT = "umt.coremail.api.port";

	private static final String KEY_DEFAULT_API_IP = "127.0.0.1";
	private static final int KEY_DEFAULT_API_PORT = 8080;
	
	private static final String KEY_PROVIDER_ID="umt.coremail.api.providerId";
	private static final String KEY_ORG_ID="umt.coremail.api.orgId";
	private static final String KEY_EMAIL_DOMAIN="umt.coremail.api.email.domain";
	private static final String KEY_API_USER_STATUS="umt.coremail.api.user.status";
	private static final String KEY_API_USER_COS_ID="umt.coremail.api.user.cosId";
	private static final String KEY_API_USER_QUO_DELTA="umt.coremail.api.user.quotaDelta";
	

	/**
	 * 获得于CoreMail的通讯客户端
	 * */
	private IClient getCoreMailClient() throws IOException {
		return getCoreMailClient(1000);
		
	}
	private IClient getCoreMailClient(int timeout)throws IOException {
		Socket socket = new Socket(umtConfig.getStringProp(KEY_API_IP, KEY_DEFAULT_API_IP), umtConfig.getInt(
				KEY_API_PORT, KEY_DEFAULT_API_PORT));
		socket.setSoTimeout(timeout);
		return APIContext.getClient(socket);
	}
	

	/**
	 * 邮件系统验证用户是否存在
	 * @param usreName 用户邮箱
	 * */
	public boolean isUserExt(String userName) {
		IClient client = null;
		APIContext context = null;
		if(isBlack(formatEmail(userName))){
			return false;
		}
		try {
			client = getCoreMailClient();
			context=client.userExist(formatEmail(userName));
			switch (context.getRetCode()) {
				case APIContext.RC_NORMAL:
					return true;
				default:
					return false;
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(),e);
			return false;
		}finally{
			closeClient(client);
		}
	}

	/**
	 * 更改用户的对应密码，注意，请先用authenticate验证，就密码是否相同
	 * 
	 * @param userName
	 *            用户账户
	 * @param newPassword
	 *            ，明文密码
	 * @return 是否更改成功
	 * */
	public boolean changePassword(String userName, String newPassword) {
		IClient client = null;
		APIContext context = null;
		try {
			client = getCoreMailClient();
			context = client.changeAttrs(userName, "password=" + URLEncoder.encode(newPassword,"UTF-8"));
			switch (context.getRetCode()) {
			case APIContext.RC_USER_NOT_FOUND:
				LOG.info("Not Found User :" + userName);
				return false;
			case APIContext.RC_NORMAL:
				LOG.info("passWordChange Success:" + userName);
				return true;
			default:
				return false;
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(),e);
			return false;
		} finally {
			closeClient(client);
		}
	}

	/**
	 * 每次操作务必调用此方法，关闭连接
	 * 
	 * @param client
	 *            API要求每个线程都有独立的client，并且执行完成以后，释放掉
	 * */
	private void closeClient(IClient client) {
		if (client != null) {
			try {
				client.close();
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 验证邮箱用户名密码，是否正确
	 * 
	 * @param userName
	 *            用户名
	 * @param passWord
	 *            用户密码
	 * */
	public CoreMailAuthenticateResult authenticate(String userName, String passWord) {
		IClient client = null;
		APIContext context = null;
		if(isBlack(userName)){
			return new CoreMailAuthenticateResult(false, false);
		}
		if(umtConfig.getBooleanProp("use.coremail.db", false)){
			try {
				long start=System.currentTimeMillis();
				CoreMailUserInfo userInfo=coremailDAO.authticate(formatEmail(userName), passWord);
				/*if(userInfo==null){
					LOG.info("db:password Error:"+userName);
					return new CoreMailAuthenticateResult(false, false);
				}*/
				if(userInfo!=null){
					return validateCanAccessFromUserInfo(start,userInfo,true);
				}
			} catch (CoreMailDBException e) {
				if(CoreMailDBException.WHY_DB_ERROR.equals(e.getWhy())){
					LOG.error(e.getWhy(),e);
				}else if(CoreMailDBException.WHY_PWD_ERROR.equals(e.getWhy())){
					LOG.info("user "+userName+" encType strange!!!");
				}
			}
		}
		try {
			client = getCoreMailClient();
			context = client.authenticate(formatEmail(userName), passWord);
			switch (context.getRetCode()) {
				case APIContext.RC_USER_NOT_FOUND:
					LOG.info("Not Found User :" + userName);
					return new CoreMailAuthenticateResult(false, false);
					
				case APIContext.RC_PASSWORD_ERR:
					LOG.info("passWordError in User:" + userName);
					return new CoreMailAuthenticateResult(false, true);
				case APIContext.RC_NORMAL:{
					long start=System.currentTimeMillis();
					CoreMailUserInfo coreMailUserInfo=getCoreMailUserInfo(formatEmail(userName));
					return validateCanAccessFromUserInfo(start,coreMailUserInfo,false);
					
				}default:{
					LOG.info("unkown code :" + context.getRetCode());
					return new CoreMailAuthenticateResult(false, false);
				}
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(),e);
			return new CoreMailAuthenticateResult(false, false);
		} finally {
			closeClient(client);
		}
	}
	private CoreMailAuthenticateResult validateCanAccessFromUserInfo(long start,CoreMailUserInfo userInfo,boolean db){
		if(userInfo==null){
			LOG.info("why userinfo is null?");
			return new CoreMailAuthenticateResult(false, false);
		}
		if(!CoreMailUserInfo.STATUS_NORMAL.equals(userInfo.getStatus())){
			LOG.info("user's info is null! cause user's status is unnormal");
			return new CoreMailAuthenticateResult(true, true,userInfo);
		}else if(userInfo.isExpired()){
			LOG.info("user is expired! ");
			return new CoreMailAuthenticateResult(true, true,userInfo);
		}
		LOG.info("validate["+userInfo.getEmail()+"] password use "+(db?"db":"api")+" "+(System.currentTimeMillis()-start)+"ms");
		return new CoreMailAuthenticateResult(true, true,userInfo);
	}

	/**
	 * 用户登录
	 * 
	 * @param userName
	 *            用户名
	 * @param passWord
	 *            密码
	 * @return 返回操作是否成功
	 * */
	public boolean login(String userName, String passWord) {
		if (authenticate(userName, passWord).isSuccess()) {
			IClient client = null;
			APIContext context = null;
			if(isBlack(userName)){
				return false;
			}
			try {
				client = getCoreMailClient();
				context = client.userLoginEx(userName, "type=API");
				switch (context.getRetCode()) {
				case APIContext.RC_NORMAL: {
					return true;
				}
				case APIContext.RC_USER_NOT_FOUND: {
					LOG.error("userNotFound");
					break;
				}
				case APIContext.RC_SES_ERROR: {
					LOG.error("SessionError");
					break;
				}
				default: {
					LOG.error("unkown error,error code:" + context.getRetCode());
					break;
				}
				}

			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}finally{
				closeClient(client);
			}
		}
		return false;
	}

	/**
	 * 从URL规则的字符串里面提取值,如
	 * getParameter("&userName=zs&password=123","userName"),即会返回zs
	 * 
	 * @param encoded
	 *            已经encode过的字符串
	 * @param key
	 *            需要提取的key
	 * @return value
	 * */
	private String getParameter(String encoded, String key) throws UnsupportedEncodingException {
		int start;
		if (encoded.startsWith(key + '=')) {
			start = key.length() + 1;
		} else {
			int i = encoded.indexOf('&' + key + '=');
			if (i == -1) {
				return null;
			}
			start = i + key.length() + 2;
		}
		int end = encoded.indexOf('&', start);
		String value = (end == -1) ? encoded.substring(start) : encoded.substring(start, end);
		return URLDecoder.decode(value, "UTF-8");
	}
	
	/**
	 * 获得用户信息,请调用这个方法前，用authenticate(userName,passWord)验证改用户是否有效
	 * 
	 * @param userName
	 * @return 用户信息
	 * */
	public CoreMailUserInfo getCoreMailUserInfo(String userName) { 
		IClient client = null;
		APIContext context = null;
		if(isBlack(userName)){
			return null;
		}
		try {
			client = getCoreMailClient();
			context = client.getAttrsEx(userName, "true_name=&user_id=&user_status=&user_expiry_date=");
			if (APIContext.RC_NORMAL == context.getRetCode()) {
				String result = context.getResult();
				String expireTime=getParameter(result, "user_expiry_date");
				String trueName = getParameter(result, "true_name");
				String status=getParameter(result, "user_status");
				CoreMailUserInfo userInfo=new CoreMailUserInfo();
				userInfo.setEmail(userName.toLowerCase());
				userInfo.setTrueName(CommonUtils.isNull(trueName)?userName:trueName);
				userInfo.setExpireTime(expireTime);
				userInfo.setStatus(status);
				return userInfo;
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(),e);
		}finally{
			closeClient(client);
		}
		return null;
	}
	private Attributes abstractSearch(String orgIds,String[] userAttributes, Attributes[] filters,String[] orders,int offset,int size){
		try {
			APIContext  obj = getCoreMailClient().listUsersJNDI(orgIds, null, true, true, userAttributes, filters, orders, offset, size);
			return (Attributes)(obj.getResultEx());
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		} catch (NamingException e) {
			LOG.error(e.getMessage(),e);
		}
		return null;
	}
	private Attributes getFilter(SearchField field,String keyword){
		Attributes filter = new BasicAttributes();
		filter.put("op", 6); // LIKE
		filter.put("val", "%"+keyword+"%");
		switch(field){
			case TRUE_NAME:{
				filter.put("name","true_name");
				break;
			}
			case DOMAIN:{
				filter.put("name","domain_name");
				break;
			}
			case CSTNET_ID:{
				filter.put("name","user_id");
				break;
			}
			default:throw new RuntimeException("the CoreMail Field Unkown");
		}
		return filter;
	}
	private String getOrderBy(SearchField field){
		switch(field){
		case TRUE_NAME:{
			return "true_name";
		}
		case DOMAIN:{
			return "domain_name";
		}
		case CSTNET_ID:{
			return "user_id";
		}
		default:throw new RuntimeException("the CoreMail Field can't be ALL");
	}
		
	}
	/**
	 * 获取次关键字有多少结果
	 * @param keyword
	 * */
	public int getSize(String domain,String keyword,SearchField field){
		Attributes filter=getFilter(field,keyword);
		int total=0;
		for(String orgId:getOrgId(domain)){
			try {
			
			Attributes result=abstractSearch(orgId,new String[]{}, new Attributes[]{filter}, null,0,-1);
			total+=(int)result.get("total").get();
			} catch (NamingException e) {
				LOG.error(e.getMessage(),e);
			}
		}
		
		return total;
	}
	private String[] getOrgId(String domain) {
		APIContext context;
		try {
		    if("all".equals(domain)){
		    	return new String[]{null};
		    }
			else if(!CommonUtils.isNull(domain)&&domainExist(domain)){
				context = getCoreMailClient().getOrgListByDomain(domain);
				return context.getResult().split(",");
			}
			return new String[]{};
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
			return new String[]{};
		}
	}
	/**
	 * 根据关键字搜索
	 * @param keyword 关键字
	 * @param field 搜索字段	
	 * @param offset 偏移
	 * @param size	-1为无限制
	 * @return
	 */
	public List<User> searchByKeyword(String keyword,String domain,SearchField field,int offset,int size){
		  String[] userAttributes = {"user_id","domain_name", "true_name"};
		  // 定义一个域名查询条件查询条件
		  Attributes filter =getFilter(field,keyword);
		  List<User> list=new ArrayList<User>();
		  for(String orgId:getOrgId(domain)){
			  Attributes result=abstractSearch(orgId,userAttributes, new Attributes[]{filter}, new String[]{getOrderBy(field)},offset,size);
			  if(result!=null){
				  Attribute u=result.get("u");
				  addUserToList(u, list);
			  }
		  }
		  return list;
	}
	public void addUserToList(Attribute u,List<User> list){
		for(int i=0;i<u.size();i++){
			 try {
				Attributes userInfo=(Attributes)u.get(i);
				User user=new User();
				user.setCstnetId(userInfo.get("user_id").get()+"@"+userInfo.get("domain_name").get());
				Object trueName=userInfo.get("true_name").get();
				user.getTrueName();
				user.setTrueName(trueName==null?null:trueName.toString());
				user.setType(User.USER_TYPE_CORE_MAIL);
				list.add(user);
			 } catch (NamingException e) {
				LOG.error(e.getMessage(),e);
			}
		  }
	}
	
	/**
	 * 检查CoreMail域名是否已存在
	 * */
	public boolean domainExist(String domainName){
		if(isBlack("example@"+domainName)){
			return false;
		}
		try {
			String domain=CommonUtils.trim(domainName);
			if(umtConfig.getBooleanProp("use.coremail.db", false)){
				try{
					return coremailDAO.isDomainExits(domain);
				}catch(CoreMailDBException e){
					LOG.error(e.getMessage(),e);
				}
			}
			APIContext context=getCoreMailClient().domainExist(domain);
			return !CommonUtils.isNull(context.getResult())&&context.getRetCode()==APIContext.RC_NORMAL;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * 创建用户
	 * @param username
	 * @param password
	 */
	public boolean createUser(String username,String trueName, String password) {
		IClient client = null;
		APIContext context = null;
		try {
			client = getCoreMailClient(5000);
			String param="password="+password+
					"&domain_name="+umtConfig.getStringProp(KEY_EMAIL_DOMAIN, "")+
					"&user_status="+umtConfig.getStringProp(KEY_API_USER_STATUS, "")+
					"&cos_id="+umtConfig.getStringProp(KEY_API_USER_COS_ID, "")+
					"&quota_delta="+umtConfig.getStringProp(KEY_API_USER_QUO_DELTA, "")+
					"&true_name="+trueName;
			context = client.createUser(
					umtConfig.getStringProp(KEY_PROVIDER_ID, ""),
					umtConfig.getStringProp(KEY_ORG_ID, ""), 
					getUserName(username), param
					);
			if(context.getRetCode()==APIContext.RC_NORMAL){
				return true;
			}else{
				LOG.error(context.getErrorInfo()+"["+param+"]");
				return false;
			}
		} catch (IOException e) {
			LOG.error(e.getMessage());
			LOG.debug(e.getMessage(),e);
			return false;
		}finally{
			closeClient(client);
		}
	}
	public void print(APIContext context,String oper){
		System.out.println("The Result of Operation["+oper+"]:");
		System.out.println("{");
		System.out.println("\tError Info:"+context.getErrorInfo());
		System.out.println("\tResult:"+context.getResult());
		System.out.println("\tReturn Code:"+context.getRetCode());
		System.out.println("\tResult Ex:"+context.getResultEx());
		System.out.println("}\n\n");
	}
}

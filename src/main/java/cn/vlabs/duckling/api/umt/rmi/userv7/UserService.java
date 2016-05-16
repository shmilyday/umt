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
package cn.vlabs.duckling.api.umt.rmi.userv7;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.duckling.api.umt.rmi.exception.APIRuntimeException;
import cn.vlabs.duckling.api.umt.rmi.exception.AccessForbidden;
import cn.vlabs.duckling.api.umt.rmi.exception.ErrorCode;
import cn.vlabs.duckling.api.umt.rmi.exception.LoginFailed;
import cn.vlabs.duckling.api.umt.rmi.exception.UserExistException;
import cn.vlabs.duckling.api.umt.rmi.exception.UserNotFoundException;
import cn.vlabs.rest.ServiceClient;
import cn.vlabs.rest.ServiceContext;
import cn.vlabs.rest.ServiceException;

/**
 * 用户操作接口<br>
 * 这里的每个操作都有可能抛出LoginFailedException, AccessForbiddenException<br>
 * 
 * @author xiejj@cnic.cn
 * @creation Dec 22, 2009 9:44:04 AM
 * <br>
 * 常用方式：<br>
 * //((Config)factory.getBean("config")).getProperty("umt.api.service.url")为配置文件中配置的UMT的URL服务路径信息,<br>
 * 可以用类似http://localhost:8080/ServiceServlet的路径来替换<br>
 * UserService umtUserService = new UserService(((Config)factory.getBean("config")).getProperty("umt.api.service.url")); <br>
 * UMTUser umtUser = new UMTUser(username, name, username, password);<br>
 * umtUserService.createUser(umtUser);<br>
 * 
 */
/**
 * @author lvly
 * @since 2013-4-24
 */
/**
 * @author lvly
 * @since 2013-4-24
 */
public class UserService {
	/**
	 * 以UMT服务的URL连接用户服务
	 * 例如：String umtServiceURL = "http://localhost:8080/ServiceServlet"; 需要根据情况替换主机(localhost)和端口号(8080)
	 * @param baseurl 
	 * @throws LoginFailed
	 */
	public UserService(String baseurl) throws LoginFailed {
		context = new ServiceContext(baseurl);
		context.setKeepAlive(true);
		client = new ServiceClient(context);
	}

	/**
	 * 创建用户
	 * 
	 * @param user
	 *            用户信息
	 * @throws UserExistException
	 *             如果要创建的用户已存在，抛出该异常
	 */
	public void createUser(UMTUser user) throws UserExistException{
		try {
			sendService("UserV7.createUser", user);
		} catch (ServiceException e) {
			if (e.getCode()==ErrorCode.USER_EXIST){
				throw new UserExistException(user.getUmtId());
			}else{
				throw new APIRuntimeException(e);
			}
		}
	}
	
	/**
	 * 批量创建用户，会返回生成的umtId之类的东东，
	 * @param user
	 * */
	@SuppressWarnings("unchecked")
	public List<UMTUser> createUsers(List<UMTUser> user) {
		try {
			return (List<UMTUser>)sendService("UserV7.createUsers", user);
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	

	/**
	 * 判断用户是否已存在
	 * 
	 * @param users
	 *            被检查的用户列表
	 * @return 如果用户存在，则在该集合中返回
	 */
	public boolean[] isExist(String[] users){
		if (users==null||users.length==0){
			return new boolean[0];
		}
		
		ArrayList<String> usernames = new ArrayList<String>();
		Collections.addAll(usernames, users);
		
		try {
			return (boolean[])sendService("UserV7.isExist", usernames);
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	public boolean isExist(String username){
		if (username==null||username.length()==0){
			return false;
		}
		ArrayList<String> usernames = new ArrayList<String>();
		usernames.add(username);
		try {
			boolean[] results= (boolean[])sendService("UserV7.isExist", usernames);
			return results[0];
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	/**
	 * 获取UMT用户对象，其password属性为空
	 * 
	 * @param umtId
	 *        指定的用户名，老用户是邮件格式，新用户是数字
	 * @return
	 *        不存在返回所有属性为空的对象
	 */
	public UMTUser getUMTUser(String umtId)
	{
		try {
			return (UMTUser)sendService("UserV7.getUser", umtId);
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<UMTUser> getUMTUsers(List<String> umtIds){
		try {
			return (List<UMTUser>)sendService("UserV7.getUmtUsers", umtIds);
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	/**
	 * 获取UMT用户对枪，其password为空
	 * */
	public UMTUser getUMTUserByLoginName(String loginName){
		try {
			return (UMTUser)sendService("UserV7.getUserByLoginName", loginName);
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	
	/** 根据搜索关键字查询出用户
	 * @param keyWord 关键字
	 * @param domain 搜索域名
	 * @param field	搜索字段
	 * @param scope	搜索范围
	 * @param offset 偏移值
	 * @param size 数量
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List<UMTUser> searchUserByKeyword(String keyWord,String domain,SearchScope scope,SearchField field,int offset,int size){
		try {
			return (List<UMTUser>)sendService("UserV7.searchByKeywordAndDomain", new Object[]{keyWord,domain,scope,field,offset,size});
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	@SuppressWarnings("unchecked")
	public List<UMTUser> searchUserByKeyword(String keyWord,SearchScope scope,SearchField field,int offset,int size){
		try {
			return (List<UMTUser>)sendService("UserV7.searchByKeyword", new Object[]{keyWord,scope,field,offset,size});
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	
	
	/**
	 * 
	 * @param name  查找包含此字符串的用户名，只搜索Umt库，老版本，不建议使用
	 * @param Count  返回个数，0 表示全部返回
	 * @return
	 */
	@Deprecated
	public List<UMTUser> searchUserLikeName(String name,int count){
		return search(name, 0, count, UMTUser.FIELD_CSTNET_ID, true);
	}

	/**
	 * 检索用户,只搜索Umt库，老版本，不建议使用
	 * @param keyword 检索的关键字，被检索域包括 username, truename, email
	 * @param offset 返回的记录集起始位置
	 * @param count  返回的记录集的数量
	 * @param orderBy 记录的排序(可以是 UMTUser.FIELD_USERNAME,UMTUser.FIELD_TRUENAME, UMTUser.FIELD_EMAIL)
	 * @param isAscendent true为升序，false为降序
	 * @return 返回符合条件的记录集
	 */
	@SuppressWarnings("unchecked")
	@Deprecated
	public List<UMTUser> search(String keyword, int offset, int count, String orderBy, boolean isAscendent){
		try {
			return (List<UMTUser>) sendService("UserV7.search", new Object[]{keyword, offset, count, orderBy, isAscendent});
		}  catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	/**
	 * 查询匹配检索条件的用户数
	 * @param keyword
	 * @return
	 */
	public int getUserCount(String keyword){
		try {
			return (Integer) sendService("UserV7.searchCount", keyword);
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	
	/**
	 * 更新用户信息，只有修改真实姓名的权限
	 * <br>其他功能，请引导用户到passport做
	 * 
	 * @param user
	 *            用户信息
	 * @throws UserNotFoundException 用户不存在时抛出该异常
	 */
	public void updateUser(UMTUser user) throws UserNotFoundException {
		try {
			sendService("UserV7.updateUser", user);
		} catch (ServiceException e) {
			if (e.getCode()==ErrorCode.USER_NOT_FOUND){
				throw new UserNotFoundException();
			}
			throw new APIRuntimeException(e);
		}
	}
	/**
	 * Login
	 * @param userName
	 * @param password
	 * @return
	 */
	public UserPrincipal login(String userName, String password)  {
		try {
			return (UserPrincipal)sendService("UserV7.login", new Object[]{userName, password});
		} catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	
	}
	/**
	 * 查看域名是不是存在
	 * */
	public boolean domainExist(String domainName){
		try{
			return (boolean)sendService("UserV7.domainExist",domainName);
		}catch (ServiceException e) {
			throw new APIRuntimeException(e);
		}
	}
	
	private Object sendService(String service, Object value) throws ServiceException{
		try {
			return client.sendService(service, value);
		} catch (ServiceException e) {
			switch (e.getCode()){
			case ErrorCode.ACCESS_FORBIDDEN:
				throw new AccessForbidden();
			default:
				throw e;
			}
		}finally{
			context.close();
		}
	}
	/**
	 * 生成指定用户的umtId,并返回
	 * */
	public String[] generateUmtId(String[] cstnetId)throws ServiceException{
		List<String> usernames = new ArrayList<String>();
		Collections.addAll(usernames, cstnetId);
		return (String[])sendService("UserV7.generateUmtId", usernames);
	}

	private ServiceContext context;

	private ServiceClient client;
}
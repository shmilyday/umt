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
package cn.vlabs.umt.ui.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.duckling.api.umt.rmi.exception.ErrorCode;
import cn.vlabs.duckling.api.umt.rmi.user.UMTUser;
import cn.vlabs.duckling.api.umt.rmi.userv7.SearchField;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.server.annotation.RestMethod;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UserField;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.ui.UMTContext;

public class RestUserServiceImpl {
	private static Logger LOGGER =Logger.getLogger(RestUserServiceImpl.class);
	
	private UserService service;
	private LoginService loginService;
	private ICoreMailClient coreMail;
	public RestUserServiceImpl(){
		BeanFactory factory = UMTContext.getFactory();
		service = (UserService)factory.getBean(UserService.BEAN_ID);
		loginService = (LoginService)factory.getBean(LoginService.BEAN_ID);
		coreMail=ICoreMailClient.getInstance();
	}
	@RestMethod("createUser")
	public void createUser(UMTUser user) throws ServiceException{
		if (service.isUsed(user.getUsername())){
			throw new ServiceException(ErrorCode.USER_EXIST,"用户已存在"+user.getUsername());
		}
		try {
			service.create(toUser(user),LoginNameInfo.STATUS_ACTIVE);
		} catch (InvalidUserNameException e) {
			throw new ServiceException(ServiceException.ERROR_INTERNAL_ERROR,"不合法的用户名:"+user.getUsername());
		}
	}
	@RestMethod("getUser")
	public UMTUser getUMTUser(String username){
		User umtUser = service.getUserByLoginName(username); 
		if(umtUser==null){
			umtUser=coreMail.getCoreMailUserInfo(username);
			return toUMTUser(umtUser);
		}
		return toUMTUser(umtUser);
	}
	
	@RestMethod("isExist")
	public boolean[] isExist(Collection<String> usernames) {
		if (usernames==null||usernames.size()==0){
			return new boolean[0];
		}
		String[] users = toArray(usernames);
		boolean[] results=new boolean[usernames.size()];
		Set<String> exists = service.isExist(users);
		if (exists!=null){
			int i=0;
			for (String username:usernames){
				results[i]=exists.contains(username);
				i++;
			}
		}
		return results;
	}
	
	private String[] toArray(Collection<String> usernames){
		String[] users = new String[usernames.size()];
		int i=0;
		for (String username:usernames){
			users[i]=username;
			i++;
		}
		return users;
	}
	
	@RestMethod("search")
	public List<UMTUser> search(String keyword, int offset, int count, String orderBy, boolean isAscendent){
		if (keyword==null || keyword.equals("")){
			return new ArrayList<UMTUser>();
		}
		keyword = keyword.replaceAll("%", "");
		if(count<=0)
		{
			count = Integer.MAX_VALUE;
		}
		
		if (offset<=0)
		{
			offset=0;
		}
		UserField field = UserField.cstnetId;
		if (UMTUser.FIELD_EMAIL.equals(orderBy)){
			field = UserField.cstnetId;
		}
		if (UMTUser.FIELD_TRUE_NAME.equals(orderBy)){
			field = UserField.trueName;
		}
		Collection<User>  users = new ArrayList<User>();
		users.addAll(coreMail.searchByKeyword(keyword,"all", SearchField.CSTNET_ID, offset, count));
		users.addAll(coreMail.searchByKeyword(keyword,"all", SearchField.DOMAIN, offset, count));
		users.addAll(coreMail.searchByKeyword(keyword,"all", SearchField.TRUE_NAME, offset, count));
		Collection<User> umtUsers=service.search(keyword, offset, count, field, isAscendent);
		if(umtUsers!=null){
			users.addAll(umtUsers);
		}
		ArrayList<UMTUser> rusers = new ArrayList<UMTUser>();
		Set<String> repeat=new HashSet<String>();
		if(users!=null&&!users.isEmpty()){
			for(User user:users){
				if(repeat.contains(user.getCstnetId())){
					continue;
				}
				repeat.add(user.getCstnetId());
				rusers.add(toUMTUser(user));
			}
		}
		return rusers;
	}
	@RestMethod("login")
	public UserPrincipal login(String userName, String password){
		UserPrincipal princ= loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(userName, password)).getUserPrincipal();
		if(princ==null){
			return null;
		}else{
			return princ;
		}
	}
	@RestMethod("searchCount")
	public int searchCount(String keyword){
		return service.searchCount(keyword);
	}
	@RestMethod("updateUser")
	public void updateUser(UMTUser user) throws ServiceException {
		if(!CommonUtils.isNull(user.getPassword())){
			LOGGER.error("password is not allowed changed");
		}else{
			updateUserWithoutPwd(user);
		}
		
	}

	@RestMethod("updateUserWithoutPwd")
	public synchronized void updateUserWithoutPwd(UMTUser user) throws ServiceException {
		if (service.isUsed(user.getUsername())){
			User localUser=service.getUserByLoginName(user.getUsername());
			if(localUser!=null){
				localUser.setTrueName(user.getTruename());
				service.update(localUser, false);
				return;
			}
			User coreMailUser=coreMail.getCoreMailUserInfo(user.getUsername());
			if(coreMailUser!=null){
				try {
					service.create(coreMailUser, LoginNameInfo.STATUS_ACTIVE);
				}catch (InvalidUserNameException e) {
					throw new ServiceException(ErrorCode.USER_EXIST, "要创建的用户已存在");
				}
			}else{
				throw new ServiceException(ErrorCode.USER_NOT_FOUND, "要更新的用户未找到");
			}
			coreMailUser.setTrueName(user.getTruename());
			service.update(coreMailUser, false);
		}else{
			throw new ServiceException(ErrorCode.USER_NOT_FOUND, "要更新的用户未找到");
		}
	}	
	private UMTUser toUMTUser(User user){
		if (user!=null){
			UMTUser umtUser= new UMTUser();
			umtUser.setEmail(user.getCstnetId());
			umtUser.setUsername(user.getCstnetId());
			umtUser.setTruename(user.getTrueName());
			return umtUser;
		}
		return null;
	}
	private User toUser(UMTUser user){
		User u = new User();
		u.setCstnetId(user.getUsername());
		u.setPassword(user.getPassword());
		u.setTrueName(user.getTruename());
		return u;
	}
}
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
import cn.vlabs.duckling.api.umt.rmi.userv7.SearchField;
import cn.vlabs.duckling.api.umt.rmi.userv7.SearchScope;
import cn.vlabs.duckling.api.umt.rmi.userv7.UMTUser;
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
import cn.vlabs.umt.services.user.service.IUserLoginNameService;
import cn.vlabs.umt.ui.UMTContext;

public class RestUserServiceV7Impl {
	private static Logger LOGGER =Logger.getLogger(RestUserServiceV7Impl.class);
	
	private UserService service;
	private LoginService loginService;
	private IUserLoginNameService loginNameService;
	private ICoreMailClient coreMailclient;
	public RestUserServiceV7Impl(){
		BeanFactory factory = UMTContext.getFactory();
		service = (UserService)factory.getBean(UserService.BEAN_ID);
		loginService = (LoginService)factory.getBean(LoginService.BEAN_ID);
		loginNameService=(IUserLoginNameService)factory.getBean(IUserLoginNameService.BEAN_ID);
		coreMailclient=ICoreMailClient.getInstance();
	}
	@RestMethod("createUser")
	public void createUser(UMTUser user) throws ServiceException{
		if (service.isUsed(user.getCstnetId())){
			throw new ServiceException(ErrorCode.USER_EXIST,"用户已存在");
		}
		try {
			service.create(toUser(user),LoginNameInfo.STATUS_ACTIVE);
		} catch (InvalidUserNameException e) {
			throw new ServiceException(ServiceException.ERROR_INTERNAL_ERROR,"不合法的用户名:"+user.getUmtId());
		}
	}
	@RestMethod("createUsers")
	public List<UMTUser> createUsers(List<UMTUser> users) throws ServiceException{
		int index=0;
		for(UMTUser umtUser:users){
			try {
				String orgPassword=umtUser.getPassword();
				User user=toUser(umtUser);
				service.create(user,LoginNameInfo.STATUS_ACTIVE);
				umtUser=toUMTUser(user);
				umtUser.setPassword(orgPassword);
				users.set(index++,umtUser);
			} catch (InvalidUserNameException e) {
				throw new ServiceException(ServiceException.ERROR_INTERNAL_ERROR,"不合法的用户名:"+umtUser.getUmtId());
			}
		}
		return users;
	}
	
	@RestMethod("getUser")
	public UMTUser getUMTUserByUmtId(String umtId){
		User umtUser = service.getUserByUmtId(umtId); 
		return toUMTUser(umtUser);
	}
	@RestMethod("getUmtUsers")
	public List<UMTUser> getUMTUsers(List<String> umtIds){
		List<User> umtUser = service.getUsersByUmtId(umtIds);
		List<UMTUser> result=new ArrayList<UMTUser>();
		if(umtUser==null){
			return result;
		}
		for(User user:umtUser){
			result.add(toUMTUser(user));
		}
		return result;
	}
	@RestMethod("getUserByLoginName")
	public UMTUser getUMTUserByLogiName(String loginName){
		User umtUser=service.getUserByLoginName(loginName);
		if(umtUser==null){
			return toUMTUser(coreMailclient.getCoreMailUserInfo(loginName));
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
		if (UMTUser.FIELD_CSTNET_ID.equals(orderBy)){
			field = UserField.cstnetId;
		}
		if (UMTUser.FIELD_TRUE_NAME.equals(orderBy)){
			field = UserField.trueName;
		}
		Collection<User>  users = new ArrayList<User>();
		users.addAll(coreMailclient.searchByKeyword(keyword,"all", SearchField.CSTNET_ID, offset, count));
		users.addAll(coreMailclient.searchByKeyword(keyword,"all", SearchField.DOMAIN, offset, count));
		users.addAll(coreMailclient.searchByKeyword(keyword,"all", SearchField.TRUE_NAME, offset, count));
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
		User princ= loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(userName, password)).getUser();
		if(princ==null){
			return null;
		}else{
			return princ.getUserPrincipal();
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
		if (service.isUsed(user.getCstnetId())){
			User localUser=service.getUserByLoginName(user.getCstnetId());
			if(localUser!=null){
				localUser.setTrueName(user.getTruename());
				service.update(localUser, false);
				return;
			}
			User coreMailUser=coreMailclient.getCoreMailUserInfo(user.getCstnetId());
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
	@RestMethod("searchByKeyword")
	public List<UMTUser> searchByKeyword(String keyWord,SearchScope scope,SearchField field,int offset,int size){
		return searchByKeyword(keyWord, "all", scope, field, offset, size);
	}
	/**
	 * 根据关键字搜索
	 * */
	@RestMethod("searchByKeywordAndDomain")
	public List<UMTUser> searchByKeyword(String keyWord,String domain,SearchScope scope,SearchField field,int offset,int size){
		Collection<User> result=null;
		 if(CommonUtils.isNull(keyWord)){
			  return null;
		  }
		keyWord=keyWord.replaceAll("%", "").replaceAll("_", "");
		if(CommonUtils.isNull(keyWord)){
			  return null;
		 }
		switch(scope){
			case UMT:{
				result=service.searchUmtOnly(keyWord, offset, size);
				break;
			}
			case CORE_MAIL:{
				result=(Collection<User>)coreMailclient.searchByKeyword(keyWord,domain, field, offset, size);
				break;
			} 
		}
		List<UMTUser> finalResult=null;
		if(result!=null){
			finalResult=new ArrayList<UMTUser>();
			for(User u:result){
				finalResult.add(toUMTUser(u));
			}
		}
		return finalResult;
	}
	/**
	 * 生成umtId并返回
	 * */
	@RestMethod("generateUmtId")
	public synchronized String[] generateUmtId(List<String> cstnetIds)throws Exception{
		
		if(CommonUtils.isNull(cstnetIds)){
			return new String[]{};
		}
		String[] umtIds=new String[cstnetIds.size()];
		int index=0;
		for(String cstnetId:cstnetIds){
			User user=service.getUserByLoginName(cstnetId);
			if(user==null&&coreMailclient.isUserExt(cstnetId)){
				user=coreMailclient.getCoreMailUserInfo(cstnetId);
				service.create(user, LoginNameInfo.STATUS_ACTIVE);
			}
			if(user!=null){
				umtIds[index]=user.getUmtId();	
			}
			index++;
		}
		return umtIds;
		
	}
	
	/**
	 * 判断域名是否存在
	 * */
	@RestMethod("domainExist")
	public boolean domainExist(String domainName){
		return coreMailclient.domainExist(domainName);
	}

	private UMTUser toUMTUser(User user){
		if (user!=null){
			UMTUser umtUser= new UMTUser();
			umtUser.setCstnetId(user.getCstnetId());
			umtUser.setUmtId(user.getUmtId());
			umtUser.setSecondaryEmails(user.getSecondaryEmails());
			umtUser.setSecurityEmail(user.getSecurityEmail());
			umtUser.setTruename(user.getTrueName());
			if(CommonUtils.isNull(user.getTrueName())){
				umtUser.setTruename(user.getCstnetId().substring(0,user.getCstnetId().indexOf("@")));
			}
			umtUser.setAuthBy(user.getType());
			if(user.isCoreMailOrUc()){
				umtUser.setCstnetIdStatus(LoginNameInfo.STATUS_ACTIVE);
			}else{
				LoginNameInfo nameInfo=loginNameService.getALoginNameInfo(user.getId(), user.getCstnetId());
				umtUser.setCstnetIdStatus(nameInfo==null?null:nameInfo.getStatus());
			}
			return umtUser;
		}
		return null;
	}
	
	
	private User toUser(UMTUser user){
		User u = new User();
		u.setUmtId(user.getUmtId());
		u.setCstnetId(user.getCstnetId());
		u.setPassword(user.getPassword());
		u.setTrueName(user.getTruename());
		return u;
	}
}
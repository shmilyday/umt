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
package cn.vlabs.umt.ui.rest.uaf;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.BeanFactory;

import cn.cnic.uaf.common.trans.UnifiedUser;
import cn.vlabs.commons.principal.UserPrincipal;
import cn.vlabs.duckling.api.umt.rmi.exception.ErrorCode;
import cn.vlabs.duckling.api.umt.rmi.user.UMTUser;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.server.annotation.RestMethod;
import cn.vlabs.umt.services.user.LoginService;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UserField;
import cn.vlabs.umt.services.user.bean.UsernamePasswordCredential;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.ui.UMTContext;

public class RestUserServiceImpl {
	private UserService service;
	private LoginService loginService;

	private static Map<String, String> userSessionMap = new HashMap<String, String>();

	public RestUserServiceImpl() {
		BeanFactory factory = UMTContext.getFactory();
		service = (UserService) factory.getBean("UserService");
		loginService = (LoginService) factory.getBean("LoginService");
	}

	@RestMethod("createUser")
	public void createUser(UMTUser user) throws ServiceException {
		if (service.getUserByLoginName(user.getUsername()) != null) {
			throw new ServiceException(ErrorCode.USER_EXIST, "用户已存在");
		}
		try {
			service.create(toUser(user),LoginNameInfo.STATUS_ACTIVE);
		} catch (InvalidUserNameException e) {
			throw new ServiceException(ErrorCode.USER_EXIST, "用户已存在");
		}
	}

	@RestMethod("getUser")
	public UMTUser getUMTUser(String username) {
		User umtUser = service.getUserByLoginName(username);
		return toUMTUser(umtUser);
	}

	@RestMethod("isExist")
	public boolean[] isExist(Collection<String> usernames) {
		if (usernames == null || usernames.size() == 0) {
			return new boolean[0];
		}
		String[] users = toArray(usernames);
		boolean[] results = new boolean[usernames.size()];
		Set<String> exists = service.isExist(users);
		if (exists != null) {
			int i = 0;
			for (String username : usernames) {
				results[i] = exists.contains(username);
				i++;
			}
		}
		return results;
	}

	private String[] toArray(Collection<String> usernames) {
		String[] users = new String[usernames.size()];
		int i = 0;
		for (String username : usernames) {
			users[i] = username;
			i++;
		}
		return users;
	}

	@RestMethod("search")
	public List<UMTUser> search(String keyword, int offset, int count,
			String orderBy, boolean isAscendent) {
		if (keyword == null || keyword.equals("")) {
			return new ArrayList<UMTUser>();
		}

		keyword = keyword.replaceAll("%", "");

		if (count <= 0)
			count = Integer.MAX_VALUE;

		if (offset <= 0)
			offset = 0;
		UserField field = UserField.cstnetId;
		if (UMTUser.FIELD_EMAIL.equals(orderBy)) {
			field = UserField.cstnetId;
		}

		if (UMTUser.FIELD_TRUE_NAME.equals(orderBy)) {
			field = UserField.trueName;
		}

		Collection<User> users = service.search(keyword, offset, count, field,
				isAscendent);
		ArrayList<UMTUser> rusers = new ArrayList<UMTUser>();
		if (users != null) {
			for (User user : users) {
				rusers.add(toUMTUser(user));
			}
		}
		return rusers;
	}

	@RestMethod("login")
	public UserPrincipal login(String userName, String password) {
		return loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(userName,
				password)).getUserPrincipal();
	}

	@RestMethod("asslogin")
	public UnifiedUser asslogin(String userName, String password) {

		UserPrincipal user = loginService.loginAndReturnPasswordType(new UsernamePasswordCredential(
				userName, password)).getUserPrincipal();
		UnifiedUser backuser = null;
		if (user != null) {
			backuser = new UnifiedUser();
			backuser.setUserName(user.getName());
			backuser.setNickName(user.getDisplayName());
			backuser.setEmail(user.getName());
			String uuid = UUID.randomUUID().toString();
			userSessionMap.put("userssion", uuid);
		}
		return backuser;
	}

	@RestMethod("verifyTicket")
	public String verifyTicket(String ticket) throws ServiceException {
		return "11";
	}
	
	@RestMethod("searchCount")
	public int searchCount(String keyword) {
		return service.searchCount(keyword);
	}

	@RestMethod("updateUser")
	public void updateUser(UMTUser user) throws ServiceException {
		if (service.getUserByLoginName(user.getUsername()) != null) {
			service.update(toUser(user), true);
		} else
			throw new ServiceException(ErrorCode.USER_NOT_FOUND, "要更新的用户未找到");
	}

	@RestMethod("updateUserWithoutPwd")
	public void updateUserWithoutPwd(UMTUser user) throws ServiceException {
		if (service.getUserByLoginName(user.getUsername()) != null) {
			service.update(toUser(user), false);
		} else
			throw new ServiceException(ErrorCode.USER_NOT_FOUND, "要更新的用户未找到");
	}

	private UMTUser toUMTUser(User user) {
		if (user != null) {
			return new UMTUser(user.getCstnetId(), user.getTrueName(),
					user.getCstnetId(), "");
		}
		return null;
	}

	private User toUser(UMTUser user) {
		User u = new User();
		u.setPassword(user.getPassword());
		u.setTrueName(user.getTruename());
		u.setCstnetId(user.getUsername().toLowerCase());
		return u;
	}
}
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
package cn.vlabs.umt.services.role;

import java.util.Collection;

import cn.vlabs.umt.services.user.bean.User;

public interface RoleService {
	static final String BEAN_ID="RoleService";
	/**
	 * 添加成员
	 * @param rolename
	 * @param username
	 */
	void addMember(String rolename, String username);
	/**
	 * 删除成员
	 * @param rolename
	 * @param username
	 */
	void removeMemeber(String rolename, String username);
	/**
	 * 查询角色的用户列表
	 * @param rolename
	 * @return
	 */
	Collection<User> getRoleMembers(String rolename);
	/**
	 * 查询用户的角色信息
	 * @param username
	 * @return
	 */
	UMTRole[] getUserRoles(String username);
	/**
	 * 删除成员
	 * @param string
	 * @param usernames
	 */
	void removeMemeber(String string, String[] usernames);
}
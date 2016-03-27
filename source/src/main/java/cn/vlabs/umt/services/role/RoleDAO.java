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
package cn.vlabs.umt.services.role;

import java.util.Collection;

import cn.vlabs.umt.services.user.bean.User;

public interface RoleDAO {
	/**
	 * 查询用户的角色信息
	 * @param username
	 * @return
	 */
	UMTRole[] getRoles(String username);
	/**
	 * 查询角色的用户信息
	 * @param rolename
	 * @return 用户ID列表
	 */
	Collection<User> getRoleMembers(String rolename);
	/**
	 * 查询角色信息
	 * @param rolename
	 * @return
	 */
	UMTRole getRole(String rolename);
	/**
	 * 添加成员
	 * @param roleid
	 * @param userid
	 */
	void addMember(int roleid, int userid);
	/**
	 * 删除成员
	 * @param roleid
	 * @param userid
	 */
	void removeMember(int roleid, int userid);
	/**
	 * @param roleName
	 * @param uid
	 * @return
	 */
	boolean isMemberOf(String roleName, int uid);
	
}
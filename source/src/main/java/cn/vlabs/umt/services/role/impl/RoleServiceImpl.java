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
package cn.vlabs.umt.services.role.impl;

import java.util.Collection;

import cn.vlabs.umt.services.role.RoleDAO;
import cn.vlabs.umt.services.role.RoleService;
import cn.vlabs.umt.services.role.UMTRole;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.User;

public class RoleServiceImpl implements RoleService {
	public RoleServiceImpl(UserService us,RoleDAO rd){
		this.rd=rd;
		this.us=us;
	}
	public Collection<User> getRoleMembers(String rolename) {
		return rd.getRoleMembers(rolename);
	}

	public UMTRole[] getUserRoles(String username) {
		return rd.getRoles(username);
	}
	public void addMember(String rolename, int uid) {
		UMTRole role = rd.getRole(rolename);
		if ( role!=null)
		{
			rd.addMember(role.getId(), uid);
		}
	}
	public void removeMemeber(String rolename, int uid) {
		UMTRole role = rd.getRole(rolename);
		if (role!=null )
		{
			rd.removeMember(role.getId(), uid);
		}
	}
	
	public void removeMemeber(String rolename, String[] usernames) {
		UMTRole role = rd.getRole(rolename);
		if (role!=null){
			for (String username:usernames){
				User u = us.getUserByUmtId(username);
				if (u!=null)
				{
					rd.removeMember(role.getId(), u.getId());
				}
			}
		}
	}
	
	
	@Override
	public boolean isMemberOf(String roleName, int uid) {
		return rd.isMemberOf(roleName,uid);
	}
	private RoleDAO rd;
	private UserService us;
}
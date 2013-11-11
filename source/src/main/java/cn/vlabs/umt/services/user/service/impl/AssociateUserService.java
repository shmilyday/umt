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
package cn.vlabs.umt.services.user.service.impl;

import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.AssociateUser;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.dao.IAssociateUserDAO;
import cn.vlabs.umt.services.user.service.IAssociateUserService;

/**
 * @author lvly
 * @since 2013-3-12
 */
public class AssociateUserService implements IAssociateUserService {
	
	private IAssociateUserDAO associateDAO;
	private UserService userService;
	
	public AssociateUserService(IAssociateUserDAO associateDAO,UserService userService){
		this.associateDAO=associateDAO;
		this.userService=userService;
	}

	@Override
	public void addAssociateUser(int uid, int associateUid, String appList) {
		associateDAO.addAssociateUser(uid,associateUid,appList);
	}

	@Override
	public AssociateUser getAssociate(int uid) {
		return associateDAO.getAssociate(uid);
	}

	@Override
	public void updateAssociateUser(int uid, int associateUid, String appList) {
		associateDAO.updateAssociateUser(uid,associateUid,appList);
	}
	@Override
	public boolean isAssociated(int uid) {
		return associateDAO.isAssociated(uid);
	}
	@Override
	public boolean isAssociated(String loginName) {
		User user=userService.getUserByLoginName(loginName);
		if(user!=null){
			return associateDAO.isAssociated(user.getId());
		}
		return false;
	}

}
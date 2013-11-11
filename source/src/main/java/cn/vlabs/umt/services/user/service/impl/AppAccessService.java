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

import java.util.List;

import cn.vlabs.umt.services.user.dao.IAppAccessDAO;
import cn.vlabs.umt.services.user.service.IAppAccessService;

/**
 * @author lvly
 * @since 2013-3-12
 */
public class AppAccessService implements IAppAccessService{
	private IAppAccessDAO appAccessDAO;

	public AppAccessService(IAppAccessDAO appAccessDAO){
		this.appAccessDAO=appAccessDAO;
	}
	@Override
	public void createAppAccess(int uid, String appName) {
		if(!appAccessDAO.isAccessed(uid, appName)){
			appAccessDAO.createAppAccess(uid,appName);
		}
	}

	@Override
	public List<String> getMyAppAccesses(int uid) {
		return appAccessDAO.getMyAppAccesses(uid);
	}
	
}
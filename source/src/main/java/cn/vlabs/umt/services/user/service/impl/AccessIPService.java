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

import cn.vlabs.umt.services.user.bean.AccessIP;
import cn.vlabs.umt.services.user.dao.IAccessIPDAO;
import cn.vlabs.umt.services.user.service.IAccessIPService;

/**
 * @author lvly
 * @since 2013-3-15
 */
public class AccessIPService implements IAccessIPService {
	private IAccessIPDAO dao;
	public AccessIPService(IAccessIPDAO dao){
		this.dao=dao;
	}

	@Override
	public void addAccessIp(int uid, String ip) {
		dao.addAccessIp(uid, ip);
	}

	@Override
	public List<AccessIP> getAllAccessIps() {
		return dao.getAllAccessIps();
	}

	@Override
	public boolean canAccess(String ip) {
		return dao.canAccess(ip);
	}

	@Override
	public void deleteIp(int ipId) {
		dao.deleteIp(ipId);
	}

}
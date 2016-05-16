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
package cn.vlabs.umt.services.user.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.duckling.falcon.api.cache.ICacheService;
import net.duckling.vmt.api.domain.VmtOrgDomain;
import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.account.ICoreMailClient;
import cn.vlabs.umt.services.user.bean.OrgDomain;
import cn.vlabs.umt.services.user.dao.IDomainDAO;
import cn.vlabs.umt.services.user.service.IDomainService;

public class DomainServiceImpl implements IDomainService{
	public static final String MEM_CACHE_DAO_KEY="domain.from.vmt.";
	private List<String> keys=new ArrayList<String>();
	
	private IDomainDAO domainDAO;
	private ICacheService cacheService;
	
	public DomainServiceImpl(IDomainDAO domainDAO,ICacheService cacheService){
		this.domainDAO=domainDAO;
		this.cacheService=cacheService;
	}
	@Override
	public List<OrgDomain> getAllByDetail() {
		return domainDAO.getAllByDetail();
	}
	@Override
	public void insertOrgDomain(List<VmtOrgDomain> domains) {
		domainDAO.insertOrgDomain(domains);
	}
	@Override
	public OrgDomain getDetailByEmail(String email) {
		if(ICoreMailClient.isBlack(email)){
			return null;
		}
		String domain=CommonUtils.trim(email.split("@")[1]);
		String memKey=MEM_CACHE_DAO_KEY.concat(domain);
		OrgDomain orgDomain=(OrgDomain)cacheService.get(memKey);
		if(orgDomain==null){
			orgDomain=domainDAO.findDetailByDomain(domain);
			cacheService.set(memKey,orgDomain);
			keys.add(memKey);
		}
		return orgDomain;
		
	}
	@Override
	public boolean checkDomain(String loginName) {
		return getDetailByEmail(loginName)!=null;
	}
}

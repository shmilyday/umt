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

import java.util.Collection;
import java.util.List;

import cn.vlabs.umt.services.user.bean.UnitDomain;
import cn.vlabs.umt.services.user.dao.IUnitDomainDAO;
import cn.vlabs.umt.services.user.service.IUnitDomainService;

public class UnitDomainService implements IUnitDomainService {

	
	private IUnitDomainDAO unitDomainDAO;
	
	public UnitDomainService(IUnitDomainDAO unitDomainDAO){
		this.unitDomainDAO=unitDomainDAO;
	}
	
	@Override
	public List<UnitDomain> findAll() {
		return unitDomainDAO.findAll();
	}

	@Override
	public int add(UnitDomain unitDomain) {
		return unitDomainDAO.add(unitDomain);
	}

	@Override
	public List<UnitDomain> findByRootDomain(String rootDomain) {
		return unitDomainDAO.findByRootDomain(rootDomain);
	}

	@Override
	public List<UnitDomain> findByMailDomain(String mailDomain) {
		return unitDomainDAO.findByMailDomain(mailDomain);
	}

	@Override
	public UnitDomain findById(int id) {
		return unitDomainDAO.findById(id);
	}

	@Override
	public boolean deleteById(int id) {
		return unitDomainDAO.deleteById(id);
	}

	@Override
	public boolean delete(UnitDomain unitDomain) {
		return unitDomainDAO.delete(unitDomain);
	}

	@Override
	public boolean update(UnitDomain unitDomain) {
		return unitDomainDAO.update(unitDomain);
	}
	
	@Override
	public Collection<UnitDomain> search(String query,int  start, int count) {
		return unitDomainDAO.search(query, start, count);
	}

	@Override
	public int searchCount(String query){
		return unitDomainDAO.searchCount(query);
	}

	@Override
	public int getUnitsCount() {
		return unitDomainDAO.getUnitsCount();
	}

	@Override
	public Collection<UnitDomain> getUnits(int start, int count) {
		return unitDomainDAO.getUnits(start, count);
	}

	@Override
	public boolean isNameUsed(String name) {
		List<UnitDomain> list=unitDomainDAO.findByName(name);
		return list==null?false:list.size()>0?true:false;
	}

	@Override
	public boolean isRootDomainUsed(String rootDomain) {
		List<UnitDomain> list=unitDomainDAO.findByRootDomain(rootDomain);
		return list==null?false:list.size()>0?true:false;
	}

	@Override
	public void delete(int[] ids) {
		unitDomainDAO.delete(ids);
		
	}
	
	@Override
	public String getRootDomainByMailArreess(String mailAddress){
		String mailDomain=mailAddress.split("@")[1];
		return getRootDomainByMailDomain(mailDomain);
	}
	@Override
	public String getRootDomainByMailDomain(String mailDomain){
		List<UnitDomain> unitDomainList=unitDomainDAO.findByMailDomainLike(mailDomain+UnitDomain.MAILDOMAIN_SPLIT);
		if(unitDomainList==null||unitDomainList.size()<1){
			return "";
		}
		
		UnitDomain unitDomain=unitDomainList.get(0);
		return unitDomain.getRootDomain();
		
	}
	

}

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
package cn.vlabs.umt.services.user.service;

import java.util.Collection;
import java.util.List;

import cn.vlabs.umt.services.user.bean.UnitDomain;

public interface IUnitDomainService {

	List<UnitDomain> findAll();
	int add(UnitDomain unitDomain);
	List<UnitDomain> findByRootDomain(String rootDomain);
	List<UnitDomain> findByMailDomain(String mailDomain);
	UnitDomain findById(int id);
	boolean deleteById(int id);
	boolean delete(UnitDomain unitDomain);
	boolean update(UnitDomain unitDomain);
	int searchCount(String query);
	Collection<UnitDomain> search(String query, int start, int count);
	int getUnitsCount();
	Collection<UnitDomain> getUnits(int start, int count);
	boolean isNameUsed(String name);
	boolean isRootDomainUsed(String rootDomain);
	void delete(int[] ids);
	String getRootDomainByMailArreess(String mailAddress);
	String getRootDomainByMailDomain(String mailDomain);
}

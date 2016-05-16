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

import java.util.List;

import cn.vlabs.umt.services.user.bean.CaApplication;
import cn.vlabs.umt.services.user.dao.ICaApplicationDAO;
import cn.vlabs.umt.services.user.service.ICaApplicationService;

public class CaApplicationService implements ICaApplicationService {
	
	private ICaApplicationDAO caApplicationDAO;

	public void setCaApplicationDAO(ICaApplicationDAO caApplicationDAO) {
		this.caApplicationDAO = caApplicationDAO;
	}

	@Override
	public int createCaApplication(CaApplication caApplication) {
		return caApplicationDAO.createCaApplication(caApplication);
	}

	@Override
	public void removeCaApplication(int CaApplicationid) {
		caApplicationDAO.removeCaApplication(CaApplicationid);
	}

	@Override
	public CaApplication getCaApplication(int caApplicationid) {
		return caApplicationDAO.getCaApplication(caApplicationid);
	}

	@Override
	public List<CaApplication> getCaApplicationByUidAndType(int uid, int type, int status) {
		return caApplicationDAO.getCaApplicationByUidAndType(uid, type, status);
	}

}

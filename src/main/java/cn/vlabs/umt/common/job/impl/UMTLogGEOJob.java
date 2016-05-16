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
package cn.vlabs.umt.common.job.impl;

import cn.vlabs.umt.common.job.Jobable;
import cn.vlabs.umt.common.util.GeoIPUtils;
import cn.vlabs.umt.domain.GEOInfo;
import cn.vlabs.umt.domain.UMTLog;
import cn.vlabs.umt.services.account.IUMTLogDAO;

public class UMTLogGEOJob implements Jobable {
	private IUMTLogDAO umtLogDAO;
	private int logId;
	private int uid;

	public UMTLogGEOJob(
			IUMTLogDAO umtLogDAO, int logId,int uid) {
		this.umtLogDAO = umtLogDAO;
		this.logId = logId;
		this.uid=uid;
	}


	@Override
	public void doJob() {
		UMTLog log = umtLogDAO.getLogById(uid, logId);
		if (log == null ) {
			return;
		}
		GEOInfo cityName = GeoIPUtils.getGEOInfo(log.getUserIp());
		if (cityName == null) {
			return;
		}
		log.setCountry(cityName.getCountry());
		log.setProvince(cityName.getProvince());
		log.setCity(cityName.getCity());
		log.setCstnetUnit(cityName.isCstnetUnit());
		log.setUnitName(cityName.getUnitName());
		log.setFromDip(cityName.isFromDip());
		umtLogDAO.updateGEOInfo(log);
	}

	@Override
	public String getJobId() {
		return "umt.log.geo.update" + uid + "." + System.currentTimeMillis();
	}

	@Override
	public boolean isJobEquals(Jobable job) {
		return false;
	}

}

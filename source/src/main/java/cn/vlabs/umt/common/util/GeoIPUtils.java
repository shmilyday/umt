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
package cn.vlabs.umt.common.util;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import cn.vlabs.umt.domain.GEOInfo;
import cn.vlabs.umt.ui.UMTContext;

/**
 * ip查询
 * */
public class GeoIPUtils {
	private static final Logger LOGGER = Logger.getLogger(GeoIPUtils.class);

	public static GEOInfo getGEOInfo(String ip) {
		String dipUrl = getConfigUrl();
		GEOInfo info = new GEOInfo();
		info.setFromDip(true);
		if (dipUrl == null) {
			LOGGER.error("can not found dip url in config");
			return info;
		}
		HttpGet get = new HttpGet(dipUrl + ip);
		String json = get.connect();
		if (CommonUtils.isNull(json)) {
			LOGGER.error("dip return null!");
			return null;
		}
		try {
			JSONObject jsonObj = (JSONObject) new JSONParser().parse(json);
			if (toBool(jsonObj.get("success"))) {
				JSONObject data = (JSONObject) jsonObj.get("data");
				info.setCountry(toStr(data.get("country")));
				info.setProvince(toStr(data.get("province")));
				info.setCity(toStr(data.get("city")));
				info.setUnitName(toStr(data.get("unitName")));
				info.setCstnetUnit(toBool(data.get("cstnetUnit")));
				info.setIp(ip);
				return info;

			} else {
				LOGGER.error("search ip error,desc[" + jsonObj.get("desc")
						+ "]");
			}
		} catch (ParseException e) {
			LOGGER.error("", e);
		}
		return null;
	}

	private static String toStr(Object obj) {
		return obj == null ? null : obj.toString();
	}

	private static boolean toBool(Object bool) {
		if (bool == null) {
			return false;
		}
		return "true".equals(bool.toString());
	}

	private static Config config;

	private static String getConfigUrl() {
		if (config == null) {
			config = (Config) UMTContext.getFactory().getBean("Config");
		}
		if (config == null) {
			return null;
		}
		return config.getStringProp("dip.url", null);
	}

}

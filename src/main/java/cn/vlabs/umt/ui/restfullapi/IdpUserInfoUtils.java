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
package cn.vlabs.umt.ui.restfullapi;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;

import cn.vlabs.umt.common.util.PinyinUtil;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;

public class IdpUserInfoUtils {
	public static JSONObject buildUserInfoJSON(User user, LoginNameInfo loginNameInfo,String orgRootDomain) {
		JSONObject object = new JSONObject();
		object.put("umtId", user.getUmtId());
		object.put("truename", user.getTrueName());
		object.put("type", user.getType());
		object.put("cstnetId", user.getCstnetId().toLowerCase());
		object.put("cstnetIdStatus", loginNameInfo.getStatus());
		object.put("orgRootDomain", orgRootDomain);
		String pinyinName=PinyinUtil.getPinyinMingXing(user.getTrueName());
		String xing=pinyinName;
		String ming=pinyinName;
		if(StringUtils.split(pinyinName, " ").length==2){
			xing=StringUtils.split(pinyinName, " ")[1];
			ming=StringUtils.split(pinyinName, " ")[0];
		}
		object.put("xing", xing);
		object.put("ming", ming);
		object.put("pinyinName", pinyinName);
		
		return object;
	}
}

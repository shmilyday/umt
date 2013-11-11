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
package cn.vlabs.umt.services.user.dao;

import java.util.List;

import cn.vlabs.umt.services.user.bean.BindInfo;

/**
 * 绑定第三方账户
 * @author lvly
 * @since 2013-2-25
 */
public interface IBindThirdPartyDAO {
	/***
	 * 用户绑定第三方账户
	 * @param uid umtUid
	 * @param screenName 第三方用户名
	 * @param openId 第三方用户标识
	 * @param type 第三方类型
	 */
	void bindThirdParty(BindInfo bindInfo);
	
	/***
	 * 获得用户的所有第三方绑定关系
	 * @param uid 用户id
	 * @return 获取用户的第三方绑定关系
	 * */
	List<BindInfo> getBindInfosByUid(int uid);
	
	/**
	 * 解除与第三方的绑定关系
	 * @param bindId 绑定id
	 * */
	void deleteBindById(int bindId);
	/**
	 * 解除一个用户的所有绑定关系
	 * @param uid 用户id
	 * */
	void deleteBindByUid(int[] uid);
}
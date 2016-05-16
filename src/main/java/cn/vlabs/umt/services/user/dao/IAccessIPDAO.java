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
package cn.vlabs.umt.services.user.dao;

import java.util.List;

import cn.vlabs.umt.services.user.bean.AccessIP;

/**
 * @author lvly
 * @since 2013-3-15
 */
public interface IAccessIPDAO {
	static final String BEAN_ID="accessIPDAO";
	/**
	 * 增加允许访问的ip
	 * @param scope TODO
	 * @param desc TODO
	 * */
	void addAccessIp(int uid,String ip, String scope, String desc);
	/**
	 * 获得所有允许访问的ip
	 * */
	List<AccessIP> getAllAccessIps();
	/**
	 * 此ip是否允许访问
	 * */
	boolean canAccess(String ip);
	/**
	 * 删除指定ip
	 **/
	void deleteIp(int ipId);
	boolean canAccess(String ip, String scope);

}
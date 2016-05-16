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

import java.util.List;

/**
 * @author lvly
 * @since 2013-3-12
 */
public interface IAppAccessService {
	
	static final String BEAN_ID="appAccessService";
	
	/**
	 * 创建应用访问记录
	 * @param uid 
	 * @param appName 应用名称
	 * */
	void createAppAccess(int uid,String appName);
	/**
	 * 获取我应用的访问记录
	 * @param uid
	 * */
	List<String> getMyAppAccesses(int uid);
}
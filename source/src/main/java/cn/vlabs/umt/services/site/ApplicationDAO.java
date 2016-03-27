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
package cn.vlabs.umt.services.site;

import java.util.Collection;

/**
 * 应用的DAO
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 7, 2009 10:23:13 AM
 */
public interface ApplicationDAO {
	/**
	 * 查询应用信息
	 * @param name 应用程序的名称
	 * @return 应用的详细信息
	 */
	Application getApplication(String name);
	/**
	 * 创建新的APP
	 * @param app 应用的详细信息
	 * @return 新应用的ID号
	 */
	int createApplication(Application app);
	/**
	 * 更新应用信息
	 * @param app 应用的详细信息
	 */
	void updateApplcation(Application app);
	/**
	 * 删除应用信息
	 * @param name 应用的名称
	 */
	void deleteApplication(String name);
	/**
	 * 删除应用信息
	 * @param appid 应用的ID
	 */
	void deleteApplication(int appid);
	/**
	 * 修改应用的KeyID
	 * @param appid 应用的ID
	 * @param keyid	应用的KeyID
	 */
	void changePublicKey(int appid, int keyid);
	/**
	 * 查询应用
	 * @param start 开始记录数
	 * @param count 查询的记录数量
	 * @return 返回的应用集合。
	 */
	Collection<Application> getApplications(int start, int count);
	/**
	 * 查询应用的数量
	 * @return 应用总数
	 */
	int getApplicationCount();
	Application getApplication(int appid);
}
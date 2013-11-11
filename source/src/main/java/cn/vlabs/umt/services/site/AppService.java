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
package cn.vlabs.umt.services.site;

import java.util.Collection;


/**
 * 提供应用的管理功能。
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 7, 2009 9:37:19 AM
 */
public interface AppService {
	/**
	 * 查询应用信息
	 * @param name 应用程序的名称
	 * @return 应用的详细信息
	 */
	Application getApplication(String name);
	Application getApplication(int appid);
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
	 * 更新应用的Key
	 * @param appid 应用的ID
	 * @param publickey 应用的Key
	 * @return 新的KeyID
	 */
	int updatePublicKey(int appid, String publickey);
	/**
	 * 加密信息
	 * @param appname 使用的应用公钥
	 * @param message 被加密的信息
	 * @return 使用应用公钥加密以后的结果。
	 * @throws ApplicationNotFound 如果应用未找到，抛出该异常。
	 */
	String encrypt(String appname, String message) throws ApplicationNotFound;
	/**
	 * 查询公钥
	 * @param keyid 公钥ID
	 * @return 公钥的内容
	 */
	String getPublicKey(int keyid);
	/**
	 * 查询应用
	 * @return 应用信息
	 */
	Collection<Application> getAllApplication(int start, int count);
	/**
	 * 查询应用总数
	 * @return 应用的数量
	 */
	int getApplicationCount();
}
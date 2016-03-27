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
/**
 * 操作Key的DAO接口
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 7, 2009 9:55:15 AM
 */
public interface KeyDAO {
	/**
	 * 注册新的公钥
	 * @param keycontent 公钥的内容
	 * @return 新的公钥的ID
	 */
	int create(String keycontent);
	/**
	 * 查询公钥的内容
	 * @param keyid 公钥的ID
	 * @return 公钥的内容
	 */
	String getContent(int keyid);
}
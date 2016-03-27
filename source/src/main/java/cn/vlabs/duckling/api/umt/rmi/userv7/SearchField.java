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
package cn.vlabs.duckling.api.umt.rmi.userv7;

/**
 * 检索条件
 * @author lvly
 * @since 2013-4-25
 */
public enum SearchField {
	/**
	 * 真实姓名
	 * **/
	TRUE_NAME,
	/**
	 * 按照域名搜索,umt不支持此写法
	 * */
	DOMAIN,
	/**
	 * 邮箱,umt理解为a@a.com,coreMail会理解为a
	 * */
	CSTNET_ID,
}
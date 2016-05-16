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
 * 检索范围
 * @author lvly
 * @since 2013-4-25
 */
public enum SearchScope {
	/**
	 * 在umt搜索用户
	 * */
	UMT,
	/**
	 * 在coreMail搜索用户
	 * */
	CORE_MAIL,
}
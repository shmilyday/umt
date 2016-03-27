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
/**
 * 这个类用作变换用户密码
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 7, 2009 1:35:42 PM
 */
public interface ITransform {
	static final String BEAN_ID="transform";
	
	 static final String TYPE_MD2 = "MD2";

	 static final String TYPE_MD5 = "MD5";

	 static final String TYPE_NONE = "NONE";

	 static final String TYPE_SHA = "SHA";

	 static final String TYPE_SHA_256 = "SHA-256";

	 static final String TYPE_SHA_384 = "SHA-384";

	 static final String TYPE_SSHA = "SSHA";
	 static final String TYPE_NT_HASH="NT-Hash";
	/**
	 * 变换函数
	 * @param value 变换前的值
	 * @return 变换后的值
	 */
	String transform(String value);
}
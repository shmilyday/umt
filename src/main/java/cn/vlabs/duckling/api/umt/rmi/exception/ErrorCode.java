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
package cn.vlabs.duckling.api.umt.rmi.exception;

public final class ErrorCode {
	private ErrorCode(){
		
	}
	/**
	 * 无效的代理证书
	 */
	public static final int WRONG_PROXY=401;
	/**
	 * 没有权限操作
	 */
	public static final int ACCESS_FORBIDDEN=403;
	/**
	 * 没有登陆
	 */
	public static final int NOT_LOGIN = 402;
	/**
	 * 要创建的用户已存在
	 */
	public static final int USER_EXIST = 404;
	
	
	/**
	 * 用户不存在
	 */
	public static final int USER_NOT_FOUND = 405;
	public static final int DOMAIN_NOT_ALLOWD=406;
}
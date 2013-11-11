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
package cn.vlabs.umt.services.requests;

import java.util.Collection;

import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.ui.UMTContext;

/**
 * 用户请求的接口
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 21, 2009 8:42:40 AM
 */
public interface RequestService {
	/**
	 * 创建注册请求
	 * @param request 注册请求
	 */
	int createRequest(UserRequest request,UMTContext context);
	/**
	 * 删除注册请求
	 * @param rid 注册的ID号
	 */
	void removeRequest(int rid,UMTContext context);
	/**
	 * 同意用户注册
	 * @param rid 注册的ID号
	 * @throws UserExist 如果用户已存在，则抛出该异常
	 * @throws InvalidUserNameException 
	 */
	void approveRequest(int rid,UMTContext context) throws UserExist, InvalidUserNameException;
	/**
	 * 拒绝用户注册
	 * @param rid 注册的ID号
	 */
	void denyRequest(int rid, UMTContext context);
	/**
	 * 查询用户请求数
	 * @return 请求总数
	 */
	int getRequestCount(int state);
	/**
	 * 查询用户请求的详细信息
	 * @param states 请求状态
	 * @param start 开始记录号，从0开始
	 * @param count	记录数
	 * @return 请求列表
	 */
	Collection<UserRequest> getRequests(int state, int start, int count);
}
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
/**
 * 数据库操作接口
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 21, 2009 9:02:20 AM
 */
public interface RequestDAO {
	/**
	 * 创建新用户请求
	 * @param request 用户请求
	 * @return 用户请求ID号
	 */
	int createRequest(UserRequest request);
	/**
	 * 删除用户请求
	 * @param rid
	 */
	void removeRequest(int rid);
	/**
	 * 更新请求状态
	 * @param rid 	请求编号
	 * @param state	新的请求状态
	 */
	void updateState(int rid, int state, String operator);
	/**
	 * 查询用户请求数
	 * @return
	 */
	int getRequestCount();
	/**
	 * 查询用户请求数
	 * @param state
	 * @return
	 */
	int getRequestCount( int state);
	/**
	 * 查询用户请求详细信息
	 * @return
	 */
	Collection<UserRequest> getRequests(int start, int count);
	/**
	 * 查询用户请求详细信息
	 * @param state
	 * @return
	 */
	Collection<UserRequest> getRequests(int state, int start, int count);
	/**
	 * 查询用户注册请求信息
	 * @param rid 用户注册请求ID
	 * @return 用户注册请求的详细信息
	 */
	UserRequest getRequest(int rid);
}
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
package cn.vlabs.umt.services.user.dao;

import java.util.Collection;
import java.util.List;

import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UserField;

public interface IUserDAO {
	/**
	 * 创建新用户
	 * @param user 用户的信息
	 * @return 用户ID
	 */
	int create(User user);
	/**
	 * 删除用户
	 * @param userid 用户的ID
	 */
	void remove(int userid);
	/**
	 * 更新用户信息
	 * @param user 用户信息
	 */
	void update(User user);
	/**
	 * 更新用户信息（不更新用户密码）
	 * @param user
	 */
	void updateWithoutPass(User user);
	/**
	 * 修改用户密码
	 * @param uid	用户id
	 * @param password	新密码
	 */
	void updatePassword(int uid, String password);
	/**
	 * 查询用户,根据用户名
	 * @param loginName 登录名
	 * @return	用户信息
	 */
	List<User> getUsersByUmtId(List<String> loginName);
	/**
	 * 查询用户
	 * @param uid 数据库主键
	 * @return	用户信息
	 */
	User getUserByUid(int uid);
	/**
	 * 查询用户
	 * @param openid openid信息
	 * @param url 
	 * @param type 
	 * @return	用户信息
	 */
	User getUserByOpenid(String openid, String type, String url);
	/**
	 * 查询用户总数
	 * @return 数据库中注册用户总数
	 */
	int getUserCount();
	/**
	 * 查询用户信息
	 * @param start
	 * @param count
	 * @return
	 */
	Collection<User> getUsers(int start, int count);
	/**
	 * 批量删除用户
	 * @param uids
	 */
	void remove(int[] uids);
	/**
	 * 查询用户
	 * @param query
	 * @param isAscendent 
	 * @param orderBy 
	 * @return
	 */
	Collection<User> search(String query, int start, int count, UserField orderBy, boolean isAscendent);
	/**
	 * 查询匹配条件的用户数
	 * @param query
	 * @return
	 */
	int searchCount(String query);
	/**
	 * 批量创建用户
	 * @param users
	 */
	void create(List<User> users);
	/**
	 * 更新账户的某一个属性
	 * @param uid uid
	 * @param columName 字段名
	 * @param value 值
	 * 
	 **/
	void updateValueByColumn(int uid[],String columnName, String value);
	
	/**
	 * 查询用户密码是否正确
	 * @param loginName  登陆名
	 * @param password 加过密的密码
	 * @return User 匹配的用户
	 * */
	User checkPassword(String loginName,String password);
	/**
	 * 获得最后生成的umtId
	 * @return
	 */
	String getLastedUmtId();
	/**
	 * 删除用户，除了我，因为有抢注，所以要删除这个用户
	 * @param uid
	 * @param loginName
	 */
	List<Integer> getExpectMeByCstnetId(int uid, String loginName);
	/**
	 * @param keyword
	 * @param offset
	 * @param size
	 * @return
	 */
	Collection<User> searchUmtOnly(String keyword, int offset, int size);
	List<User> getUsersByIds(List<String> uids);
	void switchGEOInfo(int uid, boolean userSwitch);
}
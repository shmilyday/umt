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
package cn.vlabs.umt.services.user;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.bean.UserField;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.services.user.exception.UserNotFound;

/**
 * 用户服务的接口
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 7, 2009 11:32:26 AM
 */
public interface UserService {
	static final String BEAN_ID="UserService";
	/**
	 * 创建新用户
	 * @param user 用户的信息
	 * @param status 账户创建，是否带激活
	 * @return 用户ID
	 * @throws InvalidUserNameException 如果用户名不是合法的Email格式则抛出该异常
	 */
	int create(User user,String status) throws InvalidUserNameException	;
	/**
	 * 查询用户
	 * @param umtId 用户名
	 * @return 用户信息
	 */
	User getUserByUmtId(String umtId);
	/**
	 * 批量查询用户
	 * */
	List<User> getUsersByUmtId(List<String> umtId);
	/**
	 * 查询用户,用uid 
	 * */
	User getUserByUid(int uid);
	/**
	 * 查询用户
	 * @param openid openid信息
	 * @return 用户信息
	 */
	User getUserByOpenid(String openid,String type,String url);
	/**
	 * 查询用户，根据登录名
	 * @param loginName 登陆名
	 * */
	User getUserByLoginName(String loginName);
	/**
	 * 删除用户
	 * @param userid 用户的ID
	 */
	void removeByUid(int userid);
	/**
	 * 更新用户信息
	 * @param user 用户信息
	 */
	void update(User user);
	/**
	 * 更新用户信息
	 * @param user 
	 * @param withPassword
	 */
	void update(User user, boolean withPassword);
	
	/**
	 * 更新账户的某一个属性
	 * @param columName 字段名
	 * @param value 值
	 * @param username 登陆邮箱或者userKey
	 * 
	 * */
	void updateValueByColumn(int uid,String columnName,String value);
	/**
	 * 修改用户密码
	 * @param username	用户名
	 * @param password	新密码
	 */
	void updatePassword(int uid, String password);
	
	/**
	 * 修改CoreMail用户密码
	 * */
	void updateCoreMailPassword(String username,String password);
	/**
	 * 发送用户修改密码的邮件
	 * @param locale 国际化
	 * @param username 用户名
	 * @param baseUrl 本机器的根地址
	 * @param type security or login
	 * @throws UserNotFound 
	 */
	void sendChangeMail(Locale locale,int uid,String username,String baseURL) throws UserNotFound;
	/**
	 * 发送注册验证邮箱，以后更改登录名后难免也要激活
	 * @param  locale 主要用来取国际化参数 
	 * @param uid 用户id
	 * @param userName 要发送的地址，也就是刚注册成功或者更换成功的账户
	 * @param activeUrl 激活地址
	 * @param changeLoginName 是否是更改登录名
	 */
	void sendActivicationLoginMail(Locale locale,int uid,String loginName,String activeUrl,boolean changeLoginName,int loginNameInfoId);
	
	/**
	 * 发送主账号激活码和密保邮箱，从邮箱点的话，两者皆可激活
	 * @param  locale 主要用来取国际化参数 
	 * @param uid 用户id
	 * @param userName 要发送的地址，也就是刚注册成功或者更换成功的账户
	 * @param activeUrl 激活地址
	 * @param loginNameInfoId 账户Id
	 * 
	 * */
	void sendActivateionLoginMailAndSecurity(Locale locale,int uid,String loginName,String baseUrl,int loginNameInfoId);
	/**
	 * 提交确认邮件
	 * @param locale 国际化参数
	 * @param uid 用户id
	 * @param oldLoginName 老主账户名
	 * @param baseUrl 基本路径
	 * */
	void sendComfirmToOldMail(Locale locale,int uid,String oldloginName,String baseUrl);
	/**
	 * 发送注册验证邮箱，以后更改登录名后难免也要激活
	 * @param  locale 主要用来取国际化参数 
	 * @param uid 用户id
	 * @param loginName 要发送的地址，也就是刚注册成功或者更换成功的账户
	 * @param activeUrl 激活地址
	 * @param loginNameId 用户名id
	 * @param isChange 是否是更改激活
	 */
	public void sendActivicationSecondaryEmail(Locale locale,int uid,String loginName,String activeUrl,int loginNameId,boolean isChange);
	/**
	 * 发送注册密保邮箱，以后更改登录名后难免也要激活
	 * @param locale 主要用来取国际化参数
	 * @param loginEmail
	 * @param securityEmail 要发送的地址，也就是刚注册成功或者更换成功的账户
	 * @param activeUrl 激活地址
	 * @return null
	 * */
	public void sendActivicationSecurityMail(Locale locale,int uid,String securityEmail,String activeUrl);
	
	
	/**
	 * 应用调用的时候
	 * */
	public void sendWarnUCUser(String umtId,String baseUrl);
	/**
	 * 删除Token
	 * @param tokenid tokend的ID?
	 */
	void removeToken(int tokenid);
	/**
	 * 查询用户总数
	 * @return
	 */
	int getUserCount();
	/**
	 * 查询用户信息
	 * @param start
	 * @param recordPerPage
	 * @return
	 */
	Collection<User> getUsers(int start, int count);
	/**
	 * 删除用户
	 * @param usernames
	 */
	void remove(int[] uids);
	/**
	 * 判断用户是否存在
	 * @param usernames
	 * @return
	 */
	Set<String> isExist(String[] usernames);
	/**
	 * 查询用户,老方法，不建议使用
	 * @param query
	 * @return
	 */
	Collection<User> search(String query,int start, int count);
	/**
	 * 查询用户,老方法，不建议使用
	 * @param query
	 * @param start
	 * @param count
	 * @param orderBy  "username, email, truename"
	 * @param isAscendent
	 * @return
	 */
	Collection<User> search(String query,int start, int count, UserField orderBy, boolean isAscendent);
	
	/**
	 * 只查询umt用户
	 * @param keyword 关键字
	 * @param offset 偏移
	 * @param size 数量
	 * */
	Collection<User> searchUmtOnly(String keyword,int offset,int size);
	/**
	 * 查询用户,老方法，不建议使用，建议使用
	 * @param query
	 * @return
	 */
	int searchCount(String query);
	
	/**
	 * 查询用户，而且，只查询umt用户
	 * */
	/**
	 * 批量创建用户
	 * @param users
	 */
	void create(List<User> users);
	/**
	 * 获取一个用户的密保邮箱,已激活的
	 * @param uid
	 * @return securityEmail
	 * */
	String getSecurityEmail(int uid);
	/**
	 * 获取一个用户的密保邮箱,未激活的
	 * @param uid
	 * @return securityEmail
	 * */
	String getTempSecurityEmail(int uid);
	
	/**
	 * 用户是否验证过主账号
	 * @param uid 用户id
	 * @param email 主账号登陆名
	 **/
	boolean isActivePrimaryEmail(String email);
	
	/***
	 * 用户绑定第三方账户
	 * @param uid umtUid
	 * @param screenName 第三方用户名
	 * @param openId 第三方用户标识
	 * @param type 第三方类型
	 */
	void bindThirdParty(BindInfo bindInfo);
	
	/**
	 * 获取用户的第三方信息
	 * */
	List<BindInfo> getBindInfosByUid(int uid);
	/**
	 * 解除与第三方的绑定关系
	 * @param bindId 绑定id
	 * */
	void deleteBindById(int bindId);
	/**
	 * 解除与第三方的绑定关系
	 * @param bindId 绑定id
	 * */
	void deleteBindByUid(int uid);
	
	/**
	 * 获得最大的umtId
	 * */
	String getLastedUmtId();
	
	/**
	 * 当前账户名，是否被使用
	 * @param loginName
	 **/
	boolean isUsed(String loginName);
	
	/**
	 * 除了本人，其他的冗余信息全部删掉，因为有抢注存在，慎用
	 * */
	void removeUserExpectMe(String loginName,int uid);
	/**
	 * 同步coreMail用户，如果存在则不管，如果不存在则
	 * 返回loginNameInfoId
	 * */
	User upgradeCoreMailUser(String loginName);
	
	
}
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
package cn.vlabs.umt.services.user.service;

import java.util.List;

import cn.vlabs.umt.services.user.bean.LoginNameInfo;


/**
 * @author lvly
 * @since 2013-3-4
 */
public interface IUserLoginNameService {
	public static final String BEAN_ID="userLoginNameService";
	/**
	 * 获取用户登录名信息
	 * @param uid
	 * @param type User.LOGINNAME_XXX
	 * */
	List<LoginNameInfo> getLoginNameInfo(int uid,String type);
	
	/**
	 * 获取用户的一个信息;
	 * @param uid
	 * @param loginName
	 * */
	LoginNameInfo getALoginNameInfo(int uid,String loginName);
	/**
	 * 获取用户登录名信息
	 * @param loginNameInfoId umt_login_name 产生的id
	 * */
	LoginNameInfo getLoginNameInfoById(int loginNameInfoId);

	/**
	 * 更新登陆名
	 * @param uid 
	 * @param oldLoginName 旧的登录名
	 * @param newLoginName 新的登录名
	 */
	void updateLoginName(int uid,String oldLoginName,String newLoginName);
	
	/**单纯的增加一条记录
	 * <br>请根据业务逻辑进行判断，是否应该插入
	 * @param loginName 登录名，可以是邮箱，也可以是手机号
	 * @param uid 用户id
	 * @param type 类型，说明是手机，主账户，还是辅助账户
	 * @param status 登陆名状态，激活或者，未激活
	 * @return generateId
	 */
	int createLoginName(String loginName,int uid,String type,String status);
	
	/**
	 * 获得一个用户的所有有效辅助邮箱
	 * @param uid
	 * @return str 以分号分隔，请直接掺入到user表里的secondary_emails
	 * */
	String getValidSecondaryEmailStr(int uid);
	
	/**
	 * 删除一个用户的loginName，不可恢复，谨慎使用
	 * @param uid 用户id
	 * @param email 根据登录名删除
	 * */
	void removeSecondaryEmail(int uid,String email);
	/**
	 * 设置已激活，但是提出更改请求的账户
	 * @param uid 用户id
	 * @param oldLoginName 旧用户登录名
	 * @param newLoginName 新用户登录名，显示用
	 * */
	void updateToLoginName(int uid,String oldLoginName,String newLoginName);
	
	/**
	 * 激活账户名
	 * @param uid 用户id
	 * @param loginName 账户名
	 * @param type 类型
	 * */
	void toActive(int uid,String loginName,String type);
	
	/**
	 * 激活账户名
	 * @param loginNameinfoId 主键
	 * */
	void toActive(int loginInfoId);

	/**
	 * 删除
	 * @param loginNameInfoId
	 */
	void removeLoginNameById(int loginNameInfoId);
	
	/**
	 * 根据登陆名取出登录名id
	 * @param uid
	 * @param loginName
	 * @param type
	 * */
	int getLoginNameId(int uid,String loginName,String type);
	/**
	 * @param uid
	 * @param loginName
	 * @return
	 */
	boolean isUsedByMe(int uid, String loginName);
	
	/**
	 * 删除一个用户的所有登陆名信息
	 * */
	void removeLoginNamesByUid(int uid);

}
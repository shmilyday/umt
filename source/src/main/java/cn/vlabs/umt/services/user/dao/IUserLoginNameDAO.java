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
package cn.vlabs.umt.services.user.dao;

import java.util.List;

import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;

/**
 * 用户登录名相关持久层
 * @author lvly
 * @since 2013-2-27
 */
public interface IUserLoginNameDAO {
	
	/**单纯的增加一条记录
	 * <br>请根据业务逻辑进行判断，是否应该插入
	 * @param loginName 登录名，可以是邮箱，也可以是手机号
	 * @param uid 用户id
	 * @param type 类型，说明是手机，主账户，还是辅助账户
	 * @param status 状态，已激活，未激活等 参见LoginNameInfo.STATUS_XXX
	 * @return generateId
	 */
	int createLoginName(String loginName,int uid,String type,String status);
	
	/**
	 * 删除登录账户，主账户不可删，只能删除辅助账户，或者手机
	 * @param loginNameId 数据库主键
	 * @return none
	 **/
	void removeLoginName(int loginNameId);
	/**
	 * 删除登录账号，删除辅助账号
	 * @param uid
	 * @param email
	 */
	void removeSecondaryLoginName(int uid, String email);
	
	/**
	 * 更新登录值
	 * @param uid 用户id
	 * @param oldLoginName 旧账户
	 * @param newLoginName 新登录名
	 * @return boolean  是否成功更新
	 **/ 
	boolean updateLoginName(int uid,String oldLoginName,String newloginName);
	
	/**
	 * 根据登陆名获取用户信息
	 * @param loginName 登陆名
	 * @return 用户
	 * */
	User getUserByLoginName(String loginName);
	
	/**
	 * 查询该登录名是否被使用
	 * @param loginName
	 * */
	boolean isUsed(String loginName);
	/**
	 * 批量查询登陆名是否已被使用
	 * @param loginName 登录名
	 * @return 返回已被使用的集合
	 * */
	String[] isUsed(String[] loginName);
	
	/**
	 * 获得一个用户的一类的所有登录名
	 * @param uid
	 * @param type User.LOGINNAME_XXX
	 * @param status LoginNameInfo.STATUS_XXX
	 * @return
	 */
	List<String> getLoginNameInfos(int uid,String type,String status);

	/**
	 * 设置已激活，但是提出更改请求的账户
	 * @param uid 用户id
	 * @param oldLoginName 旧用户登录名
	 * @param newLoginName 新用户登录名，显示用
	 * */
	void updateToLoginName(int uid,String oldLoginName,String newLoginName);

	/**
	 * 获取用户登录名信息
	 * @param uid
	 * @param type User.LOGINNAME_XXX
	 * */
	List<LoginNameInfo> getLoginNameInfo(int uid,String type);

	/**
	 * 获取用户登录名信息
	 * @param loginNameInfoId umt_login_name 产生的id
	 * */
	LoginNameInfo getLoginNameInfoById(int loginNameInfoId);
	
	/**
	 * 激活账户名
	 * @param uid 用户id
	 * @param loginName 账户名
	 * @param type 类型
	 * */
	void toActive(int uid,String loginName,String type);
	
	/**
	 * 删除已有，未激活的邮箱
	 * @param email
	 * 
	 **/
	void removeSameTmpEmail(String email);
	
	/**
	 * 激活账户名
	 * @param loginNameinfoId 主键
	 * */
	void toActive(int loginInfoId);

	/**
	 * @param uid
	 * @param loginName
	 * @return
	 */
	LoginNameInfo getALoginNameInfo(int uid, String loginName);

	/**
	 * @param uid
	 * @param loginName
	 * @param type
	 * @return
	 */
	int getLoginNameId(int uid, String loginName, String type);

	/**
	 * @param uid
	 * @param loginName
	 * @return
	 */
	boolean isUsedByMe(int uid, String loginName);

	/**
	 * @param uid
	 */
	void removeLoginNamesByUid(int[] uid);

}
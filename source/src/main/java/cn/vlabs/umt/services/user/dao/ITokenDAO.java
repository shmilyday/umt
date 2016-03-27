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

import java.util.List;

import cn.vlabs.umt.services.user.bean.Token;

/**
 * 这个类负责操作PasswordChangeToken表
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 14, 2009 8:03:32 PM
 */
public interface ITokenDAO {
	/**
	 * 登记ChangeToken
	 * @param random 随机字符串
	 * @return 新创建的Token id
	 */
	int createToken(Token token);
	/**
	 * 删除一个Token
	 * @param tokenid token的ID
	 */
	void removeToken(int tokenid);
	/**
	 * 查询一个Token
	 * @param tokenid	Token的ID
	 * @param type 那种类型的token
	 * @return	Token对象
	 */
	Token getToken(int tokenid);
	
	/**
	 * 新产生一个token，之前，把以前的token置为无效
	 * @param uid
	 * @param  type
	 * */
	void removeTokenUnused(int uid,int type);
	
	/**
	 * 判断一个token 是否有效
	 * @param tokenId
	 * @param random
	 * @param type
	 * */
	boolean isValid(int tokenId,String random,int type);
	/**
	 * 设置token为已使用
	 * @param tokenId
	 * @return 
	 */
	boolean toUsed(int tokenId);
	/**
	 * 取一条用户最新的，某个type的，用过的token（用来取得已激活的密保邮箱等）
	 * @param uid
	 * @param operation
	 * @param status
	 * */
	Token getATokenByUidAndOperation(int uid, int operation,int status);
	/**
	 * @param uid
	 * @param type
	 * @param status
	 * @return
	 */
	List<Token> getTokenByUidAndOperation(int uid, int type, int status);
}
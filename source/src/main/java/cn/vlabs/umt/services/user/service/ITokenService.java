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

import java.util.Date;
import java.util.List;

import cn.vlabs.umt.services.user.bean.Token;


/**
 * 管理token服务类
 * @author lvly
 * @since 2013-1-31
 */
/**
 * @author lvly
 * @since 2013-1-31
 */
public interface ITokenService {
	static final String BEAN_ID="tokenService";
	/**
	 * 创建token,默认失效时间为24小时后，而且新建token以后，以前的token会失效
	 * @param uid 用户id
	 * @param operation 详见Token.TYPE_XXX
	 * @return token
	 * */
	Token createToken(int uid,int operation);
	/**
	 * 创建token，默认失效时间为24小时后，而且新建token以后，以前的token会失效
	 * @param uid 用户id
	 * @param operation 详见Token.TYPE_XXX
	 * @param content 详见Token.var 
	 * @return token
	 * */
	Token createToken(int uid,int operation,String content);
	/**
	 * 创建token，而且新建token以后，以前的token会失效
	 * @param uid 用户id
	 * @param operation 详见Token.TYPE_XXX
	 * @param content 详见Token.var 
	 * @return tokenId
	 * */
	Token createToken(int uid,int operation,String content,Date invalidTime);
	
	/**
	 * 创建token，而且新建token以后，以前的token会失效
	 * @param uid	用户ID
	 * @param operation	类型 详见 Token.TYPE_XX
	 * @param content	约定变量，可以为空
	 * @param invalidTime	失效时间，可以为空
	 * @param status token 状态，详细见Token.STATUS_XX
	 * @return
	 */
	Token createToken(int uid,int operation,String content,Date invalidTime,int status);
	
	int createToken(Token token);
	
	/**
	 * 把token置为已使用
	 * @param tokenId tokenId
	 * @return flag 设置成功了吗
	 * */
	boolean toUsed(int tokenId);
	
	/**
	 * 查一查当前token是否有效
	 * @param tokenId
	 * @param random
	 * @param operation
	 * */
	boolean isValid(int tokenId,String random,int operation);
	
	/**
	 * 取一条用户最新的，某个type的，用过的token（用来取得已激活的密保邮箱等）
	 * @param uid
	 * @param operation
	 * @param status
	 * */
	Token getATokenByUidAndOperation(int uid,int operation,int status);
	
	/**
	 * 取多个Token， 应用场景：比如取得所有未激活的辅助邮箱
	 * @param uid
	 * @param operation
	 * @param status
	 * @return
	 */
	List<Token> getTokenByUidAndOperation(int uid,int operation,int status);
	/**
	 * 根据tokenId获取Token
	 * @param tokenId
	 * @return token
	 * */
	Token getTokenById(int tokenId);
	
	/**
	 * 如果发出一个token的时候，想删除以前所有同类型的token，请调用次方法
	 * @param uid
	 * @param operation
	 */
	void removeTokensUnsed(int uid,int operation);
	
	/**
	 * 删除一个token，这跟used有本质区别
	 * @param tokenId
	 * */
	void removeTokenById(int tokenId);
	/**
	 * 获取一个token 同getAtokenByUidAndOperation,但是会顾虑过期时间，如果过期，则返回null
	 * @param userId
	 * @param operation
	 * @param status
	 * @return
	 */
	Token getATokenByUidAndOperationWithExpire(int userId, int operation, int statusUsed);
}
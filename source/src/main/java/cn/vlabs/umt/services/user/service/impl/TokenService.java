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
package cn.vlabs.umt.services.user.service.impl;

import java.util.Date;
import java.util.List;

import cn.vlabs.umt.common.util.RandomUtil;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.dao.ITokenDAO;
import cn.vlabs.umt.services.user.service.ITokenService;

/**
 * @author lvly
 * @since 2013-1-31
 */
public class TokenService implements ITokenService{
	
	public static final long HOURS_24=24*60*60*1000;
	private ITokenDAO tokenDAO;
	
	public ITokenDAO getTokenDAO() {
		return tokenDAO;
	}
	public void setTokenDAO(ITokenDAO tokenDAO) {
		this.tokenDAO = tokenDAO;
	}
	@Override
	public Token createToken(int uid, int type) {
		return createToken(uid,type,null);
	}
	@Override
	public Token createToken(int uid, int type, String var) {
		return createToken(uid,type,var,null);
	}
	@Override
	public void removeTokensUnsed(int uid, int operation) {
		tokenDAO.removeTokenUnused(uid,operation);
		
	}
	@Override
	public int createToken(Token token) {
		token.setId(tokenDAO.createToken(token));
		return token.getId();
	}
	@Override
	public Token createToken(int uid, int type, String var, Date invalidTime, int status) {
		Token token=new Token();
		token.setCreateTime(new Date());
		if(invalidTime==null){
			token.setExpireTime(new Date(System.currentTimeMillis()+HOURS_24));
		}else{
			token.setExpireTime(invalidTime);
		}
		RandomUtil ru = new RandomUtil();
		token.setRandom(ru.getRandom(20));
		if(status>0){
			token.setStatus(status);
		}else{
			token.setStatus(Token.STATUS_UNUSED);
		}
		token.setOperation(type);
		token.setUid(uid);
		token.setContent(var);
		token.setId(tokenDAO.createToken(token));
		return token;
	}
	@Override
	public Token createToken(int uid, int type, String var, Date invalidTime) {
		return createToken(uid, type, var, invalidTime, 0);
	}
	@Override
	public boolean toUsed(int tokenId) {
		return tokenDAO.toUsed(tokenId);
	}
	@Override
	public boolean isValid(int tokenId, String random,int type) {
		return tokenDAO.isValid(tokenId, random, type);
	}
	
	@Override
	public List<Token> getTokenByUidAndOperation(int uid, int type,int status) {
		return tokenDAO.getTokenByUidAndOperation(uid,type,status);
	}
	@Override
	public Token getATokenByUidAndOperation(int uid, int type, int status) {
		return tokenDAO.getATokenByUidAndOperation(uid,type,status);
	}
	@Override
	public Token getTokenById(int tokenId) {
		return tokenDAO.getToken(tokenId);
	}
	@Override
	public void removeTokenById(int tokenId) {
		tokenDAO.removeToken(tokenId);
	}
	@Override
	public Token getATokenByUidAndOperationWithExpire(int uid, int operation, int status) {
		Token token=getATokenByUidAndOperation(uid, operation, status);
		if(token!=null&&token.getExpireTime()!=null&&!token.getExpireTime().before(new Date())){
			return token;
		}
		return null;
	}
}
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

import net.duckling.falcon.api.cache.ICacheService;

import cn.vlabs.umt.services.user.bean.OauthToken;
import cn.vlabs.umt.services.user.dao.IOauthTokenDAO;
import cn.vlabs.umt.services.user.service.IOauthTokenService;

public class OauthTokenService implements IOauthTokenService {
	private ICacheService cacheService;
	private IOauthTokenDAO oauthTokenDAO;
	public void setCacheService(ICacheService cacheService) {
		this.cacheService = cacheService;
	}

	public void setOauthTokenDAO(IOauthTokenDAO oauthTokenDAO) {
		this.oauthTokenDAO = oauthTokenDAO;
	}
	
	@Override
	public OauthToken getTokenByAccess(String accessToken) {
		Object o = cacheService.get(getAccessTokenKey(accessToken));
		if(o==null){
			OauthToken auth = oauthTokenDAO.getTokenByAccess(accessToken);
			if(auth!=null){
				addTokenToCache(auth);
			}
			return auth;
		}else{
			return (OauthToken)o;
		}
	}
	private void addTokenToCache(OauthToken token){
		cacheService.set(getAccessTokenKey(token), token);
		cacheService.set(getRefreshTokenKey(token), token);
	}
	
	private void removeTokenFromCache(OauthToken token){
		cacheService.remove(getAccessTokenKey(token));
		cacheService.remove(getRefreshTokenKey(token));
	}
	@Override
	public OauthToken getTokenByRefresh(String refreshToken) {
		Object o = cacheService.get(getRefreshTokenKey(refreshToken));
		if(o==null){
			OauthToken auth = oauthTokenDAO.getTokenByRefresh(refreshToken);
			if(auth!=null){
				addTokenToCache(auth);
			}
			return auth;
		}else{
			return (OauthToken)o;
		}
	}

	@Override
	public int save(OauthToken token) {
		oauthTokenDAO.save(token);
		addTokenToCache(token);
		return token.getId();
	}

	@Override
	public void update(OauthToken token) {
		OauthToken oldToken = oauthTokenDAO.getTokenById(token.getId());
		if(oldToken!=null){
			removeTokenFromCache(oldToken);
			oauthTokenDAO.update(token);
			addTokenToCache(token);
		}
	}

	@Override
	public void delete(OauthToken token) {
		removeTokenFromCache(token);
		oauthTokenDAO.delete(token);
	}

	@Override
	public void delete(int tokenId) {
		OauthToken token = oauthTokenDAO.getTokenById(tokenId);
		if(token!=null){
			delete(token);
		}
	}

	@Override
	public void deleteBeforeAccessToken(Date accessExpired) {
		oauthTokenDAO.deleteBeforeAccessToken(accessExpired);
	}

	@Override
	public void deleteBeforeRefreshToken(Date refreshExpired) {
		oauthTokenDAO.deleteBeforeRefreshToken(refreshExpired);

	}
	private String getAccessTokenKey(OauthToken token){
		return getAccessTokenKey(token.getAccessToken());
	}
	private String getAccessTokenKey(String token){
		return token+"-accessToken";
	}
	private String getRefreshTokenKey(OauthToken token){
		return getRefreshTokenKey(token.getRefreshToken());
	}
	private String getRefreshTokenKey(String token){
		return token+"-refreshToken";
	}

	@Override
	public boolean isAccessExpired(OauthToken token) {
		Date access = token.getAccessExpired();
		return System.currentTimeMillis()>access.getTime();
	}

	@Override
	public boolean isRefreshExpired(OauthToken token) {
		Date access = token.getRefreshExpired();
		return System.currentTimeMillis()>access.getTime();
	}
}
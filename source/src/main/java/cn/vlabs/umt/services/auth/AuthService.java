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
package cn.vlabs.umt.services.auth;

import java.util.List;


public class AuthService implements IAuthService {
	private AuthDAO authDao;
	private String baseUrl;
	public void setAuthDao(AuthDAO authDao){
		this.authDao = authDao;
	}
	public void setBaseUrl(String baseUrl){
		this.baseUrl = baseUrl;
	}
	@Override
	public ThirdPartyAuth find(String siteName) {
		if (siteName!=null){
			ThirdPartyAuth auth =authDao.findByCode(siteName);
			if (auth!=null){
				auth.setBaseUrl(baseUrl);
			}
			return auth;
		}else{
			return null;
		}
	}

	@Override
	public boolean existAuth(String siteName) {
		return authDao.findByCode(siteName)!=null;
	}

	@Override
	public List<ThirdPartyAuth> getAll() {
		List<ThirdPartyAuth> auths= authDao.getAll();
		for(ThirdPartyAuth auth:auths){
			auth.setBaseUrl(baseUrl);
		}
		return auths;
	}
	@Override
	public void create(ThirdPartyAuth auth) {
		authDao.create(auth);
	}
	@Override
	public void update(ThirdPartyAuth auth) {
		authDao.update(auth);
	}
	@Override
	public void remove(String code) {
		authDao.remove(code);
	}
	@Override
	public List<ThirdPartyAuth> findShowInLogin() {
		return authDao.findAllShowInLogin();
	}

}

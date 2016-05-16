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
package cn.vlabs.umt.services.account;

import java.util.List;

import cn.vlabs.duckling.api.umt.rmi.userv7.SearchField;
import cn.vlabs.umt.services.user.bean.CoreMailUserInfo;
import cn.vlabs.umt.services.user.bean.User;

/**
 * @author lvly
 * @since 2013-10-14
 */
public class CoreMailUnuseableClient extends ICoreMailClient {

	@Override
	public boolean isUserExt(String userName) {
		return false;
	}

	@Override
	public boolean changePassword(String userName, String newPassword) {
		return true;
	}

	@Override
	public CoreMailAuthenticateResult authenticate(String userName, String passWord) {
		return new CoreMailAuthenticateResult(false, false);
	}

	@Override
	public CoreMailUserInfo getCoreMailUserInfo(String userName) {
		return null;
	}

	@Override
	public int getSize(String domain, String keyword, SearchField field) {
		return 0;
	}

	@Override
	public List<User> searchByKeyword(String keyword, String domain, SearchField field, int offset, int size) {
		return null;
	}

	@Override
	public boolean domainExist(String domainName) {
		return false;
	}

	@Override
	public boolean createUser(String username, String trueName, String password) {
		return true;
	}

}
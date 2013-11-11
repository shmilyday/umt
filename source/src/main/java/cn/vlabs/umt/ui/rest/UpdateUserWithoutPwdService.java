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
package cn.vlabs.umt.ui.rest;

import cn.vlabs.duckling.api.umt.rmi.user.UMTUser;
import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceException;

/**
 * update user，不更新密码
 * @author y
 *
 */
public class UpdateUserWithoutPwdService extends SecuredAction {
	@Override
	protected Object service(RestSession session, Object arg) throws ServiceException {
		UMTUser user = (UMTUser)arg;
		RestUserServiceImpl service=new RestUserServiceImpl();
		service.updateUserWithoutPwd(user);
		return null;
	}
}
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
package cn.vlabs.umt.ui.rest;

import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceException;

/**
 * 获取UMT的用户对象，去除password信息
 * 
 * @author y
 *
 */
public class GetUMTUserService extends SecuredAction {
	@Override
	protected Object service(RestSession session, Object arg) throws ServiceException {
		RestUserServiceImpl service=new RestUserServiceImpl();
		String usernames=(String)arg;
		return service.getUMTUser(usernames);
	}

}
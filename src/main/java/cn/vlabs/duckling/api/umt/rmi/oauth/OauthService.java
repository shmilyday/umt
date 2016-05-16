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
package cn.vlabs.duckling.api.umt.rmi.oauth;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import cn.vlabs.duckling.api.umt.rmi.exception.AccessForbidden;
import cn.vlabs.duckling.api.umt.rmi.exception.ErrorCode;
import cn.vlabs.rest.ServiceClient;
import cn.vlabs.rest.ServiceContext;
import cn.vlabs.rest.ServiceException;

/**
 * Oauth接口,目前只有查询<br>
 * 
 * @author lvlongyun
 * @since 2014-04-16
 */
public class OauthService {

	private static final Logger LOG = Logger.getLogger(OauthService.class);

	private ServiceContext context;
	private ServiceClient client;

	public OauthService(String baseUrl) {
		context = new ServiceContext(baseUrl);
		context.setKeepAlive(true);
		client = new ServiceClient(context);
	}

	@SuppressWarnings("unchecked")
	public List<OauthClient> searchByKeyword(String key, int offset, int size) {
		try {
			return (List<OauthClient>) sendService("Oauth.searchByKey",
					new Object[] { key, offset, size });
		} catch (ServiceException e) {
			LOG.error(e.getMessage(), e);
			return new ArrayList<OauthClient>();
		}
	}

	private Object sendService(String service, Object[] value)
			throws ServiceException {
		try {
			return client.sendService(service, value);
		} catch (ServiceException e) {
			switch (e.getCode()) {
			case ErrorCode.ACCESS_FORBIDDEN:
				throw new AccessForbidden();
			default:
				throw e;
			}
		} finally {
			context.close();
		}
	}
}

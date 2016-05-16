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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.duckling.api.umt.rmi.oauth.OauthClient;
import cn.vlabs.rest.server.annotation.RestMethod;
import cn.vlabs.umt.services.user.bean.OauthClientBean;
import cn.vlabs.umt.services.user.service.IOauthClientService;
import cn.vlabs.umt.ui.UMTContext;

public class RestOauthServiceImpl {
	private IOauthClientService oauthClientService;
	
	public RestOauthServiceImpl(){
		BeanFactory factory = UMTContext.getFactory();
		oauthClientService = (IOauthClientService)factory.getBean(IOauthClientService.BEAN_ID);
	}
	
	@RestMethod("searchByKey")
	public List<OauthClient> searchByKey(String key,int offset,int size){
		List<OauthClientBean> beans= oauthClientService.searchClientByKey(key,offset,size);
		return convert2OauthClient(beans);
	}
	private List<OauthClient> convert2OauthClient(List<OauthClientBean> beans){
		List<OauthClient> client=new ArrayList<OauthClient>();
		if(beans==null){
			return client;
		}
		String prefix="/logo?logoId=";
		for(OauthClientBean bean:beans){
			OauthClient c=new OauthClient();
			c.setClientId(bean.getClientId());
			c.setClientName(bean.getClientName());
			c.setClientUrl(bean.getClientWebsite());
			c.setLogo100Url(generateLogoUrl(prefix,bean.getLogo100m100()));
			c.setLogo64Url(generateLogoUrl(prefix,bean.getLogo64m64()));
			c.setLogo32Url(generateLogoUrl(prefix,bean.getLogo32m32()));
			c.setLogo16Url(generateLogoUrl(prefix,bean.getLogo16m16()));
			c.setLogoCustom(bean.getLogoCustom());
			client.add(c);
		}
		return client;
	}
	private String generateLogoUrl(String prefix,int clbId){
		if(clbId==0){
			return null;
		}
		return prefix+clbId;
	}

}

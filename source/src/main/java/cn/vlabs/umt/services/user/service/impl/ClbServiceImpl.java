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
/**
 * 
 */
package cn.vlabs.umt.services.user.service.impl;

import java.io.InputStream;

import org.apache.log4j.Logger;

import cn.vlabs.clb.api.CLBConnection;
import cn.vlabs.clb.api.CLBPasswdInfo;
import cn.vlabs.clb.api.CLBServiceFactory;
import cn.vlabs.clb.api.document.CreateInfo;
import cn.vlabs.clb.api.document.DocumentService;
import cn.vlabs.clb.api.document.UpdateInfo;
import cn.vlabs.clb.api.image.IResizeImageService;
import cn.vlabs.rest.IFileSaver;
import cn.vlabs.rest.stream.StreamInfo;
import cn.vlabs.umt.common.CLBResizeparamFactory;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.services.user.service.IClbService;

/**
 * @author lvly
 * @since 2013-6-8
 */
public class ClbServiceImpl implements IClbService{
	private static final Logger LOG=Logger.getLogger(ClbServiceImpl.class);
	private Config config;
	public ClbServiceImpl(Config config){
		this.config=config;
	}
	public CLBConnection getConnection(){
		CLBPasswdInfo pwd=new CLBPasswdInfo();
		pwd.setUsername(config.getStringProp("clb.username", "passportun"));
		pwd.setPassword(config.getStringProp("clb.passwd","passportpw"));
		return new CLBConnection(config.getStringProp("clb.base.url", ""),pwd);
	}
	
	
	public DocumentService getDocService(){
		return CLBServiceFactory.getDocumentService(getConnection());
	}
	
	@Override
	public int upload(InputStream ins, String fileName) {
		try {
			StreamInfo stream=new StreamInfo();
			stream.setFilename(fileName);
			stream.setLength(ins.available());
			stream.setInputStream(ins);
			CreateInfo info=new CreateInfo();
			info.setTitle(fileName);
			info.setIsPub(1);
			UpdateInfo updateInfo=getDocService().createDocument(info,stream);
			getResizeService().resize(updateInfo.getDocid(),updateInfo.getVersion(),CLBResizeparamFactory.getCommonResizeParam());
			return updateInfo.getDocid();
		} catch (Exception e){
			LOG.error(e.getMessage(),e);
		}
		return -1;
	}
	public void downloadBySize(int imgId,IFileSaver saver,String type){
		getResizeService().getContent(imgId, LASTEST, type,saver);
	}
	public IResizeImageService getResizeService(){
		return CLBServiceFactory.getResizeImageService(getConnection());
	}
	@Override
	public void download(int imgId,IFileSaver saver){
		getDocService().getContent(imgId, saver);
	}

}

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

import java.io.InputStream;

import cn.vlabs.rest.IFileSaver;

public interface IClbService {
	String BEAN_ID="clbService";
	String LASTEST="latest";
	String LOGO_SMALL="small";
	String LOGO_MIDDLE="middle";
	String LOGO_LARGE="large";
	/**
	 * 往clb上传文件
	 * @param ins 文件流
	 * @param fileName 文件名
	 * @return docId
	 */
	int upload(InputStream ins,String fileName);
	/**
	 * 下载文件
	 * @param imgId
	 * @param saver
	 * @return
	 */
	void download(int imgId,IFileSaver saver);
	
	void downloadBySize(int imgId,IFileSaver saver,String size);

}

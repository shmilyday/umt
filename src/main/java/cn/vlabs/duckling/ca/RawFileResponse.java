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
package cn.vlabs.duckling.ca;

import java.io.IOException;
import java.io.InputStream;

/**
 * 通用的文件类型的返回
 * @author xiejj@cstnet.cn
 *
 */
public interface RawFileResponse {
	/**
	 * 获取下载的文件名
	 * @return 返回文件名
	 */
	String getFileName();
	/**
	 * 获取文件大小
	 * @return 如果服务器设置了文件长度，则返回对应的长度值，否则返回-1
	 */
	long getFileSize();
	/**
	 * 获取下载文件的输入流。注意调用者负责关闭这个输入流
	 * @return 文件流
	 * @throws IOException
	 */
	InputStream getInputStream() throws IOException;
	/**
	 * 关闭网络连接
	 */
	void close();
}

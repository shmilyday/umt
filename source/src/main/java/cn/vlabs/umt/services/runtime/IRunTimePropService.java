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
package cn.vlabs.umt.services.runtime;

import cn.vlabs.umt.services.runtime.bean.RunTimeProp;

/**
 * 执行时生成的prop文件的，读取
 * @author lvly
 * @since 2013-3-8
 */
public interface IRunTimePropService {
	public static final String BEAN_ID="runTimePropService";
	/**
	 * 获取配置项
	 * */
	RunTimeProp getValueByName(String propName);
	/**
	 * 增加配置项
	 * */
	void createProp(String propName,String value);
	/**
	 * 更改配置项
	 * */
	void updateProp(String propName,String toValue);
	/**
	 * 删除配置项
	 * */
	void deleteProp(String propName);
}
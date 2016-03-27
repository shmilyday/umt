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
package cn.vlabs.umt.validate.domain;

/**
 * 验证结果
 * @author lvly
 * @since 2013-1-21
 */
public class ErrorMsg {
	/**
	 * Constructor
	 * @param property 表单input 名
	 * @param key 国际化的key
	 * */
	public ErrorMsg(String property,String key){
		this.property=property;
		this.key=key;
	}
	/**
	 * 字段名
	 * */
	private String property;
	/**
	 * 错误对应的，国际化key
	 * */
	private String key;
	
	public String getProperty() {
		return property;
	}
	public void setProperty(String property) {
		this.property = property;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
}
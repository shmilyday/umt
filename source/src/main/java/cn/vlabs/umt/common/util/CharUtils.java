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
package cn.vlabs.umt.common.util;

import cn.vlabs.umt.validate.validator.ValidatorFactory;

/**
 * @author lvly
 * @since 2013-1-30
 */
public final class CharUtils {
	private CharUtils(){}
	/**
	 * 把邮箱的一部分隐藏掉，规则为前半部分，除了第一个和最后一个字母，其他隐藏
	 * 比如zhangsan@163.com 会变成 z******n@163.com
	 * @param email email验证
	 * */
	public static String hideEmail(String email){
		if(CommonUtils.isNull(email)){
			return "";
		}
		if(ValidatorFactory.getEmailRegixValidator().validate(email)){
			return email.substring(0,1)+"******"+email.substring(email.indexOf("@")-1);
		}else {
			return "";
		}
		
	}

}
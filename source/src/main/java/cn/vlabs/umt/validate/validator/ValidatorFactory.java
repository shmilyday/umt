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
package cn.vlabs.umt.validate.validator;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.validate.validator.impl.EmailRegixValidator;
import cn.vlabs.umt.validate.validator.impl.MaxLengthValidator;
import cn.vlabs.umt.validate.validator.impl.MinLengthValidator;
import cn.vlabs.umt.validate.validator.impl.PasswordEqualsValidator;
import cn.vlabs.umt.validate.validator.impl.RegixValidator;
import cn.vlabs.umt.validate.validator.impl.RequiredValidator;
import cn.vlabs.umt.validate.validator.impl.UsernameExistsValidator;
import cn.vlabs.umt.validate.validator.impl.ValidCodeEqualsValidator;

/**
 * 获取所有验证器的工厂类，定义着各个构造验证器的参数
 * @author lvly
 * @since 2013-1-21
 */
public final class ValidatorFactory {
	private ValidatorFactory(){}
	/**
	 * 获得非空验证器
	 * */
	public static Validator getRequiredValidator(){
		return new RequiredValidator();
	}
	/**
	 * 获得email格式验证验证器
	 * */
	public static Validator getEmailRegixValidator(){
		return new EmailRegixValidator();
	}
	/**
	 * 获得最小长度验证器
	 * @param length 最小长度
	 * 
	 * */
	public static Validator getMinLengthValidator(int length){
		return new MinLengthValidator(length);
	}
	/**
	 * 获得最大长度验证器
	 * @param length 最大长度
	 * */
	public static Validator getMaxLengthValidator(int length){
		return new MaxLengthValidator(length);
	}
	/**
	 * 验证用户是否存在
	 * @param beanFactory，主要获取用户相关服务类
	 * */
	public static Validator getUserExistValidator(BeanFactory beanFactory){
		return new UsernameExistsValidator(beanFactory);
	}
	/**
	 * 验证密码是否相同
	 * @param password，第一次输入的密码
	 * */
	public static Validator getPasswordEqualsValidator(String password){
		return new PasswordEqualsValidator(password);
	}
	/**
	 * 验证码是否正确
	 * @param request 主要获取session里的验证码
	 * */
	public static Validator getValidCodeEqualsValidator(HttpServletRequest request){
		return new ValidCodeEqualsValidator(request);
	}
	/**
	 * 验证是否匹配所给出的正则表达式
	 * @param regix 需要匹配的正则表达式
	 * */
	public static Validator getRegixValidator(String regix){
		return new RegixValidator(regix);
	}

}
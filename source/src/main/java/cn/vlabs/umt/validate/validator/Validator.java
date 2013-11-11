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
package cn.vlabs.umt.validate.validator;


/**
 * 后端验证接口,所有验证接口都要实现改接口
 * @author lvly
 * @since 2013-1-21
 */
public interface Validator {
	/**
	 * 需扩展的方法，验证方法，value值是待验证的字段，不适合用于联合验证
	 * */
	boolean validate(String value);

}
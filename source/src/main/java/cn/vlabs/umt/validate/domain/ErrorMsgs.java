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
package cn.vlabs.umt.validate.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * 一次验证表单的，所有错误集合
 * @author lvly
 * @since 2013-1-21
 */
public class ErrorMsgs {
	/**
	 * 一次验证表单的，所有错误集合
	 * */
	private List<ErrorMsg> errors=new ArrayList<ErrorMsg>();
	
	public List<ErrorMsg> getMsgs(){
		return errors;
	}
	/**
	 * 验证结果是否通过
	 * @return 通过了吗？
	 * 
	 * */
	public boolean isPass(){
		return errors.isEmpty();
	}
	/**
	 * 错误结果加入到队列中
	 * @param msg 单个的错误消息
	 * */
	public void addMsg(ErrorMsg msg){
		errors.add(msg);
	}

}
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
package cn.vlabs.duckling.api.umt.rmi.exception;

import cn.vlabs.rest.ServiceException;

public class APIRuntimeException extends RuntimeException {
	public  APIRuntimeException(ServiceException e){
		this.code=e.getCode();
		this.message=e.getMessage();
	}
	public int getCode(){
		return code;
	}
	
	public String getMessage(){
		return message;
	}
	private int code;
	private String message;
	private static final long serialVersionUID = 1L;
}
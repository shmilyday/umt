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
package cn.vlabs.umt.common.datasource;

public class CoreMailDBException extends Exception{
	private static final long serialVersionUID = -3149229654104166912L;
	private String why=WHY_DB_ERROR;
	public static final String WHY_DB_ERROR="DB_ERROR";
	public static final String WHY_PWD_ERROR="PWD_CAN_NOT_PARSE_ERROR";
	public CoreMailDBException(String why){
		super();
		this.why=why;
	}
	public CoreMailDBException(String why,Exception e){
		super(e);
		this.why=why;
	}
	public String getWhy() {
		return why;
	}
	public void setWhy(String why) {
		this.why = why;
	}
	
}

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
package cn.vlabs.umt.services.account;

/**
 * @author lvly
 * @since 2013-9-17
 */
public class CoreMailAuthenticateResult {
	private boolean isSuccess;
	private boolean isValidUserName;
	public CoreMailAuthenticateResult(boolean isSuccess,boolean isValidUserName){
		this.isSuccess=isSuccess;
		this.isValidUserName=isValidUserName;
	}
	public boolean isSuccess() {
		return isSuccess;
	}
	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}
	public boolean isValidUserName() {
		return isValidUserName;
	}
	public void setValidUserName(boolean isValidUserName) {
		this.isValidUserName = isValidUserName;
	}
	

}
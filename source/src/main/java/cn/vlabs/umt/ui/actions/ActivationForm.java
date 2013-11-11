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
package cn.vlabs.umt.ui.actions;

import org.apache.struts.action.ActionForm;

/**
 * @author lvly
 * @since 2013-1-28
 */
public class ActivationForm extends ActionForm {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4815644349876348449L;
	private int tokenid;
	private String random;
	private boolean changeLoginName;
	private int loginNameInfoId;
	
	public int getLoginNameInfoId() {
		return loginNameInfoId;
	}
	public void setLoginNameInfoId(int loginNameInfoId) {
		this.loginNameInfoId = loginNameInfoId;
	}
	public boolean isChangeLoginName() {
		return changeLoginName;
	}
	public void setChangeLoginName(boolean changeLoginName) {
		this.changeLoginName = changeLoginName;
	}
	public int getTokenid() {
		return tokenid;
	}
	public void setTokenid(int tokenid) {
		this.tokenid = tokenid;
	}
	public String getRandom() {
		return random;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	
}
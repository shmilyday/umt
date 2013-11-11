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

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.user.bean.User;

/** 
 * MyEclipse Struts
 * Creation date: 12-20-2009
 * 
 * XDoclet definition:
 * @struts.form name="manageUserForm"
 */
public class ManageUserForm extends ActionForm {
	/*
	 * Generated fields
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** password property */
	private String password;

	/** userid property */
	private String userid;

	/** total property */
	private String total;

	/** act property */
	private String act;

	/** truename property */
	private String truename;

	/** page property */
	private String page;

	/** username property */
	private String username;
	private String email;
	/** pageCount property */
	private int pageCount;

	/*
	 * Generated Methods
	 */

	public User getUser(){
		User u = new User();
		u.setCstnetId(email);
		if (email==null)
		{
			u.setCstnetId(username);
		}
		u.setPassword(password);
		u.setTrueName(truename);
		return u;
	}
	/** 
	 * Method validate
	 * @param mapping
	 * @param request
	 * @return ActionErrors
	 */
	public ActionErrors validate(ActionMapping mapping,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return null;
	}

	/** 
	 * Method reset
	 * @param mapping
	 * @param request
	 */
	public void reset(ActionMapping mapping, HttpServletRequest request) {
		// TODO Auto-generated method stub
	}

	/** 
	 * Returns the password.
	 * @return String
	 */
	public String getPassword() {
		return password;
	}

	/** 
	 * Set the password.
	 * @param password The password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/** 
	 * Returns the userid.
	 * @return String
	 */
	public String getUserid() {
		return CommonUtils.trim(userid);
	}

	/** 
	 * Set the userid.
	 * @param userid The userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}

	/** 
	 * Returns the total.
	 * @return int
	 */
	public String getTotal() {
		return CommonUtils.trim(total);
	}

	/** 
	 * Set the total.
	 * @param total The total to set
	 */
	public void setTotal(String total) {
		this.total = total;
	}

	/** 
	 * Returns the act.
	 * @return String
	 */
	public String getAct() {
		return CommonUtils.trim(act);
	}

	/** 
	 * Set the act.
	 * @param act The act to set
	 */
	public void setAct(String act) {
		this.act = act;
	}

	/** 
	 * Returns the truename.
	 * @return String
	 */
	public String getTruename() {
		return CommonUtils.trim(truename);
	}

	/** 
	 * Set the truename.
	 * @param truename The truename to set
	 */
	public void setTruename(String truename) {
		this.truename = truename;
	}

	/** 
	 * Returns the page.
	 * @return int
	 */
	public String getPage() {
		return CommonUtils.trim(page);
	}

	/** 
	 * Set the page.
	 * @param page The page to set
	 */
	public void setPage(String page) {
		this.page = page;
	}

	/** 
	 * Returns the username.
	 * @return String
	 */
	public String getUsername() {
		return CommonUtils.trim(username);
	}

	/** 
	 * Set the username.
	 * @param username The username to set
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/** 
	 * Returns the pageCount.
	 * @return int
	 */
	public int getPageCount() {
		return pageCount;
	}

	/** 
	 * Set the pageCount.
	 * @param pageCount The pageCount to set
	 */
	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getEmail() {
		return CommonUtils.trim(email);
	}
}
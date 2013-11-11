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

/** 
 * MyEclipse Struts
 * Creation date: 12-21-2009
 * 
 * XDoclet definition:
 * @struts.form name="manageRequestsForm"
 */
public class ManageRequestsForm extends ActionForm {
	/*
	 * Generated fields
	 */

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/** total property */
	private String total;

	/** page property */
	private String page;

	/** rid property */
	private int rid;

	/*
	 * Generated Methods
	 */

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
	 * Returns the total.
	 * @return String
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
	 * Returns the page.
	 * @return String
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
	 * Returns the rid.
	 * @return int
	 */
	public int getRid() {
		return rid;
	}

	/** 
	 * Set the rid.
	 * @param rid The rid to set
	 */
	public void setRid(int rid) {
		this.rid = rid;
	}
}
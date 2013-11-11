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
import org.apache.struts.upload.FormFile;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.services.site.Application;

/** 
 * MyEclipse Struts
 * Creation date: 12-18-2009
 * 
 * XDoclet definition:
 * @struts.form name="manageApplicationForm"
 */
public class ManageApplicationForm extends ActionForm {
	private static final long serialVersionUID = 1L;

	/** serverType property */
	private String serverType;

	/** description property */
	private String description;

	/** url property */
	private String url;

	/** name property */
	private String name;

	/** appid property */
	private int appid;
	
	private String page;
	private String total;
	private FormFile keyfile;
	private boolean allowOperate;
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

	public Application getApplication(){
		Application app = new Application();
		app.setDescription(description);
		app.setName(name);
		app.setServerType(serverType);
		app.setUrl(url);
		app.setAllowOperate(allowOperate);
		app.setId(appid);
		return app;
	}
	/** 
	 * Returns the serverType.
	 * @return String
	 */
	public String getServerType() {
		return CommonUtils.trim(serverType);
	}

	/** 
	 * Set the serverType.
	 * @param serverType The serverType to set
	 */
	public void setServerType(String serverType) {
		this.serverType = serverType;
	}

	/** 
	 * Returns the description.
	 * @return String
	 */
	public String getDescription() {
		return CommonUtils.trim(description);
	}

	/** 
	 * Set the description.
	 * @param description The description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/** 
	 * Returns the url.
	 * @return String
	 */
	public String getUrl() {
		return CommonUtils.trim(url);
	}

	/** 
	 * Set the url.
	 * @param url The url to set
	 */
	public void setUrl(String url) {
		this.url = url;
	}

	/** 
	 * Returns the name.
	 * @return String
	 */
	public String getName() {
		return CommonUtils.trim(name);
	}

	/** 
	 * Set the name.
	 * @param name The name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/** 
	 * Returns the appid.
	 * @return String
	 */
	public int getAppid() {
		return appid;
	}

	/** 
	 * Set the appid.
	 * @param appid The appid to set
	 */
	public void setAppid(int appid) {
		this.appid = appid;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getPage() {
		return CommonUtils.trim(page);
	}

	public void setTotal(String total) {
		this.total = total;
	}

	public String getTotal() {
		return CommonUtils.trim(total);
	}

	public void setKeyfile(FormFile keyfile) {
		this.keyfile = keyfile;
	}

	public FormFile getKeyfile() {
		return keyfile;
	}

	public void setAllowOperate(boolean allowOperate) {
		this.allowOperate = allowOperate;
	}

	public boolean isAllowOperate() {
		return allowOperate;
	}
}
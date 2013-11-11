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
package cn.vlabs.umt.services.user.bean;

import java.io.Serializable;
import java.util.Date;

import org.json.simple.JSONObject;

/**
 * Token 可用于验证用户身份，比如在激活邮箱，修改密码等
 * @author xiejj@cnic.cn
 * 
 * @creation Dec 14, 2009 8:05:17 PM
 */
public class Token implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -4849876158508506312L;
	public void setRandom(String random) {
		this.random = random;
	}
	
	public String getRandom() {
		return random;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	/**
	 * 主键
	 * */
	private int id;
	/**
	 * 随机生成码
	 * */
	private String random;
	/**
	 * 创建时间
	 * */
	private Date createTime;
	/**
	 * Token作用域，详见TYPE_XXX
	 * */
	private int operation;
	/**
	 * umt外键，原先使用username，这里要换成umt内部的uid
	 * */
	private int uid;
	/**
	 * 失效时间如果不设置，默认时间为24小时
	 * */
	private Date expireTime;
	/**
	 * Token使用状态，详见 STATUS_XX
	 * */
	private	int status;
	/**
	 * 变量，根据type不同，可随意设置，string，json或者xml
	 * 按照约定用即可，或者不用
	 * */
	private String content;
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public int getOperation() {
		return operation;
	}
	public void setOperation(int type) {
		this.operation = type;
	}
	public Date getExpireTime() {
		return expireTime;
	}
	public void setExpireTime(Date invalidTime) {
		this.expireTime = invalidTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String var) {
		this.content = var;
	}
	public static final int OPERATION_CHANGE_PASSWORD=1;
	public static final int OPERATION_ACTIVATION_PRIMARY_EMAIL=2;
	public static final int OPERATION_ACTIVATION_SECURITY_EMAIL=3;
	public static final int OPERATION_MOBILE_PHONE = 4;
	public static final int OPERATION_ACTIVATION_SECONDARY_EMAIL=5;
	public static final int OPERATION_COMFIRM_PRIMARY_EMAIL=6;
	public static final int OPERATION_ACTIVATION_PRIMARY_AND_SECURITY=7;
	public static final int OPERATION_WEB_LOGIN=8;
	
	public static final int STATUS_UNUSED=11;
	public static final int STATUS_USED=12;
	public static final int STATUS_DELETED=14;
	
	
}
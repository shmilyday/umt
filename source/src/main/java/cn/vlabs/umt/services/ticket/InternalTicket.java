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
package cn.vlabs.umt.services.ticket;

import java.util.Date;


public class InternalTicket implements Ticket {
	private long id;
	private int type;
	private String userip;
	private String random;
	private String extra;
	private String sessionid;
	private Date createTime;
	public void setId(long id) {
		this.id = id;
	}
	public long getId() {
		return id;
	}
	public void setType(int type) {
		this.type = type;
	}
	/* (non-Javadoc)
	 * @see cn.vlabs.umt.tickets.Ticket#getType()
	 */
	public int getType() {
		return type;
	}
	public void setRandom(String random) {
		this.random = random;
	}
	public String getRandom() {
		return random;
	}
	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getExtra() {
		return extra;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	/* (non-Javadoc)
	 * @see cn.vlabs.umt.tickets.Ticket#getSessionid()
	 */
	public String getSessionid() {
		return sessionid;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setUserIp(String userip) {
		this.userip = userip;
	}
	public String getUserIp() {
		return userip;
	}
}
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

public interface TicketDAO {
	/**
	 * 保存Ticket
	 * @param ticket ticket对象
	 * @return 内部TicketID号
	 */
	long save(InternalTicket ticket);
	/**
	 * 删除Ticket
	 * @param ticketid ticket的编号
	 */
	void remove(long ticketid);
	/**
	 * 读取Ticket
	 * @param tickid ticket编号
	 * @return 读取的Ticket对象
	 */
	InternalTicket load(long tickid);
	/**
	 * 删除所有某个时间点之前的内容
	 * @param createTime 创建时间
	 */
	void removeBefore(Date createTime);
}
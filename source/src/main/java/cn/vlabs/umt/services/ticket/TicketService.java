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
package cn.vlabs.umt.services.ticket;

public interface TicketService {
	/**
	 * 注册一个Ticket
	 * @param extra 		Ticket中的额外信息
	 * @param sessionid 	产生Ticket的SessionID
	 * @param type 			Ticket的类型
	 * @return 该Ticket的随机串
	 */
	String generateKeys(String extra, String sessionid, String userip, int type);
	
	/**
	 * 验证一个Ticket有效
	 * @param ticket generateKeys中返回的字符串
	 * @return 如果有效，返回一个Ticket
	 */
	Ticket validate(String ticket);
	/**
	 * 清除所有过期的Ticket
	 */
	void cleanup();
}
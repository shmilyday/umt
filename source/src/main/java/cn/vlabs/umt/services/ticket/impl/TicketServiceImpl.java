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
package cn.vlabs.umt.services.ticket.impl;

import java.util.Date;

import org.apache.commons.lang.time.DateUtils;

import cn.vlabs.umt.common.schedule.ScheduleService;
import cn.vlabs.umt.common.util.RandomUtil;
import cn.vlabs.umt.services.ticket.InternalTicket;
import cn.vlabs.umt.services.ticket.Ticket;
import cn.vlabs.umt.services.ticket.TicketDAO;
import cn.vlabs.umt.services.ticket.TicketService;

public class TicketServiceImpl implements TicketService {
	public TicketServiceImpl(int randlength, int lifetime, ScheduleService scheduler){
		this.randlength=randlength;
		this.lifetime=lifetime;
		this.util=new RandomUtil();
		this.scheduler=scheduler;
	}
	
	public void setTicketDAO(TicketDAO td){
		this.td=td;
	}
	
	public void init(){
		scheduler.schedule(lifetime+1, "TicketMinuteTrigger", "TicketCleanup", CleanTicketJob.class, "service", this);
	}
	
	public void destroy(){
		scheduler.removeJob("TicketCleanup", "TicketMinuteTrigger");
	}
	public void cleanup(){
		Date createTime = DateUtils.addMinutes(new Date(), -(lifetime+1));//lifetime+1分钟后，清理创建的Ticket
		td.removeBefore(createTime);
	}
	
	public String generateKeys(String extra, String sessionid,String userip,  int type) {
		String random= util.getRandom(randlength);
		
		InternalTicket ticket = new InternalTicket();
		ticket.setCreateTime(new Date());
		ticket.setExtra(extra);
		ticket.setSessionid(sessionid);
		ticket.setType(type);
		ticket.setUserIp(userip);
		ticket.setRandom(random);
		
		long id = td.save(ticket);
		String idpart = Long.toHexString(id);
		return idpart+"|"+random;
	}

	public Ticket validate(String ticketString) {
		if (ticketString==null)
		{
			return null;
		}
		int index = ticketString.indexOf('|');
		if (index!=-1){
			String idpart = ticketString.substring(0, index);
			String random = ticketString.substring(index+1);
			try{
				long ticketid	= Long.parseLong(idpart,16);
				InternalTicket ticket = td.load(ticketid);
				if (ticket!=null){
					if (isValid(ticket.getCreateTime())){
						if (ticket.getRandom().equals(random)){
							td.remove(ticketid);
							return ticket;
						}
					}else{
						td.remove(ticketid);
					}
				}
			}catch (NumberFormatException e){
				
			}
		}
		return null;
	}
	
	private boolean isValid(Date start){
		Date deadline = DateUtils.addMinutes(start, lifetime);
		return start.before(deadline);
	}
	
	private RandomUtil util;
	private TicketDAO td;
	private int randlength;
	private int lifetime;
	private ScheduleService scheduler;
}
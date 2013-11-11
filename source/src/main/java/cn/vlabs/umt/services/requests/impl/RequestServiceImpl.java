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
package cn.vlabs.umt.services.requests.impl;

import java.util.Collection;

import cn.vlabs.umt.common.mail.MessageSender;
import cn.vlabs.umt.services.requests.RequestDAO;
import cn.vlabs.umt.services.requests.RequestService;
import cn.vlabs.umt.services.requests.UserExist;
import cn.vlabs.umt.services.requests.UserRequest;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.LoginNameInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.exception.InvalidUserNameException;
import cn.vlabs.umt.ui.UMTContext;

class RequestServiceImpl implements RequestService {
	public RequestServiceImpl(RequestDAO rd, MessageSender sender, UserService us){
		this.rd=rd;
		mails=new RequestMails(sender);
		this.us=us;
	}
	
	public void approveRequest(int rid,UMTContext context) throws UserExist, InvalidUserNameException {
		UserRequest request = rd.getRequest(rid);
		if (request!=null && request.getState()==UserRequest.INIT){
			if (us.getUserByUmtId(request.getUsername())!=null){
				throw new UserExist(request.getUsername());
			}
			User user = new User();
			user.setCstnetId(request.getUsername());
			user.setPassword(request.getPassword());
			user.setTrueName(request.getTruename());
			us.create(user,LoginNameInfo.STATUS_ACTIVE);
			
			rd.updateState(rid, UserRequest.APPROVE, context.getLoginInfo().getUser().getCstnetId());
			mails.sendAcceptMail(request, context);
		}
	}

	public int createRequest(UserRequest request, UMTContext context) {
		mails.sendRegistUser(request, context);
		return rd.createRequest(request);
	}

	public void denyRequest(int rid, UMTContext context) {
		UserRequest request = rd.getRequest(rid);
		if (request!=null && request.getState()==UserRequest.INIT){
			mails.sendDenyUser(request, context);
			rd.updateState(rid, UserRequest.DENY, context.getLoginInfo().getUser().getCstnetId());
		}
	}

	public int getRequestCount(int state) {
		if (state==UserRequest.ALL){
			return rd.getRequestCount();
		}else{
			return rd.getRequestCount(state);
		}
	}

	public Collection<UserRequest> getRequests(int state, int start, int count) {
		Collection<UserRequest> requests =null;
		if (state==UserRequest.ALL){
			requests= rd.getRequests(start, count);
		}else{
			requests= rd.getRequests(state, start, count);
		}
		if (requests.size()>0){
			return requests;
		}
		else{
			return null;
		}
	}

	public void removeRequest(int rid,UMTContext context) {
		UserRequest request = rd.getRequest(rid);
		if (request!=null && request.getState()==UserRequest.INIT){
			//不需要发送通知邮件
			rd.updateState(rid, UserRequest.SILENCE_DENY, context.getLoginInfo().getUser().getCstnetId());
		}
	}
	private RequestDAO rd;
	private UserService us;
	private RequestMails mails;
}
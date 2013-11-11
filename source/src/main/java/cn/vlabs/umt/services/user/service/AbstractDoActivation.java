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
package cn.vlabs.umt.services.user.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForward;

import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.Token;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.UMTContext;
import cn.vlabs.umt.ui.actions.ActivationForm;

/**
 * 定义的激活动作的抽象层次
 * @author lvly
 * @since 2013-3-25
 */
/**
 * @author lvly
 * @since 2013-3-25
 */
public abstract class AbstractDoActivation {
	private HttpServletRequest request;
	private HttpServletResponse response;
	private Token token;
	private User user;
	private ActivationForm data;
	
	public HttpServletRequest getRequest() {
		return request;
	}
	public HttpServletResponse getResponse() {
		return response;
	}
	public AbstractDoActivation(HttpServletRequest request, HttpServletResponse response,Token token,User user,ActivationForm data){
		this.request=request;
		this.response=response;
		this.token=token;
		this.user=user;
		this.data=data;
	}
	public Token getToken() {
		return token;
	}
	public User getUser() {
		return user;
	}
	public ActivationForm getData() {
		return data;
	}
	private boolean isSelf(){
		return SessionUtils.getUserId(request)==token.getUid();
	}
	private boolean hasLogin(){
		UMTContext context=new UMTContext(request);
		return context.getCurrentUMTUser()!=null;
	}
	/**
	 * 里面会重定向，永远返回null;
	 * */
	public ActionForward doActivation()throws Exception{
		if(request==null||response==null){
			toError();
			return null;
		}
		if(token==null||user==null||data==null){
			toError();
			return null;
		}
		boolean hasLogin=hasLogin();
		boolean isSelf=isSelf();
		if(hasLogin&&isSelf){
			return hasLoginAndIsSelf();
		}else if(hasLogin&&!isSelf){
			return hasLoginAndNotSelf();
		}else{
			return notLogin();
		}
	}
	/**
	 * 把form里的元素加到url里面
	 * */
	public String addFormData(String url,ActivationForm data){
		String result=url;
		result=RequestUtil.addParam(result, "tokenid", data.getTokenid()+"");
		result=RequestUtil.addParam(result, "random", data.getRandom());
		result=RequestUtil.addParam(result, "loginNameInfoId", data.getLoginNameInfoId()+"");
		result=RequestUtil.addParam(result, "changeLoginName", data.isChangeLoginName()+"");
		return result;
	}
	public ITokenService getTokenService(){
		return ServiceFactory.getTokenService(getRequest());
	}
	public UserService getUserService(){
		return ServiceFactory.getUserService(getRequest());
	}
	public IUserLoginNameService getLoginNameService(){
		return ServiceFactory.getLoginNameService(getRequest());
	}
	/**错误页面*/
	public abstract ActionForward toError()throws Exception;
	/**
	 * 成功页面
	 * */
	public abstract ActionForward toSuccess() throws Exception;
	
	/**
	 * 现在是登陆状态，且是登陆的是自己
	 * @param request
	 * @param response
	 */
	public abstract ActionForward hasLoginAndIsSelf()throws Exception;
	
	/**
	 * 现在是已登录状态，但登陆的不是自己
	 * @param request
	 * @param response
	 * */
	public abstract ActionForward hasLoginAndNotSelf()throws Exception;

	/**
	 * 现在是未登录状态
	 * @param request
	 * @param response
	 * */
	public abstract ActionForward notLogin()throws Exception;
	
}
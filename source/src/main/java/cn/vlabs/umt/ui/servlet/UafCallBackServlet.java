package cn.vlabs.umt.ui.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.falcon.api.cache.impl.MemcachedCacheService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.BeanFactory;

import cn.vlabs.umt.common.util.CommonUtils;
import cn.vlabs.umt.common.util.Config;
import cn.vlabs.umt.common.util.RequestUtil;
import cn.vlabs.umt.services.session.SessionUtils;
import cn.vlabs.umt.services.user.UserService;
import cn.vlabs.umt.services.user.bean.BindInfo;
import cn.vlabs.umt.services.user.bean.User;
import cn.vlabs.umt.services.user.utils.ServiceFactory;
import cn.vlabs.umt.ui.Attributes;

@SuppressWarnings("serial")
/**
 * UAF登陆回调接口
 * */
public class UafCallBackServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(UafCallBackServlet.class);
	private MemcachedCacheService cacheService;
	private Config config;
	
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		BeanFactory beanFactory=(BeanFactory)config.getServletContext().getAttribute(Attributes.APPLICATION_CONTEXT_KEY);
		this.cacheService=(MemcachedCacheService)beanFactory.getBean("cacheService");
		this.config=(Config)beanFactory.getBean("Config");
	}
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String key=request.getParameter("key");
		if(CommonUtils.isNull(key)){
			LOGGER.info("key can not be null");
			return;
		}
		LOGGER.info("key:"+key.trim());
		Map<String,Object> map=(Map<String,Object>)cacheService.get(key);
		if(map==null){
			LOGGER.info("can't find the key:"+key+" in memcache");
			return;
		}
		LOGGER.info("map:"+map);
		String openId=map.get("userId").toString();
		String screenName=map.get("displayName").toString();
		String idpProvider=map.get("idpProvider").toString();
		String email=map.get("email").toString();
		if(idpProvider.equals(config.getStringProp("uaf.passport.self.idp", ""))){
			response.setStatus(403);
			LOGGER.info("don't bind self ");
			return ;
		}
		UserService userService=ServiceFactory.getUserService(request);
		User umtUser=userService.getUserByOpenid(openId,BindInfo.TYPE_UAF,idpProvider);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_USER, screenName);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_OPEN_ID, openId);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_TYPE, BindInfo.TYPE_UAF);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_URL, idpProvider);
		SessionUtils.setSessionVar(request, Attributes.THIRDPARTY_LOGIN_NAME, email);
		cacheService.remove(key);
		LOGGER.info("login use uaf:"+map);
		if(umtUser==null){
			LOGGER.info("use openId ["+openId+"] bind account,uaf");
			response.sendRedirect(RequestUtil.getContextPath(request)+"/accountBind_createUmt.jsp");
		}else{
			response.sendRedirect(RequestUtil.getContextPath(request)+"/login?type="+BindInfo.TYPE_UAF+"&act=Validate&authBy="+BindInfo.TYPE_UAF+getSiteInfoParam(request));
		}
	}
	private String getSiteInfoParam(HttpServletRequest request){
		StringBuffer result=new StringBuffer();
		Map<String,String> siteInfo=SessionUtils.getSiteInfo(request);
		if(siteInfo!=null){
			for (String param:Attributes.SSO_PARAMS){
				if (siteInfo.get(param)!=null){
					result.append("&").append(param).append("=").append(siteInfo.get(param));
					if(Attributes.RETURN_URL.equals(param)){
						try {
							result.append(URLEncoder.encode("&pageinfo=userinfo", "UTF-8"));
						}catch (UnsupportedEncodingException e){
							LOGGER.error(e.getMessage(),e);
						}
					}
				}
			}
		}
		return result.toString();
	}
}

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<h4>中国科技网通行证应用接入开发</h4>
<div class="commonQA" style="margin-left:10px;">
  <ol>
	<li>
	  <h4>通行证与UMT的关系</h4>
	   <p>UMT即用户管理工具，是一套能实现用户管理以及单点登录统一身份认证的软件系统，是协同工作环境套件Duckling的核心组件之一。</p>		
	   <p>中国科技网通行证是基于UMT的统一账号系统。通行证账号包括中科院邮件系统账号以及原Duckling通行证账号共计约20万用户账号。</p>			
	</li>
	<li>
	   <h4>UMT提供UMT OAuth2授权方案</h4>
	   <p>UMT OAuth2授权方案下载：<a href="http://www.escience.cn/docs/umt-oauth2/umt-oauth2.pdf" target="_blank">UMT-OAuth2授权方案.pdf</a></p>
	   <p>UMT OAuth2 SDK下载：<a href="http://www.escience.cn/docs/umt-oauth2/umt-oauth2-client-0.1.3.jar" target="_blank">UMT-OAuth2-client-0.1.3.jar</a></p>
	   </li>
		<li>
			<h4>申请应用接入账号（需要登录）</h4>
			<p><a href="<umt:url value="/user/developer.do?act=display"/>">点击申请</a></p>  
		</li>
	</ol>
</div>
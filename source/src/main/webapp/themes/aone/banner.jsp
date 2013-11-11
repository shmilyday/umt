<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="umt" uri="WEB-INF/tld/umt.tld"%>
<fmt:setBundle basename="application" />
<%
	pageContext.setAttribute("contextPath", request.getContextPath());
	pageContext.setAttribute("DecludeMenu", request
			.getParameter("DecludeMenu"));
%>
<script type="text/javascript"
	src="${contextPath}/js/jquery-1.3.2.min.js"></script>
<script type="text/javascript" src="${contextPath}/js/menu.js"></script>
<script type="text/javascript" src="${contextPath}/js/cookie.js"></script>
<div class="banner">
	<fmt:message key='banner.title' />
	<div class="banner_r">
		<ul>
			<c:if test="${DecludeMenu==null}">
				<umt:HasLogin>
					<li>
						<umt:CurrentUserName />
					</li>
					<li>
						<a href="${contextPath}/logout"><fmt:message
								key='banner.logout' /> </a>
					</li>
				</umt:HasLogin>
				<umt:NotLogin>
					<li>
						<a href="${contextPath}/login"><fmt:message key='banner.login' />
						</a>
					</li>
				</umt:NotLogin>
			</c:if>
			<li>
				<a id="lang"><fmt:message key='banner.language' />
				</a>
			</li>
		</ul>
		<div class="langmenu" id="langmenu" style="display:none">
			<ul>
				<li>
					<a href="#" onclick="changeLocale('zh_CN')">中文</a>
					<a href="#" onclick="changeLocale('en_US')">English</a>
				</li>
			</ul>
		</div>
		<script type="text/javascript">
				function changeLocale(lang){
					var cookie=new Cookie();
					cookie.setCookie('umt.locale', lang, {expireDays:365, path:'${contextPath}'});
					window.location.reload();
				};
				
				$(document).ready(function(){
					$('#lang').menu({menuid:'#langmenu'});
				});
			</script>
	</div>
</div>

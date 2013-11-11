<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='accountManage.bind.title'/></title>
		<link href="<umt:url value="/images/favicon.ico"/>" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<li id="accountManage"><a href="<umt:url value="/user/manage.do?act=showManage"/>"><fmt:message key='accountManage.accountInfo'/></a></li>
				<c:if test="${'qq'!=loginInfo.user.type||'weibo'!=loginInfo.user.type }">
					<li id="changePsw"><a href="<umt:url value="/user/manage.do?act=showChangePassword"/>"><fmt:message key='accountManage.changePassword.title'/></a></li>
				</c:if>
				<li class="active" id="accountBind"><a href="<umt:url value="/user/manage.do?act=showBindAccount"/>"><fmt:message key='accountManage.bind.title'/></a></li>
				<div class="clear"></div>
			</ul>
			<div class="sub-content" id="accountBindShow" >
				<h4 class="sub-title"><fmt:message key='accountManage.bind.title'/></h4>
				<p class="sub-text"><fmt:message key='accountManage.bind.hint'/></p>
				<table class="saftyList manage mid">
					<tr>
						<th><fmt:message key='accountManage.third.party'/></th>
						<th><fmt:message key='accountManage.third.bind.name'/></th>
						<th><fmt:message key='common.operation'/></th>
					</tr>
					<c:forEach items="${bindInfos }" var="info">
					<tr>
						<td>
						<c:choose>
							<c:when test="${info.type=='weibo' }">
								<img src="<umt:url value="/images/login/weibo.com.gif"/>"/> <fmt:message key='thirdParty.weibo'/>
							</c:when>
							<c:when test="${info.type=='qq' }">
								<img src="<umt:url value="/images/login/qqhao.gif"/>"/><fmt:message key='thirdParty.qq'/>
							</c:when>
							<c:when test="${info.type=='cashq' }">
								<img src="<umt:url value="/images/login/casSso.gif"/>"/><fmt:message key='thirdParty.cashq'/>
							</c:when>
							<c:when test="${info.type=='uaf' }">
								<a href="${info.url }"><img src="<umt:url value="/images/login/uaf.png"/>"/>
									<fmt:message key='thirdParty.uaf'>
										<fmt:param>&nbsp;</fmt:param>
									</fmt:message>
								</a>
							</c:when>
							<c:otherwise>
								<img src="<umt:url value="/images/login/weibo.com.gif"/>"/>${info.type }
							</c:otherwise>
						</c:choose>
						</td>
						<td><span class="success-text">${info.trueName }</span></td>
						<td><a href="<umt:url value="/user/manage.do?act=deleteBind&bindId=${info.id }"/>"><fmt:message key='create_sucess_message.unbind'/></a></td>
					</tr>
					</c:forEach>
				</table>
			</div>
			
		</div>
		
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		
	</body>
	
	<script type="text/javascript" >
		$(document).ready(function(){
			$('#banner_user_manage').addClass("active");
		});
	</script>
</html>
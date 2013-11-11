<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:refreshUser/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='index.title'/></title>
		<link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<f:css href="css/jqModal.css"/>
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<div class="container login gray">
			<c:if test="${!empty msg }">
				<h3 class="success slide"><fmt:message key='${msg }'/></h3>
			</c:if>
			<h2 class="total-title">${loginInfo.user.trueName }<span class="small-font"><a href="<umt:url value="/user/info.do?act=show"/>"/><fmt:message key='common.change'/></a></span></h2>
			<div class="form-horizontal" style="padding:30px 5% 0">
				<jsp:include flush="true" page="loginEmailActive.jsp"/>
				
				<c:forEach items="${secondaryEmail}" var="loginName" varStatus="index" >
					<div class="control-group">
						<label class="control-label nopadding">
								<c:if test="${index.index ==0}">
								<fmt:message key='common.userInfo.secondaryMail'/>
								</c:if>
						</label>
						<div class="controls">
							<c:choose>
								<c:when test="${loginName.status=='active' }">
										<strong>${loginName.loginName}</strong>
										<span class="success-text small-font"><fmt:message key='common.leftBracket'/><fmt:message key='accountManage.account.isVerified'/><fmt:message key='common.rightBracket'/></span>
								</c:when>
								<c:when test="${loginName.status=='temp' }">
										<strong>${loginName.loginName}</strong>
										<span class="small-font"><fmt:message key='common.leftBracket'/><fmt:message key='accountManage.account.isNotVerified'/><fmt:message key='common.rightBracket'/></span>
								</c:when>
							</c:choose>
							<c:if test="${index.index ==0}">
								<a class="small-font" href="<umt:url value="/user/manage.do?act=showManage"/>"><fmt:message key='index.userInfo.change'/></a>
							</c:if>
						</div>
						</div>
				</c:forEach>
	            
				<div class="control-group">
	              	<label class="control-label nopadding">
						<strong><fmt:message key='common.userInfo.password'/></strong>
					</label>
	              	<div class="controls">
						********
						<c:if test="${loginInfo.user.type!='weibo'}">
					   		<a class="small-font"  href="<%=request.getContextPath() %>/user/password.do?act=showChangePassword"><fmt:message key='inputpassword.title' /></a> 
					  	</c:if>
	              	</div>
	            </div>
			</div>
			
			<c:if test="${'weibo'!=loginInfo.user.type }">
				<h3 class="success"><fmt:message key='accountSafty.title'/></h3>
				<jsp:include flush="true" page="safeItems.jsp">
					<jsp:param value="index" name="from"/>
				</jsp:include>
			</c:if>
			<%--<c:if test="${!empty myAppList }" >
				<h3 class="success"><fmt:message key='index.my.application'/></h3>
				<p class="config"><fmt:message key='index.applist.title'/></p>
				<jsp:include flush="true" page="/my_app_list.jsp"/>
				<h3 class="success"><fmt:message key='index.other.application'/></h3>
				<p class="config"><fmt:message key='index.applist.title'/></p>
				<jsp:include flush="true" page="/app_list.jsp"/>
			</c:if> --%>
			<%--<c:if test="${empty myAppList }" > --%>
				<h3 class="success"><fmt:message key='index.application'/></h3>
				<p class="config"><fmt:message key='index.applist.title'/></p>
				<jsp:include flush="true" page="/app_list.jsp"/>
			<%--</c:if>--%>
			<h3 class="success"><fmt:message key='index.oauth.manage.in'/></h3>
			<p class="config"><a href="<%=request.getContextPath() %>/user/developer.do?act=display"><fmt:message key='index.oauth.manage'/></a></p>
			
			<umt:IsAdmin>
				<h3 class="success"><fmt:message key='common.admin'/></h3>
				<p class="config"><a href="<%=request.getContextPath() %>/admin/manageUser.do?act=showUsers"><fmt:message key='system.admin.tip' /></a></p>
			</umt:IsAdmin>
			
		</div>
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		<script>
		$(document).ready(function(){
			$('#banner_username').addClass("active");
			//var passport=new Passport({umtUrl:'http://passport.escience.cn',viewPort:$("#testDiv"),message:'haha,runing...',loginclass:'miaomiao'});
			//passport.checkLogin(function(result){
			//	alert(result);
			//});
			//$('#dialog').jqm();
		});
		</script>
	</body>
</html>

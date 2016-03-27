<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<c:if test="${empty sessionScope.thirdParty_type }">
	<jsp:forward page="login"/>
</c:if>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='thirdParty.bind.title'/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="/banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login">
			<form id="bindForm" class="form-horizontal" method="post" action="<umt:url value="/bind.do"/>?act=bindUmt">
				<!-- 登陆 -->
				<div id="bind_table">
					<div class="control-group">
					    <label class="control-label" for="loginName">
							<span class="ness">*</span><fmt:message key="regist.form.username"/>
						</label>
				        <div class="controls">
				          	<input id="loginName" name="loginName" type="text"></input>
							<span id="loginName_error_place" class="error help-inline">
								<c:if test="${!empty loginName_error }">
									<fmt:message key="${loginName_error }"/>
								</c:if>
							</span>
				         </div>
				     </div>
				     <div class="control-group">
					    <label class="control-label" for="loginPassword">
							<span class="ness">*</span><fmt:message key="regist.form.password"/>
						</label>
				        <div class="controls">
				          	<input id="loginPassword" name="loginPassword" type="password"></input>
							<span id="loginPassword_error_place" class="error help-inline">
								<c:if test="${!empty loginPassword_error }">
									<fmt:message key="${loginPassword_error }"/>
								</c:if>
							</span>
				         </div>
				     </div>
				     <div class="control-group">
				        <div class="controls">
				          	<input type="submit" class="btn long btn-primary" value="<fmt:message key="accountBind.bindnow"/>" />
				         </div>
				     </div>
			     </div>
				<input type="hidden" name="screenName" value="${thirdParty_user}"/>
				<input type="hidden" name="openId" value="${thirdParty_openId}"/>
				<input type="hidden" name="type" value="${thirdParty_type }"/>
			</form>
		</div>
		
		
		
		
		
		
		
		
		
		
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
		</script>
	</body>
</html>
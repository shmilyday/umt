<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>用户权限选择</title>
</head>
<body>
	<jsp:include flush="true" page="../banner2013.jsp"></jsp:include>
	<br/><br/><br/><br/><br/><br/>
	<form action="authorize?${user_oauth_request.url}" method="post">
		<input type="hidden" name="client_id" value="${client_id }">
		<input type="hidden" name="pageinfo" value="userscope">
		<c:forEach items="${scopes}" var="scope">
			<c:choose>
				<c:when test="${scope.status eq 'checked' }">
					<input type="checkbox" name="userScopes" checked="checked" value="${scope.id }">				
				</c:when>
				<c:otherwise>
					<input type="checkbox" name="userScopes" value="${scope.id }">				
				</c:otherwise>
			</c:choose>
			${scope.name}<p/>
		</c:forEach>
		 <div class="control-group thirdLogin">
              	<div class="controls small-font">
              		<p><fmt:message key='common.login.userOtherName'/>  
              			<a href="<umt:url value="/thirdParty/login?type=weibo"/>"><img src="<umt:url value="/images/login/weibo.png"/>" alt="用新浪微博登录" /></a>
						<a href="<umt:url value="/thirdParty/login?type=qq"/>"><img src="<umt:url value="/images/login/qq.png"/>" alt="用QQ账号登录" /></a>
						<a href="<umt:url value="/thirdParty/login?type=cashq"/>"><img src="<umt:url value="/images/login/cashq.png"/>" alt="使用UAF登录" title="使用UAF登录" /></a>
						<a href="<umt:url value="/thirdParty/login?type=uaf"/>"><img src="<umt:url value="/images/login/uaf.png"/>" alt="使用UAF登录" title="使用UAF登录" /></a>
					 	<a class="btn cancle bit right" style='display:none'><fmt:message key='common.login.returnBack'/></a>
					 </p>
              	</div>
		      </div>   
		
		<input type="submit" value="提交">
	</form>
	<jsp:include flush="true" page="../bottom2013.jsp"></jsp:include>
</body>
</html>
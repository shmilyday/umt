<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
		<title><fmt:message key="banner.help"/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
</head>
<body class="login">
	<jsp:include flush="true" page="banner2013.jsp">
		<jsp:param name="DecludeMenu" value="true"/>
	</jsp:include>
  	<div class="container gray login">
		<div class="help-content readonly">
   			<p class="maintain">尊敬的用户，您好！</p>
   			<p class="indent maintain">系统正在升级维护中，除登录功能外，其它功能暂时停止服务，系统将于${recoveryTime }全面恢复，请稍后再访问，不便之处敬请谅解！</p>
   			<%-- <img src="<%= request.getContextPath() %>/images/updating.png"/> --%>
   			<hr></hr>
   			<p class="indent">您现在仍然可以正常使用接入中国科技网通行证的其它应用服务：</p>
   			<jsp:include flush="true" page="/app_list.jsp"/>
   			<p class="sign">中国科技网通行证</p>
   			<p class="sign"><fmt:formatDate value="${date }" pattern="yyyy年MM月dd日"/></p>
		</div>
	</div>
<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>

</body>
<script>
$(document).ready(function(){
	$('#banner_help').addClass("active");
});
</script>

</html>
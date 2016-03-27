<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   
 <%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<%
        pageContext.setAttribute("contextPath", request.getContextPath());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='banner.digitalCertificateSetting'/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<f:script  src="${contextPath }/thirdparty/jquery.jeditable.js"/>
		
			<div class="container login gray">
				<ul class="sub-nav">
						<li id="accountManage"><a href="<umt:url value="/user/digitalCertificate.do?act=index"/>"><fmt:message key='digitialManage.title.digitialManage' /></a></li>
						<li class="active" id="accountApp"><a href="<umt:url value="/user/digitalCertificate.do?act=manage"/>"><fmt:message key='digitialManage.title.manage' /></a></li>
						<li id="accountBind"><a href="<umt:url value="/user/digitalCertificate.do?act=help"/>"><fmt:message key='digitialManage.title.help' /></a></li>
					<div class="clear"/>
				</ul>
				<div class="sub-content" id="accountManageShow">
					<c:choose>
						<c:when test="${errorType=='applyError' }">
							<fmt:message key='digitialManage.manage.error.apply'/>
						</c:when>
						<c:when test="${errorType=='downloadError' }">
							<fmt:message key='digitialManage.manage.error.download'/>
						</c:when>
						<c:when test="${errorType=='noPermission' }">
							<fmt:message key='digitialManage.manage.error.noPermission'/>
						</c:when>
						<c:when test="${errorType=='notExist' }">
							<fmt:message key='digitialManage.manage.error.notExist'/>
						</c:when>
						<c:when test="${errorType=='paramError' }">
							<fmt:message key='digitialManage.manage.error.paramError'/>
						</c:when>
						<c:when test="${errorType=='getKeyError' }">
							<fmt:message key='digitialManage.manage.error.getKeyError'/>
						</c:when>
						<c:otherwise>
							<fmt:message key='digitialManage.manage.error.other'/>${message }
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			
		
		
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		<script type="text/javascript" >
			$(document).ready(function(){
				$('#banner_user_digitalCertificate').addClass("active");
				$("#closeTooltip").live("click",function(){
					$(this).parent().parent().hide();
				});
				
				
				$("#editPassword").live('click',function(){
					$(this).parents("p#passwordShow").hide();
					$("#passwordInput").show();
				}); 
				$("#savePassword").live('click',function(){
					$(this).parents("p#passwordInput").hide();
					$("#passwordText").text($(this).parents("p#passwordInput").find("input:first").val());
					$("#passwordShow").show();
					
				}); 
				
				
			});
			
			
		</script>
	</body>
</html>
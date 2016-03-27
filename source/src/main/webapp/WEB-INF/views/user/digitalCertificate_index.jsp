<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   
<fmt:setBundle basename="application" />
<umt:AppList/>
<umt:refreshUser/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='banner.accountSetting'/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<li class="active" id="accountManage"><a href="<umt:url value="/user/digitalCertificate.do?act=index"/>"><fmt:message key='digitialManage.title.digitialManage' /></a></li>
				<li id="accountApp"><a href="<umt:url value="/user/digitalCertificate.do?act=manage"/>"><fmt:message key='digitialManage.title.manage' /></a></li>
				<%-- <li id="accountBind"><a href="<umt:url value="/user/digitalCertificate.do?act=record"/>">登录记录</a></li> --%>
				<li id="accountBind"><a href="<umt:url value="/user/digitalCertificate.do?act=help"/>"><fmt:message key='digitialManage.title.help' /></a></li>
				<div class="clear"/>
			</ul>
			<div class="sub-content" id="accountManageShow">
				<p class="sub-text"><fmt:message key='digitialManage.index.totip.show'/></p>
				<p class="sub-text"><strong><fmt:message key='digitialManage.index.totip.system.support'/></strong></p>
				<p class="sub-text"><fmt:message key='digitialManage.index.totip.system.support.detail'/></p>
				<p class="sub-text"><strong><fmt:message key='digitialManage.index.totip.browser.support'/></strong></p>
				<p class="sub-text"><fmt:message key='digitialManage.index.totip.browser.support.detail'/></p>
				
				<c:if test="${empty caList }">
					<p class="sub-text"><fmt:message key="digitialManage.index.applay.empty"/></p>
					<P class="sub-text"><button class="btn long btn-primary" id="applyDigital"><fmt:message key='digitialManage.btn.apply'/></button></P>
					<P class="sub-text"><fmt:message key="digitialManage.index.applay.totip"/></P>
				</c:if>
				<c:if test="${not empty caList }">
					<P class="sub-text"><button class="btn long btn-primary" id="manageDigital"><fmt:message key='digitialManage.btn.manage'/></button></P>
				</c:if>
				
				<p> &nbsp;  </p>
				<p class="sub-text blackfont"><strong><fmt:message key="digitialManage.index.help"/></strong></p>
				<P class="sub-text blackfont"><strong><fmt:message key="digitialManage.index.whatIs"/></strong></P>
				<P class="sub-text blackfont">关于数字证书的说明</P>
				<P class="sub-text blackfont"><strong><fmt:message key="digitialManage.index.howUse"/></strong></P>
				<P class="sub-text blackfont">关于数字证书的说明</P>
				<P class="sub-text blackfont"><strong><fmt:message key="digitialManage.index.howApplay"/></strong></P>
				<p class="sub-text blackfont">申请流程介绍</p>
			</div>
		</div>
		
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		<script type="text/javascript" >
			function confirmDelete(obj){
				var result=confirm("<fmt:message key='accountManage.secondaryMail.delete'/>");
				if(result){
					window.location.href=$(obj).attr("url");
				}
			}
			function confirmDeleteLdap(obj){
				var result=confirm("<fmt:message key='account.manage.delete.ldap'/>");
				if(result){
					window.location.href=$(obj).attr("url");
				}
				
			}
			
			
			$("#applyDigital").on("click",function(){
				window.location.href='<umt:url value="/user/digitalCertificate.do?act=applyView"/>';
			});
			
			$("#manageDigital").on("click",function(){
				window.location.href='<umt:url value="/user/digitalCertificate.do?act=manage"/>';
			});
			
			$(document).ready(function(){
				$('#banner_user_digitalCertificate').addClass("active");
				$("#closeTooltip").live("click",function(){
					$(this).parent().parent().hide();
				});
			});
			
		</script>
	</body>
</html>
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
		<title>账号绑定</title>
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
				<li id="changePsw"><a href="<umt:url value="/user/manage.do?act=showChangePassword"/>">修改密码</a></li>
				<li class="active" id="accountBind"><a href="<umt:url value="/user/manage.do?act=showBindAccount"/>">账号绑定</a></li>
				<li id="accountAssociate"><a href="<umt:url value="/user/manage.do?act=showAssociate"/>">关联</a></li>
				<div class="clear"></div>
			</ul>
			<div class="sub-content" id="accountBindShow" >
				<h4 class="sub-title">账号关联</h4>
					 <table class="saftyList manage mid">
					<tr>
						<td>
						  ${associatedUser.primaryEmail }
						</td>
						<td><a href="<umt:url value="/user/manage.do?act=updateAssociate&associateId=${associate.id }"/>">修改</a></td>
						<td><a href="<umt:url value="/user/manage.do?act=deleteAssociate&associateId=${associate.id}"/>">解除关联</a></td>
					</tr>
				</table>
				<p class="sub-text">可以选择中国科技网通行证其他账号，与本帐号相关联</p>
				
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
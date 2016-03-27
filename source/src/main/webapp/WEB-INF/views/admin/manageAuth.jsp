<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<%
	pageContext.setAttribute("contextPath", request.getContextPath());
	pageContext.setAttribute("DecludeMenu",
			request.getParameter("DecludeMenu"));
	String showLogin = request.getParameter("showLogin");
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key="admin.oauth.menu" /></title>
<link href="../css/umt.css" rel="stylesheet" type="text/css" />
<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap.min.css" />
<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap-responsive.min.css" />
<script type="text/javascript"
	src="${contextPath}/js/jquery-1.7.2.min.js"></script>
<f:css href="${contextPath }/thirdparty/bootstrap/js/bootstrap.min.js" />
<f:css href="${contextPath }/css/umt2013.css" />
<f:css href="${contextPath }/css/umt2013-responsive.css" />
<f:css href="${contextPath }/css/umt-admin.css" />
<style type="text/css">
	td.nosee{overflow:hidden;white-space:nowrap;}
	.inline{display:inline;margin-bottom:0;margin-left:7px;}
</style>
</head>
<body class="login">
	<jsp:include flush="true" page="/banner.jsp"></jsp:include>
	<script type="text/javascript" src="../js/jquery.corner.js"></script>
	<div class="container login gray">
		<jsp:include flush="true" page="/menu.jsp"></jsp:include>
		<div class="content-container">
			<div class="toolBar">
				<button type="button" id="addClient" class="btn btn-primary"
					style="float: right; margin: 0 10px 0 0;">
					添加第三方认证
				</button>
			</div>
			<div class="userTable" style="height: 527px;">
				<table class="table table-hover"
					style="word-wrap: break-word; table-layout: fixed;" id="sessions"
					border="0">
					<tbody>
						<tr class="top">
							<td width="30px">#</td>
							<td width="5%">代号</td>
							<td width="7%">名称	</td>
							<td >服务器URL</td>
							<td width="10%">ClientId</td>
							<td width="10%">界面主题</td>
							<td width="10%">显示在登录窗口</td>
							<td width="10%">操作</td>
						</tr>
						<c:forEach items="${auths}" varStatus="status" var="client">
							<tr class="firstline ">
								<td>${status.index+1 }</td>
								<td>${client.code}</td>
								<td>${client.name}</td>
								<td>${client.serverUrl}</td>
								<td>${client.clientId }</td>
								<td>${client.theme}</td>
								<td>${client.showInLogin}</td>
								<td width="20%">
									<a onclick="updateClient('${client.code}')">更新</a>
									<a onclick="deleteClient('${client.code}')">删除</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
		<jsp:include page="parts/addThirdPartyDialog.jsp"/>
		<jsp:include page="parts/updateThirdPartyDialog.jsp"/>
		<div
			style="display: none; position: absolute; z-index: 999; top: 10%; left: 30%; right: 30%; background-color: #fff; border: 5px solid #888; box-shadow: 0 3px 7px rgba(0, 0, 0, 0.8); height: 300px;"
			class="oauthClientOperate">
			<a class="closeOperate"
				style="float: right; background: #08a; color: #fff; font-weight: bold; padding: 5px;">X</a>
			<div class="operateMessage">
				<p style="color: red" class="messageShow"></p>
			</div>
			<h4 id="operateTitle" style="font-size: large;"></h4>
			<div></div>
		</div>
		<form id="removeForm" action="<umt:url value="/admin/manageAuth.do"/>" method="POST">
			<input type="hidden" name="act" value="remove"/>
			<input type="hidden" name="code" id="removeCode"/>
		</form>
		<script type="text/javascript"
			src="<umt:url value="/js/jquery.validate.min.js"/>"></script>
		<script type="text/javascript"
			src="<umt:url value="/thirdparty/bootstrap/js/bootstrap.min.js"/>"></script>
		<script type="text/javascript">
			function deleteClient(code){
				if(confirm("确认要删除第三方认证配置么?")){
					$("#removeCode").val(code);
					$("#removeForm").submit();
				}
			};
			$(document).ready(function() {
				$(".nav li.current").removeClass("current");
				$("#thirdpartyMenu").addClass("current");
			});
		</script>
	</div>
	<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
</body>
</html>
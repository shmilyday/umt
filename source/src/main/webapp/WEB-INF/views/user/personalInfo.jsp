<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:AppList />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title><fmt:message key="userinfo.title" /></title>
<link href="<%= request.getContextPath() %>/images/favicon.ico"
	rel="shortcut icon" type="image/x-icon" />
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
</head>
<body class="login">
	<jsp:include page="../../../banner2013.jsp" />
	<div class="container login gray">
		<h2 class="login-title">
			<fmt:message key='userinfo.title' />
		</h2>
		<form id="infoForm" class="form-horizontal" method="post"
			action="<umt:url value="/user/info.do?act=save"/>">
			<div class="control-group">
				<label class="control-label nopadding" for="password"> <fmt:message
						key='common.userInfo.cstnetID' />
				</label>
				<div class="controls">${loginInfo.user.cstnetId }</div>
			</div>
			<div class="control-group">
				<label class="control-label" for="password"> <fmt:message
						key='common.userInfo.truename' />
				</label>
				<div class="controls">
					<input maxlength="30" name="trueName" id="trueName" type="text"
						value="<c:out value="${loginInfo.user.trueName }"/>"></input> <span
						id="trueName_error_place" class="error help-inline"></span>
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button class="btn long btn-primary" type="submit">
						<fmt:message key='userinfo.submit' />
					</button>
				</div>
			</div>

		</form>
	</div>
	<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>

	<script type="text/javascript">
	$(document).ready(function(){
		$('#infoForm').validate({
			rules:{
				trueName:{required:true}
			},
			messages:{
				trueName:{required:'<fmt:message key="common.validate.truename.required"/>'}
			},
			errorPlacement: function(error, element){
				 var sub="_error_place";
				 var errorPlaceId="#"+$(element).attr("name")+sub;
				 	$(errorPlaceId).html("");
				 	error.appendTo($(errorPlaceId));
			}
		});	
		$('#banner_user_info').addClass("active");
	});
	</script>
</body>
</html>

<%@page language="java" pageEncoding="UTF-8"%>
<%@taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt_rt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="umt" uri="WEB-INF/tld/umt.tld"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<%
	pageContext.setAttribute("contextPath", request.getContextPath());
%>
<fmt:setBundle basename="application" />
<!DOCTYPE html>
<html>
<head>
<title>${app.clientName}</title>
<f:script  src="${contextPath }/js/jquery-1.7.2.min.js"/>
<f:script  src="${contextPath }/js/ValidateCode.js"/>
<f:script  src="${contextPath }/js/scopechecker.js"/>
<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap.min.css"/>
<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap-responsive.min.css"/>
<f:css href="${contextPath }/css/wifi.css"/>

<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
</head>
<body class="wifi">
	<jsp:include flush="true" page="/banner2013.jsp">
		<jsp:param name="DecludeMenu" value="true" />
	</jsp:include>
	<div class="wifi container">
		<h3 class="title">
			欢迎使用${app.clientName}(<span class="icon_wifi"></span>CSTNET)
		</h3>
		<p class="subTitile">
			${app.clientName}目前只对&nbsp;<b>${scope}</b>&nbsp;域开放。请输入您的通行证账户密码启用无线WiFi。
		</p>
		<form id="enableForm" class="form-horizontal"
			action="<umt:url value="/enableWifi.do"/>" method="POST">
			<input type="hidden" name="clientId" value="${app.id}" /> <input
				type="hidden" name="act" value="save" />
			<div class="control-group">
				<label class="control-label">账号：</label>
				<div class="controls">
					<input type="text" name="username" id="username"
						value="${username}" /> <span id="username_error_place"
						class="error help-inline"> </span>
				</div>
			</div>
			<div class="control-group">
				<label class="control-label">通行证密码：</label>
				<div class="controls">
					<input type="password" name="password" id="password" /> <span
						id="password_error_place" class="error help-inline"> <c:if
							test="${passworderror!=null}">
							<fmt:message key="${passworderror}" />
						</c:if>
					</span>
				</div>
			</div>
			<c:if test="${showValidate!=null}">
				<div class="control-group">
					<label class="control-label">验证码：</label>
					<div class="controls">
						<input type="text" maxlength="255" name="validateCode" /> <img
							id="ValidCodeImage" src="" /> <a class="small_link" href="#"
							onclick="imageObj.changeImage()"><fmt:message
								key="login.imagetext.changeit" /></a><br /> <span
							id="validateCode_error_place" class="error help-inline"> <c:if
								test="${validateCodeError!=null}">
								<fmt:message key="login.imagetext.wrong" />
							</c:if>
						</span>
					</div>
				</div>
			</c:if>
			<div class="control-group">
				<div class="controls">
					<p class="agree">
						<input type="checkbox" id="agreeCheck" checked>同意<a
							id="agreement">服务条款</a>
					</p>
					<div class="agreementBox">
						<jsp:include page="agreement.jsp"></jsp:include>
					</div>
				</div>
			</div>
			<div class="control-group">
				<div class="controls">
					<button id="submit" class="btn btn-primary" type="submit">启用</button>
				</div>
			</div>
		</form>
	</div>
	<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>

	<script type="text/javascript">
			var imageObj;
			var checker= new ScopeChecker("${app.pubScope}");
			$(document).ready(function(){
				$("a#agreement").click(function(){
					$(".agreementBox").toggle();
				});
				function toggleSubmitState(){
					var checkbox = $("#agreeCheck");
					if (checkbox.attr("checked") && checker.isValidScope($("#username").val())){
						$("#submit").removeAttr("disabled");
					}else{
						$("#submit").attr("disabled", true);
					}
				};
				$("#agreeCheck").click(function(){
					toggleSubmitState();
				});
				$("#password").focus(function(){
					$("#password_error_place").html("");
				});
				$("#username").change(function(){
					$.get('<umt:url value="/enableWifi?act=canBeOpen"/>', {'username':$("#username").val(),'clientId':"${app.id}"}, function(canBeOpen){
						if (canBeOpen){
							$("#username_error_place").html("");
							toggleSubmitState();
						}else{
							$("#username_error_place").html("<fmt:message key='wifi.app.alreadyOpen'/>");
						}
					});
				});
				$('#enableForm').validate({
					rules: {
						password:{required:true},
						username:{required:true},
						validateCode:{required:true}
					 },
					 messages: {
						 password:{
							 required:"<fmt:message key='common.validate.password.required'/>"
						 },
						 username:{
							 required:"<fmt:message key='login.username.required'/>"
						 },
						 validateCode:{
							 required:"<fmt:message key='login.imagetext.required'/>"
						 }
						 
					 },
					 errorPlacement: function(error, element){
						 var sub="_error_place";
						 var errorPlaceId="#"+$(element).attr("name")+sub;
						 	$(errorPlaceId).html("");
						 	error.appendTo($(errorPlaceId));
					 }
				});
				imageObj = ValidateImage("ValidCodeImage");
			});
		</script>
</body>
</html>
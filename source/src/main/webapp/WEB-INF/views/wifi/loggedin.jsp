<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<%
        pageContext.setAttribute("contextPath", request.getContextPath());
%>
<!DOCTYPE html>
<html>
	<head>
		<title>科技网无线WiFi联盟</title>
		<f:script  src="${contextPath }/js/jquery-1.7.2.min.js"/>
		<f:script  src="${contextPath }/js/ValidateCode.js"/>
		<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap.min.css"/>
		<f:css href="${contextPath }/thirdparty/bootstrap/css/bootstrap-responsive.min.css"/>
		<f:css href="${contextPath }/css/wifi.css"/>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1" />
	</head>

	<body class="wifi"> 
		<jsp:include flush="true" page="/banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<div class="wifi container">
			<h3 class="title"><span class="icon_wifi"></span>${app.clientName}无线服务一键开通</h3>
			<p class="subHint">您所在的域${userDomain}已加入${app.clientName}联盟，可开通WiFi漫游服务。<span class="mayHidden">开通后，您使用中国科技网通行证账号即可在${app.clientName}联盟无线网络覆盖区域享受WiFi漫游服务，并作为eduroam的一员享受国际学术免费WiFi漫游服务（覆盖全球70多个国家/地区的上千所学校和研究机构）。请输入您的通行证密码一键开通。</span></p>
			<form id="enableForm" class="form-horizontal"  action="enableWifi" method="POST">
				<input type="hidden" name="clientId" value="${app.id}"/>
				<input type="hidden" name="act"	value="save"/>
				<div class="control-group">
	       			<label class="control-label">开通账户：</label>
	       			<div class="controls padding">
	         			${user.cstnetId}
	         			<input type="hidden" name="username" value="${user.cstnetId}"/>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<label class="control-label">通行证密码：</label>
	       			<div class="controls">
	         			<input type="password" name="password"/>
	         			<span id="password_error_place" class="error help-inline">
	         				<c:if test="${passworderror!=null}">
	         					<fmt:message key="${passworderror}"/>
	         				</c:if>
						</span>
	       			</div>
	       		</div>
	       		<c:if test="${showValidate!=null}">
		       		<div class="control-group">
		       			<label class="control-label">验证码：</label>
		       			<div class="controls">
		         			<input type="text" maxlength="255" name="validateCode"/>
		         			<img id="ValidCodeImage" src="" />
							<a class="small_link" href="#"
								onclick="imageObj.changeImage()"><fmt:message
								key="login.imagetext.changeit" /></a><br/>
		         			<span id="validateCode_error_place" class="error help-inline">
		         				<c:if test="${validateCodeError!=null}">
		         					<fmt:message key="login.imagetext.wrong"/>
		         				</c:if>
							</span>
		       			</div>
		       		</div>
	       		</c:if>
	       		<div class="control-group">
	       			<div class="controls">
	         			<p class="agree"><input type="checkbox" id="agreeCheck" checked>同意<a id="agreement">服务条款</a></p>
	         			<div class="agreementBox">
							<jsp:include page="agreement.jsp"></jsp:include>
	         			</div>
	       			</div>
	       		</div>
	       		<div class="control-group">
	       			<div class="controls">
	       				<button id="submit" class="btn btn-primary" type="submit">立即开通</button>
	       			</div>
	       		</div>
			</form>
			<h4 class="related">相关链接</h4>
			<ul class="wifi-links">
				<li><a href="http://eduroam.cstnet.cn/" target="_blank">eduroam CN无线网漫游交换中心</a></li>
				<li><a href="http://eduroam.cstnet.cn/total/283.jhtml" target="_blank">eduroam CN漫游研究所和大学列表</a></li>
			</ul>
		</div>
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
			var imageObj;
			$(document).ready(function(){
				$("a#agreement").click(function(){
					$(".agreementBox").toggle();
				});
				$("#agreeCheck").click(function(){
					var checkbox = $("#agreeCheck");
					if (checkbox.attr("checked")){
						$("#submit").removeAttr("disabled");
					}else{
						$("#submit").attr("disabled", true);
					}
				});
				$("#password").focus(function(){
					$("#password_error_place").html("");
				});
				$('#enableForm').validate({
					rules: {
						password:{required:true}
					 },
					 messages: {
						 password:{
							 required:"<fmt:message key='common.validate.password.required'/>"
						 }
					 },
					 errorPlacement: function(error, element){
						 var sub="_error_place";
						 var errorPlaceId="#"+$(element).attr("name")+sub;
						 	$(errorPlaceId).html("");
						 	error.appendTo($(errorPlaceId));
					 }
				});
				imageObj = ValidateImage("ValidCodeImage", "<umt:url value='/'/>");
			});
		</script>
	</body>
</html>
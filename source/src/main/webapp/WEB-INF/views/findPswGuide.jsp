<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key="remindpass.title"/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		
		<f:script src="js/ValidateCode.js"/>
		<f:script src="js/string.js"/>
	</head>
	<body class="login">
		<jsp:include flush="true" page="/banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<div class="container login">
			<div class="login-left span8">
				<h2 class="login-title">
					<fmt:message key="remindpass.title"/>
					<span class="sub-title hides"><fmt:message key="remindpass.username.hint"/><!--<fmt:message key="find.password.casMailLogin.hint"/>--></span>  
				</h2>
				<form id="findPswForm" class="form-horizontal" method="post" action="<umt:url value="/findPsw.do?act=submitStepOne"/>"> 
					<div class="control-group">
		              	<label class="control-label" for="password">
							<fmt:message key="remindpass.username"/>
						</label>
		              	<div class="controls">
		               		<input value="${loginEmail }" name="loginEmail" id="loginEmail" type="text"/>
		               		<span class="error help-inline"  id="loginEmail_error_place">
									<c:if test="${!empty loginEmail_error }">
										<fmt:message key="${loginEmail_error }"/>
									</c:if>
								</span>
		               		<!--span class="help-block text-quote"><fmt:message key="remindpass.username.hint"/></span-->
		              	</div>
		            </div>
		            <div class="control-group">
		              	<label class="control-label" for="password">
							<fmt:message key="login.imagetext" />
						</label>
		              	<div class="controls">
		               		<input type="hidden" name="requireValid" value="true" />
							<input id="ValidCode" name="ValidCode" type="text" maxlength="255" class="logininput ValidCode"
								name="ValidCode" 
								message="<fmt:message key="login.imagetext.required"/>" />
							<img id="ValidCodeImage" src="" />
							<a class="small_link" href="#" onclick="imageObj.changeImage();$('#ValidCode').val('');"><fmt:message
									key="login.imagetext.changeit" />
							</a>
							<span id="ValidCode_error_place" class="error help-inline">
								<c:if test="${!empty ValidCode_error }">
									<fmt:message key="${ValidCode_error }"/>
								</c:if>
							</span>
		              	</div>
		            </div>
		            <div class="control-group">
		              	<div class="controls">
		               		<button class="btn long btn-primary" type="submit"><fmt:message key="common.nextStep"/></button>
		              	</div>
		            </div>
			</form>
		</div>
		<div class="login-right span4" style="min-height:400px;">
			<h3 style="margin-top:0;"><fmt:message key='common.duckling.QA'/></h3>
			<p class="sub-gray-text"><fmt:message key='common.duckling.description'/><a href="http://www.escience.cn" target="_blank"><fmt:message key='common.escience'/></a><fmt:message key='common.quote'/><a href="http://ddl.escience.cn" target="_blank"><fmt:message key='common.ddl.escience'/></a><fmt:message key='common.quote'/><a href="http://csp.escience.cn" target="_blank"><fmt:message key='common.csp'/></a><fmt:message key='common.quote'/><a href="http://www.escience.cn/people" target="_blank"><fmt:message key='common.scholarPage'/></a><fmt:message key='common.quote'/><a href="http://mail.escience.cn" target="_blank"><fmt:message key='common.casMail'/></a><fmt:message key='common.duckling.description.more'/></p>
			
			<p><strong><fmt:message key='common.casMailLogin.hint'/></strong></p>  
			<p><strong><fmt:message key='common.duckling.defination'/></strong></p>
		</div>
		</div>
		<jsp:include flush="true" page="/bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
		var imageObj = ValidateImage("ValidCodeImage");
		$(document).ready(function(){
			$('#banner_forgot_password').addClass("active");
			$('#findPswForm').validate({
				 submitHandler:function(form){
					 form.submit();
				 },
				 rules: {
					 loginEmail:{required:true,email:true},
					 ValidCode:{
						 required:true,
						 remote:{
							 type: "GET",
							 url: 'jq/validate.do?act=validateCode',
							 data:{ 
								 "validcode":function(){
										return $('#ValidCode').val();
								}	   		
					  		}
						 }
					 }
				 },
				 messages: {
					 loginEmail: {
						 required:toRed("<fmt:message key='common.validate.email.required'/>"),
						 email:toRed('<fmt:message key="common.validate.email.invalid"/>')
					 },
					 ValidCode:{
						 required:toRed('<fmt:message key="common.validate.validateCode.required"/>'),
						 remote:toRed('<fmt:message key="common.validate.validateCode.wrong"/>')
					 }
					 
				 },
				 errorPlacement: function(error, element){
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			function toRed(str){
				return "<font color='#cc0000'>"+str+'</font>';
			}
		});
		</script>
	</body>
</html>
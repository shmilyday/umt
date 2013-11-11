<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://struts.apache.org/tags-html" prefix="html" %>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='regist.email.title'/></title>
<link href="images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<umt:url value="/css/umt2013-responsive.css"/>" />
</head>
<body class="login">

<jsp:include flush="true" page="banner2013.jsp"></jsp:include>
<f:script src="js/ValidateCode.js"/>
<f:script  src="js/string.js"/>
<div class="container login">
		<div class="login-left span8">
			<html:form action="createRequest?act=saveToEmail" method="post" styleId="createRequestForm" styleClass="form-horizontal">
				<input type="hidden" name="act" value="saveToEmail"/>
				<h2 class="login-title both">
				<fmt:message key='regist.email.title'/>   
				<span class="mail-title"> 
					<fmt:message key='common.duckling'/> 
				</span>
				<a href="<umt:url value='/'/>">
				 <span class="btn btn-success hide768">
				   <fmt:message key='common.regist.signinNow'/>
				 </span>
			 </a>
			</h2>
				<div class="control-group">
	              	<label class="control-label" for="username">
						<span class="ness">*</span><fmt:message key='login.username'/>
					</label>
	              	<div class="controls">
	               		<input type="text" style="width:10em;" id="username" maxlength="255" name="username" tabindex="1"/> 
						<span class="escience-mail"> @ <umt:config key="umt.coremail.api.email.domain"/></span>
						<span id="username_error_place" class="error help-inline">
						<c:if test="${!empty username_error }">
							<fmt:message key="${username_error }"/>
						</c:if>
						</span>
	               		<span class="help-block text-quote">
	               			<a href="${empty isFromApp?'regist.jsp':(isFromApp?registURL:'regist.jsp') }"><fmt:message key='regist.toUmt'/></a>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<label class="control-label" for="truename">
						<span class="ness">*</span><fmt:message key='regist.form.truename'/>
					</label>
	              	<div class="controls">
	               		<input type="text" maxlength="30" name="truename" id="truename" tabindex="2"/>
						<span id="truename_error_place" class="error help-inline">
						<c:if test="${!empty truename_error }">
							<fmt:message key="${truename_error }"/>
						</c:if>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<label class="control-label" for="password">
						<span class="ness">*</span><fmt:message key='regist.form.password'/>
					</label>
	              	<div class="controls">
	               		<input type="password" maxlength="255" name="password" id="password" tabindex="3"/>
						<span id="password_error_place" class="error help-inline">
						<c:if test="${!empty password_error }">
							<fmt:message key="${password_error }"/>
						</c:if>
						</span>
						<span class="help-block text-quote"><fmt:message key='regist.passpord.hint'/></span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<label class="control-label" for="repassword">
						<span class="ness">*</span><fmt:message key='regist.form.retype'/>
					</label>
	              	<div class="controls">
	               		<input type="password" maxlength="255" name="repassword" id="repassword" tabindex="4"/>
						<span id="repassword_error_place" class="error help-inline">
						<c:if test="${!empty repassword_error }">
							<fmt:message key="${repassword_error }"/>
						</c:if>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<label class="control-label" for="tempSecurityEmail">
						<fmt:message key='common.securityEmail'/>
					</label>
	              	<div class="controls">
	               		<input type="text" maxlength="255" name="tempSecurityEmail" id="tempSecurityEmail" tabindex="5"/>
						<span id="tempSecurityEmail_error_place" class="error help-inline">
						<c:if test="${!empty tempSecurityEmail_error }">
							<fmt:message key="${tempSecurityEmail_error }"/>
						</c:if>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<label class="control-label" for="validcode">
						<span class="ness">*</span><fmt:message key='regist.form.imagetext'/>
					</label>
	              	<div class="controls">
	               		<input type="text" class="logininput" style="width:5em;" maxlength="255" id="validcode" name="validcode" tabindex="7"/>
						<img id="ValidCodeImage" src="" />
						<a class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message key='regist.form.changeit'/></a>
						<span id="validcode_error_place" class="error help-inline">
							<c:if test="${!empty validcode_error }">
								<fmt:message key="${validcode_error }"/>
							</c:if>
						</span>
	              	</div>
	            </div>
	            <div class="control-group">
	              	<div class="controls">
	               		<input type="submit" class="btn btn-large btn-primary long" value="<fmt:message key='regist.form.submit'/>" tabindex="8"/>
	              	</div>
	            </div>
			<input type="hidden" name="loginUrl" value="${loginURL}">
			<input type="hidden" name="appName" value="${appName}"/>
		</html:form>
		</div>
			<div class="login-right span4">
				<p class="header">
					<fmt:message key='common.regist.hasDucklingPassport'/>
					<br></br>
					<a href="<umt:url value='/'/>">
					<span class="btn btn-success">
					   		<fmt:message key='common.regist.signinNow'/>
					</span>
					 </a>
				</p>
				<p class="sub-header">
					<fmt:message key="common.login.userOtherName"/>
					<a href="${pageContext.request.contextPath}/thirdParty/login?type=weibo"><img src="images/login/weibo.png" alt="用新浪微博登录" /></a>
					<a href="${pageContext.request.contextPath}/thirdParty/login?type=uaf"><img src="images/login/uaf.png" alt="使用UAF登录" /></a>
				</p>
				<h3><fmt:message key='common.duckling.QA'/></h3>
				<p class="sub-gray-text"><fmt:message key='common.duckling.description'/><a href="http://www.escience.cn" target="_blank"><fmt:message key='common.escience'/></a><fmt:message key='common.quote'/><a href="http://ddl.escience.cn" target="_blank"><fmt:message key='common.ddl.escience'/></a><fmt:message key='common.quote'/><a href="http://csp.escience.cn" target="_blank"><fmt:message key='common.csp'/></a><fmt:message key='common.quote'/><a href="http://www.escience.cn/people" target="_blank"><fmt:message key='common.scholarPage'/></a><fmt:message key='common.quote'/><a href="http://mail.escience.cn" target="_blank"><fmt:message key='common.casMail'/></a><fmt:message key='common.duckling.description.more'/></p>
				<p><strong><fmt:message key='common.duckling.defination'/></strong></p>
				<p><strong><fmt:message key='common.casMailLogin.hint1'/><a href="login"><fmt:message key='regist.login'/></a>  <fmt:message key='common.casMailLogin.hint2'/></strong></p>
			</div>
		<div class="clear"></div>
</div>
<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
<script type="text/javascript">
	var imageObj;
	$(document).ready(function(){
		$(window).on("load",function(){
			$("#truename").val("");
			$("#password").val("");
		});
		
		$('#banner_regist').addClass("active");
		imageObj = ValidateImage("ValidCodeImage");
	    $.validator.addMethod("emailRegex", function(value, element){
	    	var regex = /^[a-zA-Z0-9\-]+$/;
	    	return this.optional(element)||(regex.test(value));
	    }, toRed("<fmt:message key='common.validate.escience.email.invalid'/>"));
	    
	    
	    $.validator.addMethod("canotEualsTo", function(value, element){
	    	return this.optional(element)||$('#username').val()+"@"+'<umt:config key="umt.coremail.api.email.domain"/>'!=value;
	    }, toRed("<fmt:message key='changeSaftyMail.form.validate.DifferentSecurityEmail'/>"));
	    
	    $('#createRequestForm').validate({
			 submitHandler:function(form){
				 form.submit();
			 },
			 rules: {
				 username:{
					 required:true,
					 emailRegex:true,
					 minlength:3,
					 remote:{
						 type: "GET",
						 url: 'createRequest.do',
						 data:{ 
							 'act':'usercheck',
							 "username":function(){
											return $("#username").val();
									    }	   		
				  		}
					 }
			 	 },
				 truename:{required:true},
				 password:{required:true,minlength:8,passwordNotEquals:{
					 notEquals:function(){
						 return $('#username').val()+'@<umt:config key="umt.coremail.api.email.domain"/>';
					 }},
				 passwordAllSmall:true,
				 passwordAllNum:true,
				 passwordAllBig:true,
				 passwordHasSpace:true
				 },
				 repassword:{required:true,equalTo:"#password"},
				 tempSecurityEmail:{email:true,canotEualsTo:true},
				 validcode:{required:true,number:true}
			 },
			 messages: {
				 username: {
					 required:toRed('<fmt:message key="common.validate.email.required"/>'),
					 remote:toRed('<fmt:message key="regist.user.exist"/>'),
					 minlength:"<fmt:message key='input.email.length'/>"
				 },
				 truename:{required:toRed('<fmt:message key="common.validate.truename.required"/>')},
				 password:{
					 required:toRed('<fmt:message key="common.validate.password.required"/>'),
					 minlength:toRed('<fmt:message key="common.validate.password.minlength"/>'),
					 passwordNotEquals:'<fmt:message key="common.validate.password.notEuals"/>',
					 passwordAllSmall:'<fmt:message key="common.validate.password.allSmall"/>',
					 passwordAllNum:'<fmt:message key="common.validate.password.allNumber"/>',
					 passwordAllBig:'<fmt:message key="common.validate.password.allBig"/>',
					 passwordHasSpace:'<fmt:message key="common.validate.password.hasSpace"/>'
				 },
				 repassword:{
					 required:toRed('<fmt:message key="common.validate.repassword.required"/>'),
					 minlength:toRed('<fmt:message key="common.validate.repassword.minlength"/>'),
					 equalTo:toRed('<fmt:message key="common.validate.password.retype.not.equals"/>')
				},
				tempSecurityEmail:{
					required:'<fmt:message key="common.validate.securityEmail.required"/>',
					email:'<fmt:message key="common.validate.email.invalid"/>'
				},
				 validcode:{
					 required:toRed('<fmt:message key="common.validate.validateCode.required"/>'),
					 number:toRed('<fmt:message key="common.validate.validateCode.number"/>')
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
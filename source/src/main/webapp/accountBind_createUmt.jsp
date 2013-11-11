<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<c:if test="${empty sessionScope.thirdParty_type }">
	<jsp:forward page="login"/>
</c:if>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%
	request.setAttribute("msg", request.getParameter("msg"));
%>
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='thirdParty.bind.title'/></title>
		<link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<jsp:include page="accountBind_login.jsp"/>
			<c:set var="isQQ" value="${thirdParty_type=='qq' }"/>
			<c:choose>
			<c:when test="${isQQ }">
			<!-- 注册 -->
			<p class="hasMarign small-font"><strong><fmt:message key='thirdParty.bind.or.create.hint.new'/></strong></p>
			<form action="<umt:url value="/bind.do"/>?act=updateQQInfo" class="form-horizontal" method="post" id="createAndBindForm">
				<input type="hidden" name="needValidCode" value="false"/>
				<input type="hidden" name="screenName" value="${thirdParty_user}"/>
				<input type="hidden" name="openId" value="${thirdParty_openId}"/>
				<input type="hidden" name="type" value="${thirdParty_type }"/>
				<div class="control-group">
		             <label class="control-label" for="username">
						<fmt:message key='common.email'/>
					 </label>
	             	<div class="controls">
	              		<input placeholder="<fmt:message key='login.username.required'/>" type="text" id="username" maxlength="255" value="${loginInfo.user.type=='qq'?'':loginInfo.user.cstnetId }" name="username" tabindex="1"/>
						<span id="username_error_place" class="error help-inline">
							<c:if test="${!empty username_error }">
								<fmt:message key="${username_error}"/>
							</c:if>
						</span>
						<div class="text-quote help-block">
							<a href="accountBind_createCoreMail.jsp">
								<fmt:message key='regist.getMail.start'/>
							</a>
						</div>
	             	</div>
	           </div>
		       <div class="control-group">
		             <label class="control-label" for="truename">
						<span class="ness">*</span><fmt:message key='regist.form.truename'/>
					 </label>
	             	<div class="controls">
	              		<input  type="text" maxlength="30" name="truename" id="truename" value="${empty loginInfo.user?thirdParty_user:loginInfo.user.trueName}" tabindex="2"/>
						<span id="truename_error_place" class="error help-inline">
							<c:if test="${!empty truename_error }">
								<fmt:message key="${truename_error}"/>
							</c:if>
						</span>
						<div class="text-quote help-block">
							<a href="#" onclick="javascript:$('#bind_password').toggle();">
							<fmt:message key='thirdParty.bind.or.create.hint.add.pass'/>
							</a>
						</div>
	             	</div>
	           </div> 
	           <div id="bind_password" style="display:none">
	           <div class="control-group">
		             <label class="control-label" for="password">
						<fmt:message key='regist.form.password'/>
					 </label>
	             	<div class="controls">
	              		<input type="password" maxlength="255" name="password" id="password" tabindex="3"/>
						<span id="password_error_place" class="error help-inline">
							<c:if test="${!empty password_error }">
									<fmt:message key="${password_error}"/>
							</c:if>
						</span>
						<div class="text-quote help-block"><fmt:message key='regist.passpord.hint'/></div>
	             	</div>
	           </div> 
	           <div class="control-group">
		             <label class="control-label" for="repassword">
						<fmt:message key='regist.form.retype'/>
					 </label>
	             	<div class="controls">
	              		<input type="password" maxlength="255" name="repassword"  id="repassword" tabindex="4"/>
						<span id="repassword_error_place" class="error help-inline">
							<c:if test="${!empty repassword_error }">
									<fmt:message key="${repassword_error}"/>
							</c:if>
						</span>
	             	</div>
	           </div>
	           </div>
	           <div class="control-group">
	             	<div class="controls">
	             		<input type="submit" class="btn long btn-primary" value="<fmt:message key='userinfo.submit'/>" tabindex="5"/>
	             	</div>
	           </div>  
		</form>
		</c:when>
		<c:otherwise>
			<p class="hasMarign small-font"><strong><fmt:message key='thirdParty.bind.or.create.hint'/></strong></p>
			<form action="<umt:url value="/bind.do"/>?act=createAndBindUmt" class="form-horizontal" method="post" id="createAndBindForm">
				<input type="hidden" name="needValidCode" value="false"/>
				<input type="hidden" name="screenName" value="${thirdParty_user}"/>
				<input type="hidden" name="openId" value="${thirdParty_openId}"/>
				<input type="hidden" name="type" value="${thirdParty_type }"/>
				<div class="control-group">
		             <label class="control-label" for="username">
						<span class="ness">*</span><fmt:message key='login.username'/>
					 </label>
	             	<div class="controls">
	              		<input placeholder="<fmt:message key='login.username.required'/>" type="text" id="username" maxlength="255" name="username" tabindex="1"/>
						<span id="username_error_place" class="error help-inline">
							<c:if test="${!empty username_error }">
								<fmt:message key="${username_error}"/>
							</c:if>
						</span>
						<div class="text-quote help-block">
							<a href="accountBind_createCoreMail.jsp">
								<fmt:message key='regist.getMail.start'/>
							</a>
						</div>
	             	</div>
	           </div>
		       <div class="control-group">
		             <label class="control-label" for="truename">
						<span class="ness">*</span><fmt:message key='regist.form.truename'/>
					 </label>
	             	<div class="controls">
	              		<input  type="text" maxlength="30" name="truename" id="truename" value="${thirdParty_user}" tabindex="2"/>
						<span id="truename_error_place" class="error help-inline">
							<c:if test="${!empty truename_error }">
								<fmt:message key="${truename_error}"/>
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
									<fmt:message key="${password_error}"/>
							</c:if>
						</span>
						<div class="text-quote help-block"><fmt:message key='regist.passpord.hint'/></div>
	             	</div>
	           </div> 
	           <div class="control-group">
		             <label class="control-label" for="repassword">
						<span class="ness">*</span><fmt:message key='regist.form.retype'/>
					 </label>
	             	<div class="controls">
	              		<input type="password" maxlength="255" name="repassword"  id="repassword" tabindex="4"/>
						<span id="repassword_error_place" class="error help-inline">
							<c:if test="${!empty repassword_error }">
									<fmt:message key="${repassword_error}"/>
							</c:if>
						</span>
	             	</div>
	           </div>
	           <div class="control-group">
	             	<div class="controls">
	             		<input type="submit" class="btn long btn-primary" value="<fmt:message key='regist.form.submit'/>" tabindex="5"/>
	             	</div>
	           </div>  
		</form>
		
		</c:otherwise>
		</c:choose>
		</div>
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
	</body>
	<script>
	$('#createAndBindForm').validate({
		 submitHandler:function(form){
			 form.submit();
		 },
		 rules: {
			 username:{
				 email:true,
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
			 password:{minlength:8,passwordNotEquals:{
				 notEquals:function(){
					 return $('#username').val();
				 }},
				 passwordAllSmall:true,
				 passwordAllNum:true,
				 passwordAllBig:true,
				 passwordHasSpace:true},
			 repassword:{equalTo:"#password"},
			 securityEmail:{required:true,email:true},
			 /*mobilePhone:{number:true,minlength:11},*/
			 validcode:{required:true,number:true}
		 },
		 messages: {
			 username: {
				 required:'<fmt:message key="common.validate.email.required"/>',
				 email:'<fmt:message key="common.validate.email.invalid"/>',
				 remote:'<fmt:message key="regist.user.exist"/>'
			 },
			 truename:{required:'<fmt:message key="common.validate.truename.required"/>'},
			 password:{
				 required:'<fmt:message key="common.validate.password.required"/>',
				 minlength:'<fmt:message key="common.validate.password.minlength"/>',
				 passwordNotEquals:'<fmt:message key="common.validate.password.notEuals"/>',
				 passwordAllSmall:'<fmt:message key="common.validate.password.allSmall"/>',
				 passwordAllNum:'<fmt:message key="common.validate.password.allNumber"/>',
				 passwordAllBig:'<fmt:message key="common.validate.password.allBig"/>',
				 passwordHasSpace:'<fmt:message key="common.validate.password.hasSpace"/>'
			 },
			 repassword:{
				 required:'<fmt:message key="common.validate.repassword.required"/>',
				 minlength:'<fmt:message key="common.validate.repassword.minlength"/>',
				 equalTo:'<fmt:message key="common.validate.password.retype.not.equals"/>'
			 },
			 tempSecurityEmail:{
					required:'<fmt:message key="common.validate.securityEmail.required"/>',
					email:'<fmt:message key="common.validate.email.invalid"/>'
			 },
			 validcode:{
				 required:'<fmt:message key="common.validate.validateCode.required"/>',
				 number:'<fmt:message key="common.validate.validateCode.number"/>'
			 }
			 
		 },
		 errorPlacement: function(error, element){
			 var sub="_error_place";
			 var errorPlaceId="#"+$(element).attr("name")+sub;
			 	$(errorPlaceId).html("");
			 	error.appendTo($(errorPlaceId));
		}
	});
	</script>
</html>
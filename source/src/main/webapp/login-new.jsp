<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html>

<html>
	<head>
		<title><fmt:message key="login.title"/></title>
		<link href="images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<f:script src="js/ValidateCode.js"/>
		<f:script src="js/string.js"/>
		<link rel="stylesheet" type="text/css" href="<umt:url value="/css/umt2013-responsive.css"/>" />
	</head>

	<body class="login" id="login">
		<jsp:include flush="true" page="banner2013-new.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login">
			<div class="login-left span8">
				
				<form class="form-horizontal" id="loginForm" action="login" method="post">
					<umt:SsoParams />
					<h2 class="login-title">
						<fmt:message key='login.title'/> 
						<span class="mail-title"> 	
							<fmt:message key='common.duckling.use'/><fmt:message key='common.login.escience.email'/> 							
						</span>
						<umt:registerLink> 
						   <span class="btn btn-success hide768"><fmt:message key='common.login.registNow'/></span>
						 </umt:registerLink>
					</h2>
					
					<div class="control-group">
						<c:if test="${!empty cookieError }">
					    	<span style="text-align:center" class=" help-block text-quote" id="passwordHint">系统检测到您的登陆IP地址发生了变化，为保证您的账号安全，请重新登录</span>
		              	</c:if>
		              	<label class="control-label" for="username">
							<fmt:message key='login.username'/>
							<input type="hidden" value="Validate" name="act" />
						</label>
		              	<div class="controls">
		               		<input id="username" type="text" 
									placeholder="<fmt:message key='login.username.placeholder'/>"
									message="<fmt:message key="login.username.required"/>"
									maxlength="355" name="username" value="${ username}" />
									<span id="username_error_place" class="error help-inline">
									<c:choose>
										<c:when test="${! empty username_error }">
											<fmt:message key='${username_error}'/>
										</c:when>
										<c:when test="${ !empty WrongPassword}">
											<fmt:message key='login.password.wrong'/>
										</c:when>
									</c:choose>
									</span>
		               		<!-- span class="help-block text-quote"><fmt:message key='login.username.hint'/></span-->
		              	</div>
		            </div>
		            <div class="control-group">
		              	<label class="control-label" for="password">
							<fmt:message key="login.password" />
						</label>
		              	<div class="controls">
		               		<input id="password" type="password" maxlength="355" class="logininput"
									name="password"  placeholder="<fmt:message key='inputpassword.password'/>" />
									
									<span id="password_error_place" class="error help-inline">
										<c:if test="${!empty password_error }">
											<fmt:message key='${password_error }'/>
										</c:if>
									</span>
		               		<span class="help-block text-quote" id="passwordHint"><fmt:message key='common.login.password.hint.duckling'/></span>
		              	</div>
		            </div>
		            <c:if test="${showValidCode!=null || WrongValidCode!=null }">
			             <div class="control-group">
			              	<label class="control-label" for="ValidCode">
								<fmt:message key="login.imagetext" />
							</label>
			              	<div class="controls">
			               		<input type="hidden" name="requireValid" value="true" />
									<input id="ValidCode" style="width:5em;" type="text" maxlength="255" class="logininput ValidCode"
										name="ValidCode" 
										message="<fmt:message key="login.imagetext.required"/>" />
									<img id="ValidCodeImage" src="" />
									<a class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message
											key="login.imagetext.changeit" />
									</a>
									<span id="ValidCode_error_place" class="error help-inline">
									<c:choose>
										<c:when test="${lastErrorValidCode=='error'}">
											<fmt:message key='login.imagetext.wrong'/>
										</c:when>
										<c:when test="${lastErrorValidCode=='required'}">
											<fmt:message key='common.ValidCode.required'/>
										</c:when>
										<c:otherwise>
										</c:otherwise>
									</c:choose>
									</span>
			              	</div>
			            </div>
		            </c:if>
		            <div class="control-group">
		              	<div class="controls small-font">
		              		<input type="checkbox" checked="checked" name="remember" id="remember" value="yes"/>
								<fmt:message key='login.password.rememberme'/>
		               	</div>
		            </div>
		            <div class="control-group">
		              	<div class="controls">
		              		<input type="submit" class="btn btn-large btn-primary long" value="<fmt:message key="login.submit"/>"/>
								<a target="_blank" href="<umt:url value="/findPsw.do?act=stepOne"/>" class="small_link forgetpsw small-font">
									<fmt:message key="login.forgetpassword" />
								</a>
		              	</div>
		            </div>
		            <div class="control-group thirdLogin">
		              	<div class="controls small-font">
		              		<p><fmt:message key='common.login.userOtherName'/>  
		              			<a href="<umt:url value="/thirdParty/login?type=weibo"/>"><img src="images/login/weibo.png" alt="使用新浪微博登录" title="使用新浪微博登录" /></a>
		              			<a href="<umt:url value="/thirdParty/login?type=uaf"/>"><img src="images/login/uaf.png" alt="使用UAF登录" title="使用UAF登录" /></a>
		              			<!-- <a href="<umt:url value="/thirdParty/login?type=cashq"/>"><img src="images/login/cas.png" alt="使用院机关工作平台账号登录" title="使用院机关工作平台账号登录" /></a> -->
							 	<a class="btn cancle bit right" style='display:none'><fmt:message key='common.login.returnBack'/></a>
							 </p>
		              	</div>
		            </div>
				</form>
			</div>
			
			<div class="login-right span4">
				<p class="header">
					<fmt:message key='common.login.noDuckling'/>
					<br></br>
					 <umt:registerLink> 
					   <span class="btn btn-success"><fmt:message key='common.login.registNow'/></span>
					 </umt:registerLink>
				</p>
				<h3><fmt:message key='common.duckling.QA'/></h3>
				<p class="sub-gray-text"><fmt:message key='common.duckling.description'/><a href="http://www.escience.cn" target="_blank"><fmt:message key='common.escience'/></a><fmt:message key='common.quote'/><a href="http://ddl.escience.cn" target="_blank"><fmt:message key='common.ddl.escience'/></a><fmt:message key='common.quote'/><a href="http://csp.escience.cn" target="_blank"><fmt:message key='common.csp'/></a><fmt:message key='common.quote'/><a href="http://www.escience.cn/people" target="_blank"><fmt:message key='common.scholarPage'/></a><fmt:message key='common.quote'/><a href="http://mail.escience.cn" target="_blank"><fmt:message key='common.casMail'/></a><fmt:message key='common.duckling.description.more'/></p>
				<p><strong><fmt:message key='common.duckling.defination'/></strong></p>
				<p><strong><fmt:message key='common.casMailLogin.hint'/></strong></p>
			</div>
			<div class="clear"></div>
		</div>
		
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
			var imageObj = ValidateImage("ValidCodeImage");
		</script>
		<script type="text/javascript">
		$(document).ready(function(){
			/*$('#username').live("blur",function(){
				if($(this).val()==''){
					return;
				}
				$.ajax({
					url:'<umt:url value="/signin/hint.do?act=isCoreMail"/>',
					data:"email="+$(this).val(),
					type:"post",
					success:function(data){
						if(data){
							$('#passwordHint').html("<fmt:message key='common.login.password.hint.casmail'/>");
						}else{
							$('#passwordHint').html("<fmt:message key='common.login.password.hint.duckling'/>");
						}
					}
				});
			});*/
			$('#banner_login').addClass("active");
			$('#loginForm').validate({
				 submitHandler:function(form){
					 form.submit();
				 },
				 rules: {
					 username:{required:true,email:true},
					 password:{required:true/*,minlength:6*/},
					 ValidCode:{required:true,number:true}
				 },
				 messages: {
					 username: {
						 required:toRed("<fmt:message key='common.validate.email.required'/>"),
						 email:toRed('<fmt:message key="common.validate.email.invalid"/>')
					 },
					 password:{
						 required:toRed('<fmt:message key="common.validate.password.required"/>')
						 /*minlength:toRed('<fmt:message key="common.validate.password.minlength"/>')*/
					 },
					 ValidCode:{
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
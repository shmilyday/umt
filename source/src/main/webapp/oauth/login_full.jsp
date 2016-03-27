<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:oauthContext/>
<!DOCTYPE html>
<html>
	<head>
		<title><fmt:message key="login.title"/></title>
		<link href="${context }/images/favicon.ico" rel="shortcut icon"
			type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta name="viewport" content="width=device-width, initial-scale=1" />
		<meta http-equiv="cache-control" content="no-cache" />
		<f:css href="${context }/thirdparty/bootstrap/css/bootstrap.min.css"/>
		<f:css href="${context }/thirdparty/bootstrap/css/bootstrap-responsive.min.css"/>
		<f:css href="${context }/css/umt2013.css"/>
		<f:css href="${context }/css/umt2013-responsive.css"/>
		<f:script  src="${context }/js/jquery-1.7.2.min.js"/>
		<f:script  src="${context }/js/jquery.validate.min.js"/>
		<f:script  src="${context }/js/ext_validate.js"/>
		<f:script  src="${context }/js/menu.js"/>
		<f:script  src="${context }/js/cookie.js"/>
		<f:script  src="${context }/thirdparty/bootstrap/js/bootstrap.min.js"/>
		<f:script  src="${context }/js/jquery.tmpl.min.js"/>
		<f:script src="${context }/js/ValidateCode.js"/>
		<f:script src="${context }/js/string.js"/>
		<f:css href="${context }/css/oauth.css"/>
		<f:script  src="${context }/thirdparty/respond.src.js"/>
	</head>
	<body class="login full" id="login">
		<jsp:include flush="true" page="../banner2013HtmlOnly.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<div class="container login">
			<div class="login-left span8">
				<form class="form-horizontal" id="loginForm" action="authorize?${user_oauth_request.url}" method="post">
					<umt:SsoParams />
					<input type="hidden" id="login" value="${login }">
					<input type="hidden" name="pageinfo" value="userinfo">
					<h2 class="login-title">
						<fmt:message key='login.title'/> 
						<span class="mail-title"> 	
							<fmt:message key="common.duckling.oauth.loginbegin"/>
							<c:choose>
								<c:when test="${! empty client_website }">
								<a class="app_name" href="${client_website}" target="_blank">${client_name }</a>
							</c:when>
							<c:otherwise>
								<a class="app_name" >${client_name }</a>
							</c:otherwise>			
							</c:choose>
							<fmt:message key="common.duckling.oauth.loginend"/>							
						</span>
						<umt:registerLink> 
						   <span class="btn btn-success hide768"><fmt:message key='common.login.registNow'/></span>
						 </umt:registerLink>
					</h2>
					<div class="control-group">
		              	<label class="control-label" for="username">
							<fmt:message key='login.username'/>
							<input type="hidden" value="Validate" name="act" />
						</label>
		              	<div class="controls">
				             <div class="input-prepend">
								<span class="add-on" id="user"></span>
								<input  id="userName" type="text"  tabindex=1
									placeholder="<fmt:message key='login.username.placeholder'/>"
									message="<fmt:message key="login.username.required"/>"
									maxlength="255" name="userName" value="${!empty username?username:autoFill}">
									<span id="username_error_place" class="error help-inline">
										<c:if test="${userNameNull}">
											<fmt:message key="remindpass.username.required"/>
										</c:if>
										<c:if test="${loginerror}">
											<fmt:message key="login.password.wrong"/>
										</c:if>
									</span>
							</div>
		              	</div>
		            </div>
		            <div class="control-group" id="mainDiv">
		              	<label class="control-label" for="password">
							<fmt:message key="login.password" />
						</label>
		              	<div class="controls">
			              	<div class="input-append">
								<span class="add-on preparePopover" data-html='true' data-animation='false' data-trigger="click" data-placement="top" 
								data-content="<a  tabindex='-1' href='${context }/help_https.jsp' target='blank' ><fmt:message key='${isHttps?"oauth.https.hint":"oauth.http.hint" }'/></a>" id="${preSpanId }">
									<a tabindex='-1' target="_blank" href="${context }/help_https.jsp"></a>
								 </span>
								<input id="password" tabindex=2 type="password" maxlength="355" class="logininput"
									name="password"  placeholder="<fmt:message key='inputpassword.password'/>" >
								<span id="password_error_place" class="error help-inline"> 
										<c:if test="${!empty password_error }">
											<fmt:message key='${password_error }'/>
										</c:if>
								</span>
							</div>
							
									<%-- <a class="help-quote hide480" href="<umt:url value="/help.jsp"/>" title="<fmt:message key='common.passwordInputHelp'/>" target="_blank"></a> --%>
									
		               		<span class="help-block text-quote" id="passwordHint"><fmt:message key='common.login.password.hint.duckling'/></span>
		              	</div>
		            </div>
		            
		            
		            <c:if test="${showValidCode}">
		            <div id="validCodeDiv" class="control-group">
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
		            
		           
		            <div class="control-group" style="display:none">
		              	<div class="controls small-font">
		              		<input type="checkbox" name="remember" id="remember" value="yes"/>
								<fmt:message key='login.password.rememberme'/>
							<a class="tendaysHelp" href="<umt:url value="/help_tendays.jsp"/>" target="_blank"></a>
		               	</div>
		            </div>
		           
		            <div class="control-group">
		              	<div class="controls">
		              		<input tabindex=3 type="submit" class="btn btn-large btn-primary long" value="<fmt:message key="login.submit"/>"/>
								<a tabindex=4 target="_blank" href="<umt:url value="/findPsw.do?act=stepOne"/>" class="small_link forgetpsw small-font">
									<fmt:message key="login.forgetpassword" />
								</a>
		              	</div>
		            </div>
		            
		            <umt:showInLoginAuths var="auths">
			            <c:if test="${not empty thirdPartyList || not empty auths}">
				            <div class="control-group thirdLogin">
				              	<div class="controls small-font">
				              		<p><fmt:message key='common.login.userOtherName'/>  
							            <c:if test="${!empty thirdPartyList }">
					              			<c:if test="${!empty thirdPartyList['weibo'] }">
					              				<a tabindex=5 href="${pageContext.request.contextPath}/thirdParty/login?type=weibo"><img src="../images/login/weibo.png" alt="<fmt:message key='oauth.3pt.sina.login'/>" title="<fmt:message key='oauth.3pt.sina.login'/>" /></a>
					              			</c:if>
					              			<c:if test="${!empty thirdPartyList['qq'] }">
												<a  tabindex=6 href="${pageContext.request.contextPath}/thirdParty/login?type=qq"><img src="../images/login/qq.png" alt="<fmt:message key='oauth.3pt.qq.login'/>" title="<fmt:message key='oauth.3pt.qq.login'/>" /></a>
											</c:if>
											<c:if test="${!empty thirdPartyList['cashq'] }">
												<a  tabindex=7 href="${pageContext.request.contextPath}/thirdParty/login?type=cashq"><img src="../images/login/cas.png" alt="<fmt:message key='oauth.coremail.use.cashq'/>" title="<fmt:message key='oauth.coremail.use.cashq'/>" /></a>
											</c:if>
											<c:if test="${!empty thirdPartyList['uaf'] }">
												<a  tabindex=8 href="${pageContext.request.contextPath}/thirdParty/login?type=uaf"><img src="<umt:url value="/images/login/uaf.png"/>" alt="<fmt:message key='oauth.coremail.use.uaf'/>" title="<fmt:message key='oauth.coremail.use.uaf'/>" /></a>
											</c:if>
											<c:if test="${!empty thirdPartyList['geo'] }">
												<a  tabindex=9 href="${pageContext.request.contextPath}/thirdParty/login?type=geo"><img src="<umt:url value="/images/login/cas_geo.png"/>" alt="<fmt:message key='oauth.3pt.geodata.login'/>" title="<fmt:message key='oauth.3pt.geodata.login'/>" /></a>
											</c:if>
							            </c:if>
			              				<c:forEach items="${auths}" var="auth" varStatus="count">
		              						<a class="thirdParty" tabindex="${count.index+10}" href="<umt:url value="/thirdParty/login?type=${auth.code}"/>">${auth.name}</a>
			              				</c:forEach>
									 	<a  tabindex=10 class="btn cancle bit right" style='display:none'><fmt:message key='common.login.returnBack'/></a>
									 </p>
				              	</div>
				            </div>
			            </c:if>
		            </umt:showInLoginAuths>
		            
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
				<p><strong><fmt:message key='common.casMailLogin.hint'/></strong></p>
				<p><strong><fmt:message key='common.duckling.defination'/></strong></p>
				
			</div>
			<div class="clear"></div>
		</div>
		
		<jsp:include flush="true" page="../bottom2013.jsp"></jsp:include>
		<f:script src="${context }/js/oauth.js"/>
		<script type="text/javascript">
		$(document).ready(function(){
			islogin();
			function islogin(){
				var login = $("#login").val();
				if(login=='true'){
					$(".cst-container").hide();
					$(".cst-container-logined").show();
					$("#loginForm").submit();
				}
			};
			$('#username').live("blur",function(){
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
			});
			$('#banner_login').addClass("active");
			$('#loginForm').validate({
				 submitHandler:function(form){
					 var ff = new Object();
						ff.userName=$("input[name='userName']").val();
						ff.password=$("input[name='password']").val();
						ff.clientId=request('client_id');
						ff.clientName='${client_name }';
						ff.pageinfo="checkPassword";
						ff.ValidCode=$("#ValidCode").val();
						$.ajax({
							url:"authorize" ,
							data:ff,
							type:"post",
							dataType:"json", 
							success : function(data){
								if(data.status=='true'){
									form.submit();
									return;
								}
								
								
								
								if(data.showValidCode==true){
									$("#validCodeDiv").remove();
									$('#mainDiv').after($('#codeDiv').tmpl(data));
									
									
									imageObj = ValidateImage("ValidCodeImage","<%= request.getContextPath() %>");
									if(data.status=="validCode.error"){
										return;
									}
								}
								
								 if(data.status=='user.expired'){
									errorMsg='<fmt:message key="login.user.expired"/>';
								}else if(data.status=='user.locked'){
									errorMsg='<fmt:message key="login.user.locked"/>';
								}else if(data.status=='user.stop'){
									errorMsg='<fmt:message key="login.user.stop"/>';
								}else{
									errorMsg='<fmt:message key="login.password.wrong"/>';
								}
								
								$("input[name='userName']").next(".error").html("<label class='error' for='userName' generated='true' style='display: inline;'>"+errorMsg+"</label>");
							}
						});
				 },
				 rules: {
					 userName:{required:true},
					 password:{required:true},
					 ValidCode:{required:true,remote:{
						 type: "GET",
						 url: '/jq/validate.do?act=validateCode',
						 data:{ 
							 "validcode":function(){
									return $('#ValidCode').val();
							}	   		
				  		}
					 }}
				 },
				 messages: {
					 userName: {
						 required:toRed("<fmt:message key='common.validate.email.required'/>"),
						 email:toRed('<fmt:message key="common.validate.email.invalid"/>')
					 },
					 password:{
						 required:toRed('<fmt:message key="common.validate.password.required"/>')
						 /*, minlength:toRed('<fmt:message key="common.validate.password.minlength"/>')*/
					 },
					 ValidCode:{
						 required:toRed('<fmt:message key="common.validate.validateCode.required"/>'),
						 remote:toRed('<fmt:message key="common.validate.validateCode.wrong"/>')
					 }
					 
				 },
				 errorPlacement: function(error, element){
					 /* error.appendTo(element.next('.error')); */
					 var sub="_error_place";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
			});
			respond.update();
		});
		
		var imageObj = ValidateImage("ValidCodeImage","<%= request.getContextPath() %>");
		</script> 
		
<script id="codeDiv" type="text/x-jquery-tmpl" >
{{if showValidCode }}
<div id="validCodeDiv" class="control-group">
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
										{{if lastErrorValidCode=='error'}}
											<fmt:message key='login.imagetext.wrong'/>
										{{/if}}
										{{if lastErrorValidCode=='required'}}
											<fmt:message key='common.ValidCode.required'/>
										{{/if}}
									</span>
			              	</div>
	</div>
{{/if}}
</script>
		
		
	</body>
</html>
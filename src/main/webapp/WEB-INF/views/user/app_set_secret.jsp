<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<fmt:setBundle basename="application" />
<umt:refreshUser/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title>
			<c:choose>
				<c:when test="${op=='updatePwd' }">
					<fmt:message key='app.pwd.reset.title'/>
				</c:when>
				<c:otherwise>
					<fmt:message key='app.pwd.inde.title'/>
				</c:otherwise>
			</c:choose>
		</title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" /> 
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp"></jsp:include>
			<div class="container login gray"> 
			<h2 class="login-title">
			<c:choose>
				<c:when test="${op=='updatePwd' }">
					<fmt:message key='app.pwd.reset.title'/>
				</c:when>
				<c:otherwise>
					<fmt:message key='app.pwd.inde.title'/>
				</c:otherwise>
			</c:choose>
			</h2>
			<form id="secondaryForm" class="form-horizontal" action="<umt:url value="/user/manage.do?act=savePassword"/>" method="post">
				<input type="hidden" name="appId" value="${appId }"/>
				<input type="hidden" name="viewType" value="${viewType }"/>
				<div class="control-group">
	              	<label class="control-label nopadding" >
						<fmt:message key='app.access.pwd.app.name'/>
					</label>
	              	<div class="controls">
	              		<c:choose>
	              				<c:when test="${app.type=='ldap' || app.type=='wifi' }">
	              					${app.data.clientName }
	              				</c:when>
	              				<c:otherwise>
	              					<a href="${app.data.clientWebsite }" target="_blank">${app.data.clientName }</a>
	              				</c:otherwise>
	              		</c:choose>
	              	</div>
	            </div>
				<c:if test="${app.type=='oauth' }">
	            <div class="control-group">
	              	<label class="control-label nopadding">
						<fmt:message key='accountManage.account.title'/>
					</label>
	              	<div class="controls">
	              		<c:forEach items="${canUseLoginName }" var="loginName">
	              			<div>
	              				${loginName.loginName }
	              			</div>
	              		</c:forEach>
	              	</div> 
	            </div>
	            </c:if>
	            <c:if test="${app.type=='ldap'}">   
					<div class="control-group">
		              	<label class="control-label nopadding" for="password">
							<fmt:message key='requests.list.username'/>
						</label>  
		              	<div class="controls">
		              	<c:choose>
		              		<c:when test="${empty nameInfo }">
			              		<input name="ldapName" id="ldapName" value="${ldapNameStr }" type="text"/>
			              		<span id="ldapName_error_place" class="error help-inline">
									<c:if test="${!empty ldapName_error }">
										<fmt:message key="${ldapName_error }"/>
									</c:if>
								</span>
								<div class="help-block text-quote">
									<fmt:message key="ldap.app.set.secret.none.name.hint"></fmt:message>
								</div>
		              		</c:when>
		              		<c:otherwise>
		              			${nameInfo.loginName } 
		              		</c:otherwise>
		              	</c:choose>
							
		              	</div>
		            </div>
	            </c:if>
	            <c:if test="${app.type=='wifi'}">   
					<div class="control-group">
		              	<label class="control-label nopadding" for="password">
							<fmt:message key='requests.list.username'/>
						</label>  
		              	<div class="controls">
		              		${loginName}							
		              	</div>
		            </div>
	            </c:if>	            

	            <div class="control-group">
		              	<label class="control-label nopadding" for="appSecret">
							<fmt:message key='app.pwd.new.inde.password'></fmt:message>
						</label>
		              	<div class="controls">
							<input name="appSecret" maxlength="20" type="password" id="appSecret"/>
		              		<span id="appSecret_error_place" class="error help-inline">
							</span>
							<div class="help-block text-quote">
									<fmt:message key="ldap.app.set.secret.new.secret.hint"></fmt:message>
									<c:if test="${app.type=='wifi'}">
										<br/>
										<fmt:message key="ldap.app.set.secret.modify.hint"/>
									</c:if>
							</div>
		              	</div>
		        </div>
	            <div class="control-group">
		              	<label class="control-label nopadding" for="appSecret_retype">
							<fmt:message key='app.pwd.new.inde.retype'></fmt:message>
						</label>
		              	<div class="controls">
							<input name="appSecret_retype" maxlength="20" type="password" id="appSecret_retype"/>
		              		<span id="appSecret_retype_error_place" class="error help-inline">
							</span>
		              	</div>
		        </div>
		        <div class="control-group">
	              	<label class="control-label nopadding" for="password">
						<fmt:message key="changeSaftyMail.form.passportPassword"/>
					</label>
	              	<div class="controls">
						<input type="password" name="password" id="password"/>
	              		<span id="password_error_place" class="error help-inline">
							<c:if test="${!empty password_error }">
								<fmt:message key="${password_error }"/>
							</c:if>
						</span>																		
	              	</div>
	            </div>
				<div class="control-group">
	              	<div class="controls">
						<input type="hidden" value="${loginInfo.user.cstnetId }" name="loginName"/>
						<button class="btn long btn-primary" type="submit"><fmt:message key="common.confirm"/></button>
	              		<a href="<umt:url value="/user/manage.do?act=appAccessPwd&&viewType=${viewType=='ldap'?'ldap':'oauth' }"/>"><fmt:message key='common.login.returnBack'/></a>
	              	</div>
	            </div>
		</form>
		</div>
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		<script type="text/javascript">
		$(document).ready(function(){
			$.validator.addMethod("notEquals", function(value, element,params){
				return $(params).val()!=value;
			    },"<fmt:message key='app.pwd.validate.not.equals.to.password'/>");
			$('#secondaryForm').validate({
					rules: {
						password:{required:true},
						appSecret:{
							required:true,
							passwordAllSmall:true,
							 passwordAllNum:true,
							 passwordAllBig:true,
							 passwordHasSpace:true	
						},
						appSecret_retype:{
							required:true,
							equalTo:"#appSecret"
						},
						ldapName:{
							 minlength:3,
							 required:true,
							 remote:{
								 type: "GET",
								 url: '<umt:url value="/user/ldap/loginName.do"/>',
								 data:{ 
									 'act':'validateLdapNameUsed',
									 "ldapName":function(){
														return $("#ldapName").val();
													}	   		
						  	 		 }
							 }
						 }
						 
					 },
					 messages: {
						 ldapName:{
							 minlength:'<fmt:message key="app.validate.minlength.three"/>',
							 required:"<fmt:message key='app.validate.new.account.required'/>",
							 remote:'<fmt:message key="app.validate.new.account.used"/>'
						 },
						 password:{
							 required:"<fmt:message key='common.validate.password.required'/>"
						 },
						 appSecret:{
							 required:'<fmt:message key="app.pwd.validate.required"/>',
							 passwordAllSmall:'<fmt:message key="common.validate.password.allSmall"/>',
							 passwordAllNum:'<fmt:message key="common.validate.password.allNumber"/>',
							 passwordAllBig:'<fmt:message key="common.validate.password.allBig"/>',
							 passwordHasSpace:'<fmt:message key="common.validate.password.hasSpace"/>'	 
						 },
						 appSecret_retype:{
							 required:'<fmt:message key="app.pwd.validate.required"/>',
							 equalTo:'<fmt:message key="app.pwd.validate.equals"/>'
						 }
					 },
					 errorPlacement: function(error, element){
						 var sub="_error_place";
						 var errorPlaceId="#"+$(element).attr("name")+sub;
						 	$(errorPlaceId).html("");
						 	error.appendTo($(errorPlaceId));
					 }
				});
		});
		</script>
	</body>
</html>
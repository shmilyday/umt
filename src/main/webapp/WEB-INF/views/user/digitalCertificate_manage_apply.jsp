<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   
 <%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<%
        pageContext.setAttribute("contextPath", request.getContextPath());
%>
<umt:AppList/>
<umt:refreshUser/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='banner.digitalCertificateSetting'/></title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<f:script  src="${contextPath }/thirdparty/jquery.jeditable.js"/>
		<form id="applyForm" action="<umt:url value="/user/digitalCertificate.do?act=apply"/>" method="post">
		
			<div class="container login gray">
				<ul class="sub-nav">
					<li id="accountManage"><a href="<umt:url value="/user/digitalCertificate.do?act=index"/>"><fmt:message key='digitialManage.title.digitialManage' /></a></li>
					<li class="active" id="accountApp"><a href="<umt:url value="/user/digitalCertificate.do?act=manage"/>"><fmt:message key='digitialManage.title.manage' /></a></li>
					<%-- <li id="accountBind"><a href="<umt:url value="/user/digitalCertificate.do?act=record"/>">登录记录</a></li> --%>
					<li id="accountBind"><a href="<umt:url value="/user/digitalCertificate.do?act=help"/>"><fmt:message key='digitialManage.title.help' /></a></li>
					<div class="clear"/>
				</ul>
				<div class="sub-content" id="accountManageShow">
					<h4 class="sub-title"><fmt:message key='digitialManage.manage.applay.title' /></h4>
					<p class="sub-text blackfont"><strong><fmt:message key='digitialManage.manage.applay.title.type' /></strong></p>
					<p class="sub-text blackfont"><fmt:message key="digitialManage.manage.type.eduroma"/></p>
					<p class="sub-text blackfont"><strong><fmt:message key='digitialManage.manage.applay.title.dn' /></strong></p>
					<p class="sub-text blackfont">${dn }</p>
					<p class="sub-text blackfont"><strong><fmt:message key='digitialManage.manage.applay.title.password' /></strong></p>
					<div>
					
						<p class="sub-text blackfont" id="passwordShow"><label id="passwordText">${randPassword }</label><span class="EDITpassword"><a class="" id="editPassword"><fmt:message key='digitialManage.manage.applay.op.update' /></a></span></p>
						<p class="sub-text blackfont" id="passwordInput" style="display:none"><input id="textPassword" type="text" value="${randPassword }" name="password"/><span id="password_error_place" class="error help-inline"></span><span><a class="" id="savePassword"><fmt:message key='digitialManage.manage.applay.op.save' /></a></span></p>
						<span class="help-block text-quote" id="passwordHint" ><font color="red"><fmt:message key="digitialManage.manage.applay.totip.password"/></font></span>
					</div>
					<%-- /<button class="btn long btn-primary">申请</button> --%>
					<p class="sub-text"><input type="submit" id="applyBtn" class="btn long btn-primary" value="<fmt:message key='digitialManage.manage.applay.op.apply' />"/></p>
				</div>
			</div>
			
			<input name="dn" value="${dn }" type="hidden" />
			<input name="cn" value="${cn }" type="hidden" />
			
		</form>
		
		
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		<script type="text/javascript" >
			function confirmDelete(obj){
				var result=confirm("<fmt:message key='accountManage.secondaryMail.delete'/>");
				if(result){
					window.location.href=$(obj).attr("url");
				}
			}
			function confirmDeleteLdap(obj){
				var result=confirm("<fmt:message key='account.manage.delete.ldap'/>");
				if(result){
					window.location.href=$(obj).attr("url");
				}
				
			}
			
			
			
			
			
			$(document).ready(function(){
				
				var test=$('#applyForm').validate({
					rules:{
						password:{required:true,minlength:6}
					},
					messages:{
						password:{required:'<fmt:message key="common.validate.password.required"/>',minlength:'<fmt:message key="digitialManage.manage.applay.minlength"/>'}
					},
					errorPlacement: function(error, element){
						 var sub="_error_place";
						 var errorPlaceId="#"+$(element).attr("name")+sub;
						 	$(errorPlaceId).html("");
						 	error.appendTo($(errorPlaceId));
					}
				
				});	
				
				$('#banner_user_digitalCertificate').addClass("active");
				$("#closeTooltip").live("click",function(){
					$(this).parent().parent().hide();
				});
				
				
				$("#editPassword").live('click',function(){
					$(this).parents("p#passwordShow").hide();
					$("#passwordInput").show();
					$("#applyBtn").attr("disabled",true);
				}); 
				
				
			/* 	$("#textPassword").on("change",function(){
					$("#password_error_place").hide();
					var pass=$(this).val();
					if(pass==null||pass==''||pass.length<6){
						$("#password_error_place").show();
						return;
					}
					$("#password_error_place").hide();
					
				}); */
				
				$("#savePassword").on('click',function(){
					
					if(!test.form()){
						return;
					}
					$(this).parents("p#passwordInput").hide();
					$("#passwordText").text($(this).parents("p#passwordInput").find("input:first").val());
					$("#passwordShow").show();
					$("#applyBtn").attr("disabled",false);
					
				}); 
				
				
			});
			
			
		</script>
	</body>
</html>
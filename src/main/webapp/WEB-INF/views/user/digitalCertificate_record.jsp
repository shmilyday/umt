<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   
<fmt:setBundle basename="application" />
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
		
		<div class="container login gray">
			<ul class="sub-nav">
				<li id="accountManage"><a href="<umt:url value="/user/digitalCertificate.do?act=index"/>"><fmt:message key='digitialManage.title.digitialManage' /></a></li>
				<li id="accountApp"><a href="<umt:url value="/user/digitalCertificate.do?act=manage"/>"><fmt:message key='digitialManage.title.manage' /></a></li>
				<li id="accountBind"><a href="<umt:url value="/user/digitalCertificate.do?act=help"/>"><fmt:message key='digitialManage.title.help' /></a></li>
				<div class="clear"/>
			</ul>
			<div class="sub-content" id="accountManageShow">
			
				<table class="saftyList digital_manage">
				<thead>
					<tr>
						<td>登录时间</td>
						<td>内容</td>
						
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>2015-06-26 13:09:09</td>
						<td>CN=73751b51668b11e5931e402cf466ef69,DC=CSTCloud, DC=Grid, DC=CN</td>
					</tr>
					<tr>
						<td>2015-06-26 13:09:09</td>
						<td>CN=73751b51668b11e5931e402cf466ef69,DC=CSTCloud, DC=Grid, DC=CN</td>
						
					</tr>
				</tbody>
				</table>
				
				
				
				
				
			</div>
		</div>
		
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
				$('#banner_user_digitalCertificate').addClass("active");
				$("#closeTooltip").live("click",function(){
					$(this).parent().parent().hide();
				});
			});
			
			
			//Add User Name buttion click
			$('#modifyAddUserName').on('click',function(){
				$('#id').val('0');
				$('#applicant').val('');
				$('#clientName').val('');
				$('#clientWebsite').val('');
				$('#company').val('');
				$('#contactInfo').val('');
				$('#description').val('');
				$('#redirectURI').val('');
				$('#act').val('addOauth');
				$('input[name=appType][value=webapp]').click();
				$('#thirdParty').val('');
				$('.error').html("");
				$('#modalTitle').html('<fmt:message key="develop.oauth.modify.title.add"/>');
				$('#add-UserName').modal('show');
			});
		</script>
	</body>
</html>
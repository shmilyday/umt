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
		<style>
			.btn.btn-link {font-size:12px; padding:0 4px; margin:0}
		</style>
	</head>
	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<li id="accountManage"><a href="<umt:url value="/user/digitalCertificate.do?act=index"/>"><fmt:message key='digitialManage.title.digitialManage' /></a></li>
				<li class="active" id="accountApp"><a href="<umt:url value="/user/digitalCertificate.do?act=manage"/>"><fmt:message key='digitialManage.title.manage' /></a></li>
				<li id="accountBind"><a href="<umt:url value="/user/digitalCertificate.do?act=help"/>"><fmt:message key='digitialManage.title.help' /></a></li>
				<div class="clear"/>
			</ul>
			<div class="sub-content" id="accountManageShow">
			<h4 class="sub-title"><fmt:message key='digitialManage.manage.table.title' /></h4>
				<c:if test="${not empty caList }">
				<table class="saftyList digital_manage width">
				<thead>
					<tr>
						<td><fmt:message key="digitialManage.manage.table.dn"/></td>
						<td><fmt:message key="digitialManage.manage.table.type"/></td>
						<td><fmt:message key="digitialManage.manage.table.validFrom"/></td>
						<td><fmt:message key="digitialManage.manage.table.expiredTime"/></td>
						<td><fmt:message key="digitialManage.manage.table.status"/></td>
						<td><fmt:message key="digitialManage.manage.table.op"/></td>
					</tr>
				</thead>
				<tbody>
					<c:forEach items="${caList }" var="ca" varStatus="status">
						<tr>
							<td>${ca.dn }</td>
							<td>
								<c:choose>
									<c:when test="${ca.type==1 }"><fmt:message key="digitialManage.manage.type.eduroma"/></c:when>
									<c:otherwise></c:otherwise>
								</c:choose>
							</td>
							<td>${ca.valiFrom }</td>
							<td>${ca.expirationOn }</td>
							<td>
								<c:choose>
									<c:when test="${ca.status==1 }"><fmt:message key="digitialManage.manage.status.valid"/></c:when>
									<c:when test="${ca.status==3 }"><fmt:message key="digitialManage.manage.status.expired"/></c:when>
									<c:otherwise><fmt:message key="digitialManage.manage.status.deleted"/></c:otherwise>
								</c:choose>
							</td>
							<td>
								<a class=" btn btn-link" href="<umt:url value="/user/digitalCertificate.do?act=downloadView"/>&caId=${ca.id}"><fmt:message key="digitialManage.manage.op.download"/></a>
								<a class=" btn btn-link remove" url="<umt:url value="/user/digitalCertificate.do?act=remove"/>&caId=${ca.id}"><fmt:message key="digitialManage.manage.op.delete"/></a>
								<a class=" btn btn-link viewPass"  caId="${ca.id }" url="javascript:void(0);return"><fmt:message key='digitialManage.manage.op.viewPass'/></a></td>
						
						</tr>
						
					</c:forEach>
				</tbody>
				
				
				</table>
				<button class="btn long btn-primary" id="applyDigital" style="margin-left:60px;"><fmt:message key="digitialManage.btn.apply.new"/></button>
				</c:if>
				
				<c:if test="${empty caList }">
					<p class="sub-text"><fmt:message key="digitialManage.index.applay.empty"/></p>
					<button class="btn long btn-primary" id="applyDigital" style="margin-left:60px;"><fmt:message key='digitialManage.btn.apply'/></button>
				</c:if>
				
			</div>
		</div>
		
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		<script type="text/javascript" >
			$(document).ready(function(){
				$('#banner_user_digitalCertificate').addClass("active");
				$("#closeTooltip").live("click",function(){
					$(this).parent().parent().hide();
				});
				
				$(".remove").live("click",function(){
					var result=confirm("<fmt:message key='digitialManage.manage.op.confirm'/>");
					if(result){
						window.location.href=$(this).attr("url");
					}
				});
				
				$("#getPassForm").validate({
					submitHandler: function(){
						$.ajax({
							url:"/user/digitalCertificate.do" ,
							data:$("#getPassForm").serialize(),
							type:"post",
							dataType:"json", 
							success : function(data){
								if(data.result=='error'){
									$("#password_error_place").append('<label for="password" generated="true" class="error" style="display: block;"><fmt:message key="digitialManage.manage.error.other"/></label>');
								}else if(data.result=='password_error'){
									$("#password_error_place").append('<label for="password" generated="true" class="error" style="display: block;"><fmt:message key="digitialManage.manage.error.password"/></label>');
								}else if(data.result=='true'){
									$("#passTr").show();
									$("#caPass").text(data.password);
								}
							}
						});
						
						return false;
					},
					rules:{
						password:{required:true},
					},
					messages:{
						password:{required:"<fmt:message key='common.validate.password.required'/>"}
					},
					errorPlacement:function(error, element){
						var sub="_error_place";
						 var errorPlaceId="#"+$(element).attr("name")+sub;
						 	$(errorPlaceId).html("");
						 	error.appendTo($(errorPlaceId));
					}
				});
				
			$("#applyDigital").on("click",function(){
				window.location.href='<umt:url value="/user/digitalCertificate.do?act=applyView"/>';
			});
			
			$(".viewPass").on("click",function(){
				$("#passTr").hide();
				$("#caPass").text("");
				$("#cstPass").val("");
				$('#viewpassworddialog').modal("show"); 
				$('#viewpassworddialog #caId').val($(this).attr("caId"));
			});
				
	});
			
			
			
		
			
			
			
		</script>
	</body>
</html>




<div id="viewpassworddialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
			<h4 id="operateTitle"><fmt:message key='digitialManage.manage.op.viewPass'/></h4>
		</div>
		<form id="getPassForm" name="getPassForm" method="post" action="">
		<input type="hidden" name="act" value="getPass" /> 
		<input type="hidden" id="caId" name="caId" value="" />
		<div class="modal-body">
			<p id="content">
			<table>
			<tr>
				<td class="rtl">
				    <fmt:message key="digitialManage.manage.input.password.title"/>
				</td>
				<td><input id="cstPass" type="password" name="password"  />
				</td>
				<td>
				<span id="password_error_place" class="error help-inline">
				</span>
				</td>
			</tr>
			<tr id="passTr">
				<td class="rtl">
				  <fmt:message key="digitialManage.manage.input.private.key.title"/>
				</td>
				<td>
					<label id="caPass"></label>
				</td>
				<td>
				<span id="password_error_place" class="error help-inline">
				</span>
				</td>
			</tr>
		</table>
			</p>
		</div>
		<div class="modal-footer">
			<input type="submit" class="btn btn-primary"  value="<fmt:message key="common.submit"/>"/>
			<a class="btn" data-dismiss="modal" aria-hidden="true" id="no"> <fmt:message key="inputpassword.cancel"/></a>
		</div>
		</form>
</div>



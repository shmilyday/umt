<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:setBundle basename="application" />	
<div class="content-container" >
<div class="toolBar">
<form id="formId" class="form-search" style="float:right;margin:0" method="post" action="<umt:url value="/admin/accessIps.do?act=add"/>" >
<input name="ip" id="ip" value="" type="text" />
<button type="submit"  class="btn  btn-primary" style="margin-top:-6px;"><fmt:message key="common.add"/></button>
</form>
</div>
<div class="userTable" style="height:515px;">
<table class="table table-hover" id="sessions"  border="0">
							<tbody><tr class="top">
								<td>
									IP
								</td>
								<td>
									<fmt:message key="admin.oauth.operation"/>
								</td>
							</tr>
							<c:forEach items="${ips }" var="ip">
									<tr class="firstline ">
										<td>
											${ip.ip }
										</td>
										<td>
											<a href="#" onclick="deleteIp('${ip.id}')" ><fmt:message key="common.delete"/></a>
										</td>
									</tr>
							</c:forEach>
						</tbody></table>
						</div>
</div>
<script type="text/javascript" src="<umt:url value="/js/jquery.validate.min.js"/>"></script>
<script>
function deleteIp(ipId){
	var result=confirm("<fmt:message key='validate.access.ip.confirm'/>");
	if(result){
		window.location.href='<umt:url value="/admin/accessIps.do?act=delete&ipId="/>'+ipId;
	};
}
$(document).ready(function(){
	$('#formId').validate({
		 submitHandler:function(form){
			 form.submit();
		 },
		 rules: {
			 ip:{required:true}
		 },
		 messages: {
				 ip: {
					required:'<fmt:message key="validate.access.ip.required"/>'
			 }
		 }
	});
	$(".nav li.current").removeClass("current");
	$("#accessIpMenu").addClass("current");
});
</script>
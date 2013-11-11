<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<fmt:setBundle basename="application" />	

<div class="content-container">
<%--
<form id="formId" method="post" action="<umt:url value="/admin/addClient?act=add"/>" >
<c:if test="${!empty message }">
<p style="color:red">${message }</p>
</c:if>
client_id:<input name="client_id" value="${client_id }" size="10"/>
cleint_secret:<input name="cleint_secret" value="${cleint_secret }" size="10"/>
client_name:<input name="client_name" value="${client_name }" size="10">
third_part:<input name="third_party" value="${third_party }">
redirect_URI:<input name="redirect_URI" value="${redirect_URI}"/>
client_website:<input name="client_website" value="${client_website}"/>
<input type="submit" value="增加"/>
</form>
--%>
<div class="toolBar" >
<button type="button" id="addClient" class="btn btn-primary" style="float:right;margin:0 10px 0 0;" ><fmt:message key="admin.oauth.add"/></button>
</div>
<div class="userTable" style="height:527px;">
   <table class="table table-hover" style="word-wrap :break-word; table-layout:fixed; " id="sessions" border="0">
							<tbody><tr class="top">
								<td>
									App Key
								</td>
								<td width="40%">
									App secret
								</td>
								<td>
									<fmt:message key='develop.oauth.modify.appType'></fmt:message>
								</td>
								<td>
									<fmt:message key="admin.oauth.name"/>
								</td>
								<td>
									<fmt:message key="admin.oauth.status"/>
								</td>
								
								<td width="20%">
									 <fmt:message key="admin.oauth.operation"/>
								</td>
							</tr>
							<c:forEach items="${clients }" var="client">
							<tr class="firstline ">
								<td>
									${client.clientId }
								</td>
								<td title="${client.clientSecret }">
									${client.clientSecret }
								</td>
								<td>
									<c:choose>
										<c:when test="${client.appType=='webapp' }"><fmt:message key='develop.oauth.modify.appType.webapp'/></c:when>
										<c:when test="${client.appType=='phoneapp' }"><fmt:message key='develop.oauth.modify.appType.phoneapp'/></c:when>
									</c:choose>
								</td>
								<td title="${client.clientName}">
									${client.clientName}
								</td>
								<!-- c
								<td title="${client.redirectURI }">
									${client.redirectURI }
								</td>
								<td title="${client.thirdParty }">
									${client.thirdParty }
								</td>
								<td title="${client.clientWebsite }">
									${client.clientWebsite }
								</td> -->
								<td>
									${client.status }
								</td>
								<!-- <td>
									${client.description}
								</td>
								<td>
									${client.applicant}
								</td>
								<td>
								${client.company}
								</td>
								<td>
								${client.contactInfo}
								</td>
								 -->  
								<td width="20%">
								<a onclick="refreshClient('${client.id}')"><fmt:message key="common.refresh"/></a>
								<a onclick="updateClient('${client.clientId}')"><fmt:message key="common.update"/></a >
								<a onclick="deleteIp('${client.id}')" ><fmt:message key="common.delete"/></a>
								<a data-client-id="${client.clientId }" 
								data-client-secret="${client.clientSecret }"
								data-client-name="${client.clientName}"
								data-redirect-URI="${client.redirectURI }"
								data-third-party="${client.thirdParty }"
								data-status="${client.status }"
								data-description="${client.description}"
								data-applicant="${client.applicant}"
								data-company="${client.company}"
								data-client-website="${client.clientWebsite }"
								data-contact-info="${client.contactInfo}" onclick="detailClient(this)"><fmt:message key="common.detail"/></a>
								</td>
							</tr>
							</c:forEach>
						</tbody></table>
</div>
</div>
<div id="detailDialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
			<h4><fmt:message key="common.detail"/></h4>
		</div>
		<div class="modal-body">
			<p id="content">
		<input type="hidden" id="operateType" name="type"/>
		<table>
			<tr>
				<td>App Key:</td><td id="client_id"></td>
			</tr>
			<tr>
				<td>App Secret:</td><td id="client_secret"></td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.modify.clientName"/></td><td id="client_name"></td>
			</tr>
			<tr>
				<td><fmt:message key="admin.oauth.thirdParty"/></td><td id="third_party"></td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.redirect.url"/></td><td id="redirect_URI"></td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.website"/></td><td id="client_website"></td>
			</tr>
			<tr>
				<td><fmt:message key="admin.oauth.status"/><fmt:message key="common.maohao"/></td>
				<td id="client_status">
				</td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.description"/></td>
				<td id="descript">
				</td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.modify.applicant"/></td>
				<td id="applicant">
				</td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.modify.company"/></td>
				<td id="company">
				</td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.modify.contactInfo"/></td>
				<td id="contactInfo">
				</td>
			</tr>
		</table>
		</div>
		<div class="modal-footer">
			<a class="btn" data-dismiss="modal" aria-hidden="true" id="no"><fmt:message key="common.cancel"/></a>
		</div>
</div>


<div id="updateDialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
			<h4 id="operateTitle"></h4>
		</div>
		<form id="oauthClientForm" method="post">
		<input type="hidden" id="operateType" name="type"/>
		<div class="modal-body">
			<p id="content">
		<table>
			<tr>
				<td>App Key:</td><td><input  name="client_id"  size="10"/></td>
			</tr>
			<tr>
				<td>App Secret:</td><td><input name="client_secret" id="client_secret"/></td>
			</tr>
			<tr>
				<td><fmt:message key='develop.oauth.modify.appType'/><fmt:message key='common.maohao'/></td>
				<td> 
					<input type="radio" checked="checked" name="app_type" value="webapp" id="app_type_web"/><label style="display:inline;" for="app_type_web"><fmt:message key='develop.oauth.modify.appType.webapp'/></label>
					<input type="radio" name="app_type" value="phoneapp" id="app_type_phone"/><label style="display:inline;" for="app_type_phone"><fmt:message key='develop.oauth.modify.appType.phoneapp'/></label>
				</td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.modify.clientName"/></td><td><input name="client_name" ></td> 
			</tr>
			<tr>
				<td><fmt:message key="admin.oauth.thirdParty"/></td><td><input name="third_party" ></td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.redirect.url"/></td><td><input name="redirect_URI" id="redirect_URI"/></td>
			</tr>
			<tr>
				<td><fmt:message key="develop.oauth.website"/></td><td><input name="client_website" /></td>
			</tr>
			<tr>
				<td><fmt:message key="admin.oauth.status"/><fmt:message key="common.maohao"/></td>
				<td>
					<select name="client_status">
						<option value="accept">accept</option>
						<option value="apply">apply</option>
						<option value="refuse">refuse</option>
					</select>
				</td>
			</tr>
		</table>
		</div>
		<div class="modal-footer">
			<input type="submit" class="btn btn-primary"  value="<fmt:message key="common.submit"/>">
			<a class="btn" data-dismiss="modal" aria-hidden="true" id="no"> <fmt:message key="common.cancel"/></a>
		</div>
		</form>
</div>
<div style="display: none;position: absolute;z-index: 999;top: 10%;left: 30%;right:30%;background-color: #fff;border: 5px solid #888;box-shadow: 0 3px 7px rgba(0,0,0,0.8);height:300px;" class="oauthClientOperate">
	<a class="closeOperate" style="float: right;background: #08a;color: #fff;font-weight: bold;padding: 5px;">X</a>
	<div class="operateMessage"><p style="color:red" class="messageShow"></p></div>
	<h4 id="operateTitle" style="font-size: large;"></h4>
	<div>
		
	</div>
</div>
<script type="text/javascript" src="<umt:url value="/js/jquery.validate.min.js"/>"></script>
<script type="text/javascript" src="<umt:url value="/thirdparty/bootstrap/js/bootstrap.min.js"/>"></script>
<script>
function deleteIp(ipId){
	var result=confirm("<fmt:message key='admin.confirm.delete'/>");
	if(result){
		window.location.href='<umt:url value="/admin/addClient?act=delete&id="/>'+ipId;
	};
}
function refreshClient(ipId){
	window.location.href='<umt:url value="/admin/addClient?act=refresh&id="/>'+ipId;
}
function detailClient(obj){ 
	$("#detailDialog").modal('show');
	
	var data=$(obj).data();
	$("#client_id").html(data.clientId);
	$("#client_secret").html(data.clientSecret);
	$("#client_name").html(data.clientName);
	$("#third_party").html(data.thirdParty);
	$("#redirect_URI").html(data.redirectUri);
	$("#client_website").html(data.clientWebsite);
	$("#client_status").html(data.status);
	$("#descript").html(data.description);
	$("#applicant").html(data.applicant);
	$("#company").html(data.company);
	$("#contactInfo").html(data.contactInfo);
	
}
function updateClient(clientId){
	$.ajax({
		url:'<umt:url value="/admin/addClient?act=getClientInfo&client_id="/>'+clientId,
		type:'post',
		dataType:'json',
		success:function(data){
			if(data.result){
				$("#operateType").val("update");
				$("td input[name='client_id']").val(data.client_id);
				$("td input[name='client_secret']").val(data.client_secret);
				$("td input[name='client_name']").val(data.client_name);
				$("td input[name='third_party']").val(data.third_party);
				$("td input[name='redirect_URI']").val(data.redirect_URI);
				$("td input[name='client_website']").val(data.client_website);
				$("td input[name='client_id']").attr("readonly","true");
				$("td input[name='app_type'][value='"+data.app_type+"']").click();
				$("td select[name='client_status']").val(data.client_status);
				$("#operateTitle").html("<fmt:message key='common.modify'/>");
				$("#updateDialog").modal("show"); 
			}else{
				alert(data.message);
			}
		}
	});
}

$(document).ready(function(){
	$('#formId').validate({
		 submitHandler:function(form){
			 form.submit();
		 },
		 rules: {
			 client_id:{required:true},
			 
		 },
		 messages: {
			 client_id: {
				 required:function(){alert('<fmt:message key="addclient.validate.appkey.required"/>');}
			 }
		 }
	});
	$("#addClient").live('click',function(){
		$("#operateType").val("add");
		$("td input[name='client_id']").removeAttr("readonly");
		$("#operateTitle").html("<fmt:message key='common.add'/>");
		$('#oauthClientForm').get(0).reset();
		$("#updateDialog").modal("show"); 
	});
	$(".closeOperate").live("click",function(){
		$("#oauthClientForm")[0].reset();
		$(".messageShow").html();
		$("#operateTitle").html();
		$(".oauthClientOperate").hide();
	});
	$("#oauthClientForm").validate({
		submitHandler:function(form){
			var type = $("#operateType").val();
			var opurl ='';
			if(type=='add'){
				opurl='<umt:url value="/admin/addClient?act=add"/>';
			}else if(type=='update'){
				opurl='<umt:url value="/admin/addClient?act=update"/>';
			}else{
				alert("操作类型不正确");
				return;
			}
			$("#oauthClientForm").attr('action',opurl);
			var d = $("#oauthClientForm").serialize();
			$.ajax({
				url:opurl,
				type:'post',
				data:d,
				dataType:'json',
				success:function(data){
					if(data.result){
						window.location.href='<umt:url value="/admin/addClient?act=setParameter"/>'; 
					}else{
						$(".messageShow").html(data.message);
					}
				}
			});
			return;
		 },
		 rules: {
			 client_id:{required:true, 
				 remote:{
				 type: "post",
				 url: '<umt:url value="/admin/addClient?act=clientIdUsed"/>',
				 data:{"type":function(){return $("#operateType").val();}}
			 }},
			 client_secret:{required:true},
			 redirect_URI:{required:true} 
		 },
		 messages: {
			 client_id: {
				 required:'<fmt:message key="addclient.validate.appkey.required"/>',
				 remote:'<fmt:message key="addclient.validate.appkey.used"/>'
			 },
			 client_secret:{required:'<fmt:message key="addclient.validate.appsercret.required"/>'},
			 redirect_URI:{required:'<fmt:message key="addclient.validate.redirecturl.required"/>'}
		 }	
	});
	$(".nav li.current").removeClass("current");
	$("#oauthMenu").addClass("current");
});
</script>
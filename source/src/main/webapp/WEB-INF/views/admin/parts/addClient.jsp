<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<fmt:setBundle basename="application" />	

<div class="content-container">
<div class="toolBar" >
<button type="button" id="addClient" class="btn btn-primary" style="float:right;margin:0 10px 0 0;" ><fmt:message key="admin.oauth.add"/></button>
</div>
<div class="userTable" style="height:527px;">
   <table class="table table-hover" style="word-wrap :break-word; table-layout:fixed; " id="sessions" border="0">
							<tbody><tr class="top">
								<td width="30px">#</td>
								<td width="20%">
									<fmt:message key="admin.oauth.name"/>
								</td>
								<td>
									<fmt:message key='develop.oauth.modify.appType'></fmt:message>
								</td>
								<td>
									App Key
								</td>
								<td>
									<fmt:message key="admin.oauth.status"/>
								</td>
								<td  width="20%"  >
									<fmt:message key="admin.oauth.submit.time"/>
								</td>
								<td width="20%">
									 <fmt:message key="admin.oauth.operation"/>
								</td>
							</tr>
							<c:forEach items="${clients }" varStatus="status" var="client">
							<tr class="firstline ">
								<td>${status.index+1 }</td>
								<td title="${client.clientName}">
									${client.clientName}
								</td> 
								<td>
									<c:choose>
										<c:when test="${client.appType=='webapp' }"><fmt:message key='develop.oauth.modify.appType.webapp'/></c:when>
										<c:when test="${client.appType=='phoneapp' }"><fmt:message key='develop.oauth.modify.appType.phoneapp'/></c:when>
									</c:choose>
								</td>
								<td>
									${client.clientId }
								</td>
								<td>
								<c:choose>
									<c:when test="${client.status=='apply' }">
										<fmt:message key='admin.oauth.status.apply'/>
									</c:when>
									<c:when test="${client.status=='accept' }">
										<fmt:message key='admin.oauth.status.accept'/>
									</c:when>
									<c:when test="${client.status=='refuse' }">
										<fmt:message key='admin.oauth.status.refuse'/>
									</c:when>
								</c:choose>
								</td>
								<td>
								<fmt:formatDate value="${client.applicationTime }" pattern="yyyy-MM-dd HH:mm:ss"/>
								</td>
								<td width="20%">
								<a onclick="refreshClient('${client.id}')"><fmt:message key="common.refresh"/></a>
								<a onclick="updateClient('${client.clientId}')"><fmt:message key="common.update"/></a >
								<a onclick="deleteIp('${client.id}')" ><fmt:message key="common.delete"/></a>
								<a
								data-id="${client.id }"
								data-client-id="${client.clientId }" 
								data-client-secret="${client.clientSecret }"
								data-client-name="${client.clientName}"
								data-redirect-URI="${client.redirectURI }"
								data-third-party="${client.thirdParty }"
								data-status="${client.status }"
								data-description="${client.description}"
								data-applicant="${client.applicant}"
								data-company="${client.company}"
								data-client-website="${client.clientWebsite }"
								data-contact-info="${client.contactInfo}"
								data-creator-email="${client.userName }"
								data-need-org-info="${client.needOrgInfo }"
								data-applicant-time="<fmt:formatDate value="${client.applicationTime }" pattern="yyyy-MM-dd HH:mm:ss"/>"
								data-logo100="${client.logo100m100 }"
								data-logo64="${client.logo64m64 }"
								data-logo32="${client.logo32m32 }"
								data-logo16="${client.logo16m16 }"
								data-default-logo="${client.defaultLogo }"
								data-compulsion-strong-pwd="${client.compulsionStrongPwd }"
								onclick="detailClient(this)"><fmt:message key="common.detail"/></a>
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
		<input type="hidden" id="beanId"/>
		<table>
			<tr>
				<td><fmt:message key="develop.oauth.modify.clientName"/></td><td id="client_name"></td>
			</tr>
			<tr>
				<td>App Key:</td><td id="client_id"></td>
			</tr>
			<tr>
				<td>App Secret:</td><td id="client_secret"></td>
			</tr>
			<tr>
				<td><fmt:message key="admin.oauth.status"/><fmt:message key="common.maohao"/></td>
				<td id="client_status">
				</td>
			</tr>
			<tr>
				<td><fmt:message key='admin.oauth.submit.time'/><fmt:message key="common.maohao"/></td>
				<td id="applicant_time">
				</td>
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
			<tr>
				<td><fmt:message key="develop.oauth.modify.creator"/></td>
				<td id="creatorEmail"></td>
			</tr>
			<tr>
				<td><fmt:message key='admin.oauth.is.return.orgInfo'/><fmt:message key="common.maohao"/></td>
				<td id="needOrgInfo"></td>
			</tr>
			<tr>
				<td>应用图标<fmt:message key="common.maohao"/></td>
				<td>
					<img style="width:100px;height:100px" id="logo100" src=""/>
					<a class="removeLogo" data-target="100p" >删除</a>
					<img style="width:64px;height:64px" id="logo64" src=""/>
					<a class="removeLogo" data-target="64p" >删除</a>
					<img style="width:32px;height:32px" id="logo32" src=""/>
					<a class="removeLogo" data-target="32p" >删除</a>
					<img style="width:16px;height:16px" id="logo16" src=""/>
					<a class="removeLogo" data-target="16p" >删除</a>
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
				<td>App Key<fmt:message key="common.maohao"/></td><td><input  name="client_id"  size="10"/></td>
			</tr>
			<tr>
				<td>App Secret<fmt:message key="common.maohao"/></td><td><input name="client_secret" id="client_secret"/></td>
			</tr>
			<tr>
				<td>是否启用弱密码验证:</td>
				<td>
						<input  type="radio" name="compulsionStrongPwd" value="true"/>是
						<input checked="checked" type="radio" name="compulsionStrongPwd" value="false"/>否
				</td> 
			</tr>
			<tr>
				<td><fmt:message key="oauth.enc.pwd"/></td>
				<td>
					<input checked="checked" type="radio" name="pwd_type" value="none"/><fmt:message key="common.none"/>
					<input type="radio" name="pwd_type" value="SHA"/>SHA
					<input type="radio" name="pwd_type" value="MD5"/>MD5
					<input type="radio" name="pwd_type" value="crypt"/>CRYPT_MD5
				</td>
			</tr>
			<tr>
				<td><fmt:message key='app.access.pwd'></fmt:message><fmt:message key="common.maohao"/></td>
				<td>
					<label style="display:inline;" >
	           			<input type="radio" name="enableAppPwd" checked="checked" value="yes"/><fmt:message key="app.access.pwd.use"></fmt:message>
	            	</label> 
	            	<label style="display:inline;" >
	           			<input type="radio" name="enableAppPwd" value="no" /><fmt:message key="app.access.pwd.unuse"></fmt:message>
	            	</label>
				</td>
			</tr>
			<tr>
				<td><fmt:message key='develop.oauth.modify.appType'/><fmt:message key='common.maohao'/></td>
				<td> 
					<input type="radio" checked="checked" name="app_type" value="webapp" id="app_type_web"/><label style="display:inline;" for="app_type_web"><fmt:message key='develop.oauth.modify.appType.webapp'/></label>
					<input type="radio" name="app_type" value="phoneapp" id="app_type_phone"/><label style="display:inline;" for="app_type_phone"><fmt:message key='develop.oauth.modify.appType.phoneapp'/></label>
				</td>
			</tr>
			<tr>
				<td><fmt:message key='admin.oauth.scope'/><fmt:message key="common.maohao"/></td>
				<td>
					<input id="ddlService_ck" type="checkbox" value="ddlService" name="scope"/><span><fmt:message key='admin.oauth.scope.ddl.service'/></span>
				</td>
			</tr>
			<tr>
				<td><fmt:message key='admin.oauth.orgInfo'/><fmt:message key="common.maohao"/></td>
				<td>
					<input id="need_org_info" name="need_org_info" type="checkbox" value="1" name="scope"/><span><fmt:message key='admin.oauth.return.orgInfo'/></span>
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
	Oauth.selectedItem=$(obj);
	$('#beanId').val(data.id);
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
	$('#creatorEmail').html(data.creatorEmail);
	$('#needOrgInfo').html(data.needOrgInfo?'是':'否'); 
	$('#applicant_time').html(data.applicantTime); 
	$('#logo100').attr('src',"../logo?logoId="+data.logo100);
	$('#logo64').attr('src',"../logo?logoId="+data.logo64);
	$('#logo32').attr('src',"../logo?logoId="+data.logo32);
	$('#logo16').attr('src',"../logo?logoId="+data.logo16);
	   
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
				$("td input[name='pwd_type'][value='"+data.pwd_type+"']").click();
				$('input[name=enableAppPwd][value='+data.enableAppPwd+']').click();
				$('input[name=compulsionStrongPwd][value='+data.compulsionStrongPwd+']').click();
				if(data.needOrgInfo){
					$("td input[name='need_org_info']").click();
				}
				if(data.scope){
					var scopes=data.scope.split(",");
					for(var i=0;i<scopes.length;i++){
						$("#"+scopes[i]+"_ck").click();
					}
				}
				
				$("#operateTitle").html("<fmt:message key='common.modify'/>");
				$("#updateDialog").modal("show"); 
			}else{
				alert(data.message);
			}
		}
	});
}
var Oauth={
		selectedItem:null
};
$(document).ready(function(){
	
	//bind remove logo click
	$('.removeLogo').on('click',function(){
		if(confirm("确认删除应用图标吗?")){
			var $img=$(this).prev();
			var target=$(this).data('target');
			$.get("addClient?act=removeLogo",{"beanId":$('#beanId').val(),"target":target}).done(function(){
				var selectedItem=Oauth.selectedItem;
				var defaultLogoId=0;
				if(selectedItem){
					defaultLogoId=selectedItem.data('defaultLogo');
				}
				$img.attr('src',"../logo?logoId="+defaultLogoId);
				switch(target){
				case '100p':{
					selectedItem.data('logo100',defaultLogoId);
					break;
				} 
				case '64p':{
					selectedItem.data('logo64',defaultLogoId);
					break;
				}
				case '32p':{
					selectedItem.data('logo32',defaultLogoId);
					break;
				}
				case '16p':{
					selectedItem.data('logo16',defaultLogoId);
					break;
					
				}
				}
			});
		}
	});
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
				alert("<fmt:message key='admin.oauth.op.error'/>");
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
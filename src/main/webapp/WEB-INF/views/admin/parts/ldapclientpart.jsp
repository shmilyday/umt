<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<fmt:setBundle basename="application" />

<div class="content-container">
	<div class="userTable" style="height: 527px;">
		<table class="table table-hover"
			style="word-wrap: break-word; table-layout: fixed;" id="sessions"
			border="0">
			<tbody>
				<tr class="top">
					<td width="30px">#</td>
					<td width="20%"><fmt:message key="admin.oauth.name" /></td>
					<td><fmt:message key="admin.oauth.status" /></td>
					<td width="20%"><fmt:message key="admin.oauth.submit.time" />
					</td>
					<td width="20%">申请人</td>
					<td width="20%"><fmt:message key="admin.oauth.operation" /></td>

				</tr>
				<c:forEach items="${ldaps }" varStatus="status" var="ldap">
					<tr class="firstline ">
						<td>${status.index+1 }</td>
						<td title="<c:out value="${ldap.clientName }"/>"><c:out
								value="${ldap.clientName }" /></td>
						<td><c:choose>
								<c:when test="${ldap.appStatus=='apply' }">
									<fmt:message key='admin.oauth.status.apply' />
								</c:when>
								<c:when test="${ldap.appStatus=='accept' }">
									<fmt:message key='admin.oauth.status.accept' />
								</c:when>
								<c:when test="${ldap.appStatus=='refuse' }">
									<fmt:message key='admin.oauth.status.refuse' />
								</c:when>
							</c:choose></td>
						<td><fmt:formatDate value="${ldap.createTime }"
								pattern="yyyy-MM-dd HH:mm:ss" /></td>
						<td><c:out value="${ldap.userCstnetId }" /></td>
						<td width="20%" data-id="${ldap.id }"><a class="update"><fmt:message
									key="common.update" /></a> <a class="delete"
							href="<umt:url value="/admin/ldap.do?act=deleteLdap&id=${ldap.id }	"/>"><fmt:message
									key="common.delete" /></a></td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>
</div>
<div id="updateDialog" class="modal hide fade" tabindex="-1"
	role="dialog" data-backdrop='true' aria-hidden="true">
	<div class="modal-header">
		<h4 id="operateTitle">更新LDAP应用</h4>
	</div>
	<form id="ldapForm" method="post"
		action="<umt:url value="/admin/ldap.do?act=saveLdap"/>">
		<input type="hidden" id="id" name="id" value="0" />
		<div class="modal-body">
			<p id="content">
			<table>
				<tr>
					<td>应用名称</td>
					<td><input name="clientName" id="clientName" maxlength="30" /></td>
				</tr>
				<tr>
					<td>应用代码</td>
					<td id="rdn"></td>
				</tr>
				<tr>
					<td>应用密码</td>
					<td id="ldapPassword"></td>
				</tr>
				<tr>
					<td>权限</td>
					<td><label style="display: inline;"> <input
							type="radio" name="priv" checked="checked" value="open" />公开
					</label> <label style="display: inline;"> <input type="radio"
							name="priv" value="needApply" />公开需审核
					</label> <label style="display: inline;"> <input type="radio"
							name="priv" value="closed" />关闭
					</label></td>
				</tr>
				<tr>
					<td>发布范围</td>
					<td><input type="radio" name="publishToAll"
						class="publishToAll" value="true" />对所有人发布 <br /> <input
						type="radio" name="publishToAll" class="publishToAll"
						value="false" />对特定域发布 <input name="pubScope" id="pubScope"
						maxlength="100" class="publishToSpecial" /></td>
				</tr>
				<tr>
					<td><fmt:message key="admin.oauth.status" /></td>
					<td><select id="appStatus" name="appStatus">
							<option value="accept">接受</option>
							<option value="apply">申请中</option>
							<option value="refuse">拒绝</option>
					</select></td>
				</tr>
				<tr>
					<td>应用描述</td>
					<td><textarea id="description" name="description"></textarea></td>
				</tr>
				<tr>
					<td>申请者姓名</td>
					<td><input name="applicant" id="applicant" /></td>
				</tr>
				<tr>
					<td>申请人所在单位</td>
					<td><input name="company" id="company" /></td>
				</tr>
				<tr>
					<td>申请人联系方式</td>
					<td><input name="contactInfo" id="contactInfo" /></td>
				</tr>
			</table>
		</div>
		<div class="modal-footer">
			<input type="submit" class="btn btn-primary"
				value="<fmt:message key="common.submit"/>"> <a class="btn"
				data-dismiss="modal" aria-hidden="true" id="no"> <fmt:message
					key="common.cancel" /></a>
		</div>
	</form>
</div>
<div
	style="display: none; position: absolute; z-index: 999; top: 10%; left: 30%; right: 30%; background-color: #fff; border: 5px solid #888; box-shadow: 0 3px 7px rgba(0, 0, 0, 0.8); height: 300px;"
	class="oauthClientOperate">
	<a class="closeOperate"
		style="float: right; background: #08a; color: #fff; font-weight: bold; padding: 5px;">X</a>
	<div class="operateMessage">
		<p style="color: red" class="messageShow"></p>
	</div>
	<h4 id="operateTitle" style="font-size: large;"></h4>
	<div></div>
</div>
<script type="text/javascript"
	src="<umt:url value="/js/jquery.validate.min.js"/>"></script>
<script type="text/javascript"
	src="<umt:url value="/thirdparty/bootstrap/js/bootstrap.min.js"/>"></script>
<script>
$(document).ready(function(){ 
	$(".nav li.current").removeClass("current");
	$('#ldapForm').validate({
		rules:{
			clientName:{
				required:true
			},
			pubScope:{pubScope:true}
		},
		messages:{
			clientName:{
				required:'应用名称不允许为空'
			},
			pubScope:{required:'<fmt:message key="develop.oauth.validate.pubScope.required"/>'}
		}
	});
	$.validator.addMethod("pubScope",
			function(value,element){
				if(value==null||value==''){
					return true;
				}
				var domains=value.split(";");
				var pattern=/^([0-9a-z\-]+\.)+[0-9a-z]+$/;
				var result=true;
				$.each(domains,function(index,domain){
					if(!pattern.test(domain) ){
						result=false;
					}
				});
				return result;
			},"请按规则输入"
		);
	$('.update').on('click',function(){
		var id=$(this).closest('td').data('id');
		$.get('<umt:url value="/admin/ldap.do"/>',{act:'getDetail',id:id,'_':Math.random()}).done(function(data){
			 $('#updateDialog').modal('show');
			 $('#clientName').val(data.clientName);
			 $('#rdn').html(data.rdn);
			 $('#ldapPassword').html(data.ldapPassword?data.ldapPassword:'');
			 $('input[name=priv][value='+data.priv+']').click();
			 $('#appStatus').val(data.appStatus);
			 $('#description').val(data.description);
			 $('#applicant').val(data.applicant);
			 $('#pubScope').val(data.pubScope);
			 if(data.pubScope==''||data.pubScope==null){
					$(":radio.publishToAll[value='true']").attr("checked",true);
				}else{
					$(":radio.publishToAll[value='false']").attr("checked",true);
				}
				pubScopeClick();
			 $('#company').val(data.company);
			 $('#contactInfo').val(data.contactInfo);
			 $('#id').val(data.id);
		});
	});
	
	$(":radio.publishToAll").on('click',function(){
		pubScopeClick();
	});
	
	function pubScopeClick(){
		if($(":radio.publishToAll:checked").val()=='true'){
			$(".publishToSpecial").hide();
			$("#pubScope").rules("remove","required");
			$("#pubScope").next(".error").remove();
			$("#pubScope").val("");
		}else{
			$(".publishToSpecial").show();
			$("#pubScope").rules("add",{required:true});
		}
	}
	
	$("#ladpMenu").addClass("current"); 
});
</script>
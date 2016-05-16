<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<div id="updateDialog" class="modal hide fade" tabindex="-1" role="dialog"
	data-backdrop='true' aria-hidden="true">
	<div class="modal-header">
		<h4 id="operateTitle">添加</h4>
	</div>
	<form id="updateThirdPartyForm" method="post"
		action='<umt:url value="/admin/manageAuth.do"/>'>
		<input type="hidden" id="operateType" name="act" value="update" />
		<div class="modal-body">
			<p id="content">
				<table>
				<tr>
					<td>代号</td>
					<td><input type="text" name="code" readonly id="update_code"/></td>
				</tr>
				<tr>
					<td>名称</td>
					<td><input type="text" required name="name" id="update_name" /></td>
				</tr>
				<tr>
					<td>ClientId</td>
					<td><input type="text" required name="clientId" id="update_clientId" /></td>
				</tr>
				<tr>
					<td>Secret</td>
					<td><input type="text" required name="secret" id="update_secret" /></td>
				</tr>
				<tr>
					<td>服务器URL</td>
					<td><input type="url" name="serverUrl" id="update_serverUrl"/></td>
				</tr>
				<tr>
					<td>登录界面主题</td>
					<td><input type="text" required name="theme" value="full" id="update_theme"/></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="checkbox" name="showInLogin" id="update_showInLogin"/><label for="update_showInLogin" class="inline"> 显示在登录界面</label></td>
				</tr>
			</table>
		</div>
		<div class="modal-footer">
			<input type="submit" class="btn btn-primary" value="确定"> 
				<a class="btn" data-dismiss="modal" aria-hidden="true" id="no"> 取消</a>
		</div>
	</form>
</div>
<script type="text/javascript">
	function updateClient(code){
		$.get("<umt:url value='/admin/manageAuth.do'/>",{"act":"load","code":code}, function(data){
			$("#update_code").val(code);
			$("#update_name").val(data.name);
			$("#update_clientId").val(data.clientId);
			$("#update_secret").val(data.secret);
			$("#update_serverUrl").val(data.serverUrl);
			$("#update_theme").val(data.theme);
			$("#update_showInLogin").attr("checked", data.showInLogin);	
			
			$("#updateDialog").modal("show");
		});
	}
	$(document).ready(function() {
		$("#updateThirdPartyForm").validate();
	});
</script>
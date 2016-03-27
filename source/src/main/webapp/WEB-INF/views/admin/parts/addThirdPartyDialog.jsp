<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<div id="addDialog" class="modal hide fade" tabindex="-1" role="dialog"
	data-backdrop='true' aria-hidden="true">
	<div class="modal-header">
		<h4 id="operateTitle">添加</h4>
	</div>
	<form id="addThirdpartyForm" method="post"
		action='<umt:url value="/admin/manageAuth.do"/>'>
		<input type="hidden" id="operateType" name="act" value="add" />
		<div class="modal-body">
			<p id="content">
				<table>
				<tr>
					<td>代号</td>
					<td><input type="text" required name="code"/></td>
				</tr>
				<tr>
					<td>名称</td>
					<td><input type="text" required name="name" id="name" /></td>
				</tr>
				<tr>
					<td>ClientId</td>
					<td><input type="text" required name="clientId" id="clientId" /></td>
				</tr>
				<tr>
					<td>Secret</td>
					<td><input type="text" required name="secret" /></td>
				</tr>
				<tr>
					<td>服务器URL</td>
					<td><input type="url" name="serverUrl" /></td>
				</tr>
				<tr>
					<td>登录界面主题</td>
					<td><input type="text" required name="theme"/></td>
				</tr>
				<tr>
					<td></td>
					<td><input type="checkbox" name="showInLogin" id="add_showInLogin"/><label class="inline" for="add_showInLogin">显示在登录界面</label></td>
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
	$(document).ready(function() {
		$("#addClient").live('click', function() {
			$('#addThirdpartyForm').get(0).reset();
			$("#addDialog").modal("show");
		});
		$("#addThirdpartyForm").validate(
			{
				rules : {
					code : {
						required : true,
						remote : {
							type : "post",
							url : '<umt:url value="/admin/manageAuth.do?act=codeValid"/>',
							 data:{"code":$("#code").val()}
							}
						}
					},
				messages:{
					code:{
						remote:"代号已被使用"
					}
				},
				submitHandler:function(form){
					form.submit();
				}
			});
	});
</script>
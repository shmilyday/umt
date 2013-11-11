<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<fmt:setBundle basename="application" />	

<%
	pageContext.setAttribute("ContextPath", request.getContextPath());
%>

<script type="text/javascript">

</script>

<div class="content_body">
	<div class="body_img">
		<img src="../images/jt_05.gif" />
		<span class="blue_font"><fmt:message key="emailtemplate.edittitle"/></span>
	</div>

	<div class="body_bg">

		<form  action="editTemplate.do" method="post">
			<table>
				<tr>
					<td>client_id:</td>
					<td><input type="text" name="client_id"></td>
					<td></td>
				</tr>
				<tr>
					<td>client_sercret</td>
					<td><input type="text" name="client_sercret"></td>
					<td></td>
				</tr>
				<tr>
					<td>redirect_URI:</td>
					<td><input type="text" name="redirect_URI"></td>
					<td></td>
				</tr>
				<tr>
					<td>scope:</td>
					<td><input type="text" name="scope"></td>
					<td></td>
				</tr>
				<tr>
					<td>客户端名称：</td>
					<td><input type="text" name="client_name"></td>
					<td></td>
				</tr>
				<tr>
					<td>申请人：</td>
					<td><input type="text" name="applicant"></td>
					<td></td>
				</tr>
				<tr>
					<td>申请人电话：</td>
					<td><input type="text" name="applicant_phone"></td>
					<td></td>
				</tr>
				<tr>
					<td>申请人联系方式：</td>
					<td><input type="text" name="contact_info"></td>
					<td></td>
				</tr>
				<tr>
					<td>客户端描述：</td>
					<td><textarea name="description"></textarea></td>
					<td></td>
				</tr>
				<tr>
					<td><input type="submit" value="提交"></td>
					<td></td>
					<td></td>
				</tr>
			</table>
		</form>

	</div>
</div>

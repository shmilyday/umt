<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fmt:setBundle basename="application" />

<script type="text/javascript" src="../js/string.js"></script>
<script type="text/javascript" src="../js/formcheck.js"></script>
<div class="content-container">
	<div class="toolBar">
		<fmt:message key="menu.userinfo" />
	</div>
	<div class="clearboth red">
		<c:if test="${message!=null}">
			<fmt:message key="${message}" />
		</c:if>
	</div>
	<div class="userTable">
		<form action="updateUser.do" class="form-horizontal"  method="post" name="userform"
			onsubmit="return checkinput(this)">
			<input type="hidden" name="act" value="update" /> <input
				type="hidden" name="umtId" value="${User.umtId}" />
			<div class="control-group">
				<label class="control-label"><fmt:message
						key="userinfo.username" /></label>
				<div class="controls">

					<input type="hidden" name="username" value="${User.cstnetId}" />
					${User.cstnetId}
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"> <fmt:message
						key="userinfo.truename" />
				</label>
				<div class="controls">
					<input type="text" class="textinput" name="truename"
						maxLength="255" value="${User.trueName}" required="true"
						message="<fmt:message key='userinfo.message.require.truename'/>" />
				</div>
			</div>
			<div class="control-group">
				<label class="control-label"><fmt:message key="userinfo.email" /></label>
				<div class="controls"><input type="text" maxLength="255" class="textinput"
					name="email" value="${User.cstnetId}" required="true"
					datatype="email"
					message="<fmt:message key='userinfo.message.require.email'/>" /></div>
			</div>
			<div class="control-group">
				<label class="control-label"><fmt:message key="userinfo.password" /></label>
				<div class="controls"><input id="password" maxLength="255" class="textinput"
					name="password" type="password" /></div>
			</div>
			<div class="control-group" style="margin-top:-25px;">
				<div class="controls red"><fmt:message key="userinfo.message.noinput" />
				</div>
			</div>
			<div class="control-group" style="margin-top:-15px;">
				<label class="control-label"><fmt:message key="userinfo.retype" /></label>
				<div class="controls"><input id="retype" class="textinput" maxLength="255"
					name="retype" type="password" /></div>
			</div>

					<div class="control-group">
						<div class="controls"><input type="submit" style="display: none;" />
						 <button
							type="button" onclick="return submitform()"	
							class="btn btn-primary" style=""><fmt:message key='userinfo.submit'/></button>
							 <button type="reset" 
							class="btn btn-primary" ><fmt:message key="regist.form.cancel"/></button>
							</div>
					</div>
			
		</form>
	</div>
</div>
<div class="clear"></div>
<script language="javascript">
	$(document).ready(function() {
		$(".messagebar").fadeOut(900);
	});
	function submitform() {
		var form = document.userform;
		if (checkinput(form)) {
			form.submit();
		}
	}
	function checkinput(form) {
		if (!checkform(form))
			return false;
		var pwd = form.password.value;
		var retype = form.retype.value;
		if (pwd != "" || retype != "") {
			if (pwd != retype) {
				alert("<fmt:message key='userinfo.message.notmatch'/>");
				return false;
			} else {
				return true;
			}
		} else {
			return true;
		}
	}
</script>

<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:setBundle basename="application" />

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><fmt:message key='regist.title'/></title>
<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
<link href="css/umt.css" rel="stylesheet" type="text/css"/>
</head>

<body>
<jsp:include flush="true" page="banner.jsp"></jsp:include>
<script type="text/javascript" src="js/ValidateCode.js"></script>
<script type="text/javascript" src="js/string.js"></script>
<script type="text/javascript" src="js/formcheck.js"></script>
	<c:if test="${message!=null}">
		<div class="messagebar"><fmt:message key="${message}"/></div>
	</c:if>
	<form action="createRequest.do" method="post" class="createRequestForm">
	<div class="loginbox" style="margin:80px 230px;width:550px;">
	<table class="form_table">
			<tr><td class="td_background">
		<div class="innerbox">
				<input type="hidden" name="act" value="save"/>
				<table class="registtable" width="100%">
					<tr>
						<th colspan="2"><fmt:message key='regist.title'/></th>
					</tr>
					<tr>
						<td class="rtl" width="200px"><fmt:message key='regist.form.username'/></td>
						<td><input type="text" id="username" maxlength="255" name="username" required="true" datatype="email"/><span id="usermessage"></span></td>
					</tr>
					<tr>
						<td class="rtl"><fmt:message key='regist.form.truename'/></td>
						<td><input type="text" maxlength="255" name="truename" required="true" id="truename"/><span id="truenamemessage"></span></td>
					</tr>
					<tr>
						<td class="rtl"><fmt:message key='regist.form.orgnization'/></td>
						<td><input type="text" maxlength="255" name="orgnization" required="true"/></td>
					</tr>
					<tr>
						<td class="rtl"><fmt:message key='regist.form.phonenumber'/></td>
						<td><input type="text" maxlength="255" name="phonenumber" required="true" datatype="phone-number"/></td>
					</tr>
					<tr>
						<td class="rtl"><fmt:message key='regist.form.password'/></td>
						<td><input type="password" maxlength="255" name="password" required="true" id="password"/></td>
					</tr>
					<tr>
						<td class="rtl"><fmt:message key='regist.form.retype'/></td>
						<td><input type="password" maxlength="255" name="repassword" required="true" id="repassword"/><span id="repasswordmessage"></span></td>
					</tr>
					<tr>
						<td class="rtl"><fmt:message key='regist.form.imagetext'/></td>
						<td><input type="text" maxlength="255" name="validcode" required="true"/></td>
					</tr>
					<tr>
						<td class="rtl"></td>
						<td>
							<img id="ValidCodeImage" src="" />
							<a class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message key='regist.form.changeit'/></a>
						</td>
					</tr>
				</table>
				<div class="buttondiv">
					<input type="submit" value="<fmt:message key='regist.form.submit'/>"/> 
					<input type="reset" value="<fmt:message key='regist.form.cancel'/>"/></div>
		</div>
		</td></tr></table>
	</div>
</form>
<jsp:include flush="true" page="../../bottom.jsp"></jsp:include>
<script type="text/javascript">
	var imageObj;
	function checkusername(userinput){
		var username=$('#username').attr('value');
		if (!form_check_email(username)){
			$('#usermessage').html("<fmt:message key='regist.username.format'/>");
			return;
		}
		$.post('createRequest.do', {act:'usercheck', username:username}, function(data){
			if (!data[0].exist){
				$('#usermessage').html("<img src='images/ok.gif'/>");
			}else{
				$('#usermessage').html("<fmt:message key='regist.user.exist'/>");
			}
		},'json');
	}
	function checkRegisterFrom()
	{
	   var username=  $('#username').attr('value');
	   var flag = true;
	   if (!form_check_email(username)){
			$('#usermessage').html("<fmt:message key='regist.username.format'/>");
			flag = false;
		}
	   var truename = $('#truename').attr('value');
	   if(truename==""||truename==null)
	   {
	       $('#truenamemessage').html("<fmt:message key='regist.truename.require'/>");
			flag = false;
	   }
	   var password = $('#password').attr('value');
	   
	   var repassword = $('#repassword').attr('value');
	   if(password!=repassword)
	   {
	      $('#repasswordmessage').html("<fmt:message key='regist.password.unequal'/>");
	      flag = false;
	    
	   }
	   return flag;
	}
	$(document).ready(function(){
		imageObj = ValidateImage("ValidCodeImage");
		$('#username').blur(checkusername);
		$('#createRequestForm').submit(checkRegisterFrom);
	});
</script>
</body>
</html>
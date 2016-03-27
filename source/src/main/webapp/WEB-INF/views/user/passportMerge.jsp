<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
	<head>
		<title>帮助</title>
		<link href="<%= request.getContextPath() %>/images/favicon.ico" rel="shortcut icon"	type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>

	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<f:script src="${contextPath }/js/ValidateCode.js"/>
		<f:script src="${contextPath }/js/string.js"/>
		
		<div class="container gray login">
			<p class="content-text-mid">
				尊敬的用户，您好：
			</p>
			<p class="content-text-mid">
				 我们高兴的通知您，原<span class="duckling-logo-small"></span>Duckling通行证升级为中国科技网通行证。现在，中科院邮件系统账号可作为中国科技网通行证直接登录。中国科技网通行证账号密码与中科院邮件系统账号密码相同。
			</p>
			<p class="content-text-mid">
				您曾注册过名为  <strong>${loginInfo.user.cstnetId }</strong> 的原Duckling通行证账号。同时，<strong>${loginInfo.user.cstnetId }</strong> 也是中科院邮件系统账号。为了方便您的使用，请您确认将这两个账号的密码统一成您中科院邮件系统账号的密码。
				
			</p>
			<p class="content-text-mid">
				请在下面输入您中科院邮件系统账号的密码，经过验证后，您中国科技网通行证账号的密码将设置为与此相同。今后您可使用 <strong>${loginInfo.user.cstnetId }</strong>和这一密码畅通使用中科院邮件系统以及各类科研应用服务。原Duckling通行证账号和中科院邮件系统账号对应的各项服务及数据不受影响。
			</p>
			<form class="form-horizontal" action="<umt:url value="/user/merge.do?act=merge"/>" method="post">
				<div class="control-group" style="padding-left:5%">
	              	<label class="control-label" for="password">
						 中科院邮件系统密码：
					</label>
	              	<div class="controls">
	               		<input type="password" class="passwordInput" maxlength="254" id="password" name="password"/>
						<span id="password_error_place" class="error help-inline">
							<c:if test="${!empty password_error }">
								<fmt:message key="${password_error }"></fmt:message>
							</c:if>
						</span>
	              	</div>
	            </div>
				<div class="control-group" style="padding-left:5%">
	              	<div class="controls">
	              		<button class="btn long btn-primary" type="submit">确认</button><a class= "delete" href="<umt:url value="/"/>">取消</a>
	              	</div>
	            </div>
			</form>
			<p class="content-text-mid" style="color:#666666;margin-top:0">
				建议您尽早确认。原<span class="duckling-logo-small"></span>Duckling通行证账号的密码将在<strong>2013年6月1日</strong>之后不能使用。								
			</p>
			<p class="content-text-mid" style="color:#666666;margin-top:0">
				如有疑问，请联系<a href="mailto:duckling-default@cnic.cn">duckling-default@cnic.cn</a>。								
			</p>
			
		</div>
		
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		
	</body>
</html>
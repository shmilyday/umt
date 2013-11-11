<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<fmt:setBundle basename="application" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"> 
<html>
	<head>
		<title><fmt:message key="remindpass.title"/></title>
		<link href="css/umt.css" rel="stylesheet" type="text/css"/>
		<link href="images/favicon.ico" rel="shortcut icon"
			type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache"/>
		<meta http-equiv="cache-control" content="no-cache"/>
		<script type="text/javascript" src="js/ValidateCode.js"></script>
		<script type="text/javascript" src="js/string.js"></script>
		<script type="text/javascript" src="js/formcheck.js"></script>
	</head>

	<body>
		<jsp:include flush="true" page="banner.jsp"></jsp:include>
		<div class="content">
		<form action="remindpass" method="post" onsubmit="return checkform(this)">
			<div class="loginbox">
			<table class="form_table">
			<tr><td class="td_background">
				<div class="innerbox">
						<table>
							<tr>
								<th colspan="2">
									<fmt:message key="remindpass.title"/>
								</th>
							</tr>
							<tr>
								<td>
									<fmt:message key="remindpass.username"/>
								</td>
								<td>
									<input type="text" class="logininput" maxlength="255" name="username" 
										required="true" datatype="email"
										message="<fmt:message key="remindpass.username.required"/>"/>
									<c:if test="${UserNameError!=null}">
										<span class="warning">
											<fmt:message key="remindpass.usernotfound"/>
										</span>
									</c:if>
								</td>
							</tr>
							<tr>
								<td>
									<fmt:message key="remindpass.imagetext"/>
								</td>
								<td>
										<input type="text" maxlength="255" class="logininput"
											name="ValidCode" 
											required="true"
											message="<fmt:message key="login.imagetext.required"/>"/>
										<c:if test="${ValideCodeError!=null}">
											<span class="warning">
												<fmt:message key="remindpass.imagetext.wrong"/>
											</span>
										</c:if>
								</td>
							</tr>
								<tr>
									<td></td>
									<td>
										<img id="ValidCodeImage" src="" />
										<a class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message key="remindpass.changeit"/></a>
									</td>
								</tr>
							<tr>
								<td align="left"></td>
								<td>
									<input type="submit" value="<fmt:message key="remindpass.submit"/>" />
								</td>
							</tr>
						</table>
				</div>
				</td></tr></table>
			</div>
		</form>
		</div>
		<jsp:include flush="true" page="../../bottom.jsp"></jsp:include>
		<script type="text/javascript">
			var imageObj = ValidateImage("ValidCodeImage");
		</script>
	</body>
</html>

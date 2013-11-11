<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
<fmt:setBundle basename="application" />
<umt:AppList/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key="remindpass.title"/></title>
		<link href="images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
		<f:script src="js/ValidateCode.js"/>
		<f:script src="js/string.js"/>
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		<c:set value="${findPswUser.type!='coreMail'&&findPswUser.type!='uc' }" var="coreMail"></c:set>
		<div class="container login gray">
		<c:choose>
			<c:when test="${coreMailOnly}">
				<p class="congratulation sorry"><fmt:message key="remindpass.cannotFind"/></p>
			</c:when>
			<c:otherwise>
				<h2 class="login-title"><fmt:message key="remindpass.title"/>
					<span class="sub-title hides"><fmt:message key="remindpass.choose"/></span>
				</h2>
				<div class="findPsw">
					<ul class="oper">
					<c:if test="${!empty findPswUser.cstnetId &&coreMail}">
						<li <c:if test="${active=='cstnetId'}">class="active"</c:if>><a  id="" href="<umt:url value="/findPsw.do?act=stepTwo&active=cstnetId"/>" type="login"><fmt:message key="remindpass.byCstnetID"/></a></li>
					</c:if>
					<c:if test="${!empty findPswUser.secondaryEmails }">
						<li <c:if test="${active=='secondary'}">class="active"</c:if>><a  href="<umt:url value="/findPsw.do?act=stepTwo&active=secondary"/>" type="secondary"><fmt:message key='remindpass.bySecondary'/></a></li>
					</c:if>
					<c:if test="${!empty findPswUser.securityEmail }">
						<li <c:if test="${active=='security'}">class="active"</c:if>><a  href="<umt:url value="/findPsw.do?act=stepTwo&active=security"/>" type="security"><fmt:message key="remindpass.bySecurityEmail"/></a></li>
					</c:if>
					</ul>
					<c:choose>
						<c:when test="${active=='cstnetId' }">
							<div class="right-content">
								<h3><fmt:message key="remindpass.byEmail.address"/><span class="success-text"><umt:hideEmail email="${findPswUser.cstnetId }"/></span><fmt:message key="remindpass.sureToUse"/></h3>
								<table>
									<tr>
										<td class="gray-text" colSpan="2" ><fmt:message key="remindpass.imageText.hint"/></td>
									</tr>
									<tr>
										<td>
											<input id="ValidCode" class="ValidCode"></input>
											<img id="ValidCodeImage" src="" />
											<span id="ValidCode_error" class="error" style="display:none">
												<fmt:message key="login.imagetext.wrong"/>
											</span>
										</td>
										<td>
											<a class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message
													key="login.imagetext.changeit" />
											</a>
										</td>
									</tr>
									<tr>
										<td><button type="button" class="btn long btn-primary" id="loginEmail_submit"><fmt:message key="remindpass.submit"/></button></td>
										<td><a href="<umt:url value="/findPsw.do?act=stepOne"/>"><fmt:message key="remindpass.findOtherAccounts"/></a></td>
									</tr>
								</table>
							</div>
						</c:when>
						<c:when test="${active=='secondary' }">
							<div class="right-content">
								<h3>
								<fmt:message key='find.password.choose.email.title'/>
								</h3>
								<table>
									<c:forEach items="${findPswUser.secondaryEmails }" varStatus="index" var="secondary">
									<tr>
										<td colspan="2">
										<input <c:if test="${index.index==0 }">checked="checked"</c:if> type="radio" name="loginName" value="${secondary}"></input><fmt:message key="common.userInfo.secondaryMail"/><umt:hideEmail email="${secondary  }"/></td>
									</tr>
									</c:forEach>
									<tr>
										<td class="gray-text" colspan="2" ><fmt:message key="remindpass.imageText.hint"/></td>
									</tr>
									<tr>
										<td>
											<input id="ValidCode" class="ValidCode"></input>
											<img id="ValidCodeImage" src="" />
											<span id="ValidCode_error" class="error" style="display:none">
												<fmt:message key="login.imagetext.wrong"/>
											</span>
										</td>
										<td>
											<a class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message
													key="login.imagetext.changeit" />
											</a>
										</td>
									</tr>
									<tr>
										<td><button type="button" class="btn long btn-primary" id="secondaryEmail_submit"><fmt:message key="remindpass.submit"/></button></td>
										<td><a href="<umt:url value="/findPsw.do?act=stepOne"/>"><fmt:message key="remindpass.findOtherAccounts"/></a></td>
									</tr>
								</table>
							</div>							
						</c:when>
						<c:when test="${active=='security' }">
							<div class="right-content">
								<h3><fmt:message key="remindpass.bySecurityEmail.address"/><span class="success-text"><umt:hideEmail email="${findPswUser.securityEmail }"/></span><fmt:message key="remindpass.sureToUse"/></h3>
								<table>
									<tr>
										<td class="gray-text" colSpan="2" ><fmt:message key="remindpass.imageText.hint"/></td>
									</tr>
									<tr>
										<td>
											<input id="ValidCode" class="ValidCode"></input>
											<img id="ValidCodeImage" src="" />
											<span id="ValidCode_error" class="error" style="display:none">
												<fmt:message key="login.imagetext.wrong"/>
											</span>
										</td>
										<td><a class="small_link" href="#" onclick="imageObj.changeImage()"><fmt:message
													key="login.imagetext.changeit" />
											</a>
										</td>
									</tr>
									<tr>
										<td><button type="button" class="btn long btn-primary" id="securityEmail_submit"><fmt:message key="remindpass.submit"/></button></td>
										<td><a href="<umt:url value="/findPsw.do?act=stepOne"/>"><fmt:message key="remindpass.findOtherAccounts"/></a></td>
									</tr>
								</table>
							</div>
						</c:when>
					</c:choose>
					<div class="right-content" id="sendSuccessShow" style="display:none">
						<div class="congratulation">
							<h3><fmt:message key="remindpass.emailSentNotice"/><span class="success-text" id="success_email">z****a@cnic.cn</span></h3>
							<p class="sub-text"><fmt:message key="remindpass.emailSentNoticeToSet"/></p>
							<a id="toEmailLink" target="_blank" href="#"><button type="button" class="btn long btn-primary"><fmt:message key="remindpass.emailSentNoticeGo"/></button></a>
						</div>
					</div>
					<div class="clear"></div>
				</div>
			</c:otherwise>
		</c:choose>
		
		</div>
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
	</body>
	<script type="text/javascript" >
	var imageObj ;
		$(document).ready(
						function() {
							$('#banner_forgot_password').addClass("active");
							$("ul.oper li a").each(function(i, n) {
								if ('${active}'==''&&i == 0) {
									window.location.href=$(this).attr("href");
									return false;
								}
							});
							imageObj= ValidateImage("ValidCodeImage");
							$("#loginEmail_submit").click(function() {
								toAjax('${findPswUser.cstnetId}', 'login');
							});
							$("#securityEmail_submit").click(
									function() {
										toAjax('${findPswUser.securityEmail}',
												'security');
									});
							$("#secondaryEmail_submit").click(function() {
								toAjax(getRadioValue(), 'secondary');
							});
							function toAjax(email, type) {
								$.ajax({
									type : "POST",
									url : "<umt:url value='/findPsw.do'/>",
									data : "act=sendRemindeEmail&loginEmail="
											+ email
											+ "&ValidCode="
											+ $('#ValidCode')
													.val()
											+ "&uid=${findPswUser.id}",
									success : function(msg) {
										var sth = jQuery.parseJSON(msg);
										if (sth.result) {
											showSuccessMessage(email, sth.url);
										} else {
											$('#ValidCode_error')
													.show();
										}
									}
								});
							}
							function getRadioValue() {
								var result = "";
								$("input[name='loginName']")
										.each(
												function(i, n) {
													if ($(n).attr("checked") == 'checked') {
														result = $(n).val();
														return false;
													}
												});
								return result;
							}

							function showSuccessMessage(email, link) {
								$('.right-content').each(function(i, n) {
									$(n).hide();
								});
								$('#success_email').html(hideEmail(email));
								$('#sendSuccessShow').show();
								if (link != '') {
									$('#toEmailLink').show();
									$('#toEmailLink').attr("href", link);
								} else {
									$('#toEmailLink').hide();
								}
							}
						});
	</script>
</html>
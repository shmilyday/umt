<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   
 <%@ taglib uri="WEB-INF/tld/falcon.tld" prefix="f"%>
 <%request.setAttribute("context", request.getContextPath()); %>
<fmt:setBundle basename="application" />
<umt:AppList/>
<umt:refreshUser/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='index.oauth.manage'/></title>
		<link href="${context}/images/favicon.ico" rel="shortcut icon" type="image/x-icon" />
		<f:script src="${context }/thirdparty/fileuploader/fileuploader.js" />
		<f:css href="${context }/thirdparty/fileuploader/fileuploader.css" />
		
		<style type="text/css">  
			.qq-upload-button{
			display: block;
			width: 30px;
			padding: 3px 0px;
			text-align: center;
			font-weight: bold;
			background: none;
			border:none;
			-moz-border-radius: 0px;
			-webkit-border-radius: 0px;
			box-shadow: 0px 0px 0px #ccc;
			-moz-box-shadow: 0px 0px 0px #ccc;
			-webkit-box-shadow: 0px 0px 0px #ccc;
			}
			div.addNewOauth {
			text-align: right;
			padding-top: 5px;
			}
			.sub-app-btn{
			margin: 20px 7% 20px 7%;
			padding-left: 20px;
			}
		</style> 
	</head> 
	<body class="login">
		<jsp:include flush="true" page="../../../banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav" style="margin: 30px 7% 10px 7%;">
				<li class="active"><a for="myOauth" ><fmt:message key="develop.oauth.my.app"/></a></li>
				<li><a href="${context }/help_oauth.jsp" target="_blank"><fmt:message key="develop.oauth.devlop.doc"/></a></li>
				<div class="addNewOauth">
					<button type="button" id="modifyDeveloperInfo" class="btn btn-primary btn-long"><fmt:message key='develop.oauth.add'/></button>
				</div>
				<div class="clear"></div>
			</ul>
			<div class="sub-content" id="myOauth">
				<ul class="nav nav-pills">
					 <li id="webappView"><a  href="${context }/user/developer.do?act=display&viewType=webapp" ><fmt:message key='develop.oauth.modify.appType.webapp'/></a></li>
					<li id="phoneappView"><a  href="${context }/user/developer.do?act=display&viewType=phoneapp" ><fmt:message key='develop.oauth.modify.appType.phoneapp'/></a></li>
					<li id="ldapView"><a  href="${context }/user/developer.do?act=display&viewType=ldap" ><fmt:message key='develop.oauth.modify.appType.ldap'/></a></li>
					<li id="wifiView"><a  href="${context }/user/developer.do?act=display&viewType=wifi" ><fmt:message key='develop.oauth.modify.appType.wifi'/></a></li>
				</ul>
				<div class="clear"></div>
				<c:if test="${empty ldaps }">
					<h3 style="color:gray;text-align:center"><fmt:message key='empty.ldap.apps'/></h3>
				</c:if>
				<c:forEach items="${ldaps }" var="ldap">
					<div class="oAuthList form-horizontal short" >
						<h3>${ldap.clientName }  
							<c:choose>
								<c:when test="${ldap.appStatus=='accept'}">  
									<span><fmt:message key="develop.oauth.status.accept"/></span>
								</c:when>
								<c:when test="${ldap.appStatus=='apply'}">
									<span><fmt:message key="develop.oauth.status.apply"/></span>
								</c:when>
								<c:otherwise>
									<span><fmt:message key="develop.oauth.status.refuse"/></span>
								</c:otherwise>
							</c:choose>
							<button type="button" class="deleteOauth btn btn-small btn-link" data-ldap-id="${ldap.id }"><fmt:message key="develop.oauth.delete"/></button>
							<a class="btn btn-small btn-info" href="<umt:url value="/user/developer.do?act=showMember&ldapId=${ldap.id }"/>"><fmt:message key="develop.ldap.manage.member"></fmt:message></a>
							<button type="button" class="updateOauth btn btn-small btn-info" data-ldap-id="${ldap.id }"><fmt:message key="develop.oauth.update"/></button>
						</h3>
						
						<div class="appdescription">
						<c:choose>
							<c:when test="${ldap.appStatus =='accept'}">
							<div class="control-group">
					              	<label class="control-label nopadding">
										<fmt:message key="develop.ldap.app.dn"/><fmt:message key="common.maohao"/>
									</label>
					              	<div class="controls">
					              		DC=${ ldap.rdn},<umt:config key="ldap.base.dn"></umt:config>
					              	</div>
					            </div>
					            <div class="control-group">
					              	<label class="control-label nopadding">
										<fmt:message key="develop.ldap.app.pwd"/><fmt:message key="common.maohao"/>
									</label>
					              	<div class="controls">
					              		<c:out value="${ ldap.ldapPassword}"/>
					              	</div>
					            </div>
							</c:when>
							<c:otherwise>
							<div class="control-group">
			              	<label class="control-label nopadding">
								<fmt:message key="develop.ldap.app.code">
								</fmt:message>
								<fmt:message key="common.maohao"/>
							</label>
			              	<div class="controls">
			              		${ ldap.rdn}
			              	</div>
			            </div>
							</c:otherwise>
						</c:choose>
						<div class="control-group">
			              	<label class="control-label nopadding">
								<fmt:message key="develop.ldap.permision"></fmt:message>
								<fmt:message key="common.maohao"/>
							</label>
			              	<div class="controls">
			               		<fmt:message key="develop.ldap.permision.${ldap.priv }"/>
			              	</div>
			            </div>
						<div class="control-group">
			              	<label class="control-label nopadding">
								<fmt:message key="develop.oauth.description"/>
							</label>
			              	<div class="controls">
			               		${ldap.description }
			              	</div>
			            </div>
			            </div>
					</div>
				</c:forEach>
				
			</div>
			<div class="sub-content" style="display:none" id="developDoc">  
				<jsp:include page="../../../oauthDoc.jsp"/>
			</div>
			
		</div>
		<jsp:include flush="true" page="editPopup.jsp"/>
		<jsp:include flush="true" page="../../../bottom2013.jsp"></jsp:include>
		<script type="text/javascript" >
			$(document).ready(function(){
				//
				$('#${viewType}View').addClass('active');
				// bind tab click
				$('.sub-nav>li>a').on('click',function(){
					var subContentId=$(this).attr("for");
					if(subContentId){
						$('.sub-nav>li').removeClass("active");
						$(this).parent().addClass("active");
						$('.sub-content').hide();
						$('#'+subContentId).show();
					}
				});
							
				$('#modifyDeveloperInfo').on('click',showNewDialog);
				//updateOauth button
				$('.updateOauth').on('click',function(){
					$.get("${context}/user/developer.do?act=getLdapApp&ldapId="+$(this).data('ldapId')+"&_="+Math.random()).done(function(data){
						eval('data='+data);
						$('#appTypeControl').hide();
						$('input[name=appType][value=ldap]').click();
						$('#id').val(data.id);
						$('#applicant').val(data.applicant);
						$('#clientName').val(data.clientName);
						$('#pubScope').val(data.pubScope);
						if(data.pubScope==''||data.pubScope==null){
							$(":radio.publishToAll[value='true']").attr("checked",true);
						}else{
							$(":radio.publishToAll[value='false']").attr("checked",true);
						}
						pubScopeClick();
						$('#company').val(data.company);
						$('#rdn').hide();
						$('#rdnDisplay').html(data.rdn).show();
						$('#rdnHint').hide();
						$('#contactInfo').val(data.contactInfo);
						$('#description').val(data.description);
						$('#closedPrivSpan').show();
						$('input[name=priv][value='+data.priv+']').click();
						$('.error').html("");
						
						$('#modalTitle').html('<fmt:message key="develop.oauth.modify.title.update"/>');
					});
					$('#edit-popup').modal('show');  
				});
				
				//delete sth
				$('.deleteOauth').on('click',function(){
					var flag=confirm("<fmt:message key='develop.oauth.delete.confirm'/>");
					if(flag){
						var ldapId=$(this).data('ldapId');
						$.post("<umt:url value='/user/developer.do'/>",{id:ldapId,act:'deleteLdap'}).done(function(){
							window.location.reload();
						});
					}
				});
			});
		</script>
	</body>
</html>
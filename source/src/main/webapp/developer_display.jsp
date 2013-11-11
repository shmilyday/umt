<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
 <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>   
<fmt:setBundle basename="application" />
<umt:AppList/>
<umt:refreshUser/>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<title><fmt:message key='banner.accountSetting'/></title>
		<link href="<umt:url value="/images/favicon.ico"/>" rel="shortcut icon" type="image/x-icon" />
		<meta http-equiv="pragma" content="no-cache" />
		<meta http-equiv="cache-control" content="no-cache" />
	</head>
	<body class="login">
		<jsp:include flush="true" page="banner2013.jsp">
			<jsp:param name="DecludeMenu" value="true"/>
		</jsp:include>
		
		<div class="container login gray">
			<ul class="sub-nav">
				<li class="active"><a for="myOauth" href="#"><fmt:message key="develop.oauth.my.app"/></a></li>
				<li><a for="developDoc" href="#"><fmt:message key="develop.oauth.devlop.doc"/></a></li>
				<div class="clear"></div>
			</ul>
			<div class="sub-content" id="myOauth">
				<c:forEach items="${oauths }" var="oauth">
					<div class="oAuthList form-horizontal" data-json='{"thirdParty":"${oauth.thirdParty }","clientName":"${oauth.clientName }","id":"${oauth.id }","clientWebsite":"${oauth.clientWebsite }","description":"${oauth.description }","redirectURI":"${oauth.redirectURI }","applicant":"${oauth.applicant}","company":"${oauth.company }","contactInfo":"${oauth.contactInfo}","appType":"${oauth.appType }"}'>
						<h3>${oauth.clientName }  
							<c:choose>
								<c:when test="${oauth.status=='accept'}">  
									<span><fmt:message key="develop.oauth.status.accept"/></span>
								</c:when>
								<c:when test="${oauth.status=='apply'}">
									<span><fmt:message key="develop.oauth.status.apply"/></span>
								</c:when>
								<c:otherwise>
									<span><fmt:message key="develop.oauth.status.refuse"/></span>
								</c:otherwise>
							</c:choose>
							<button type="button" class="deleteOauth btn btn-small btn-link"><fmt:message key="develop.oauth.delete"/></button>
							<button type="button" class="updateOauth btn btn-small btn-info"><fmt:message key="develop.oauth.update"/></button>
						</h3>
						<c:if test="${ oauth.status=='accept'}">
						<div class="control-group">
			              	<label class="control-label nopadding">
								App Key:
							</label>
			              	<div class="controls">
			               		${oauth.clientId }
			              	</div>
			            </div>
						<div class="control-group">
			              	<label class="control-label nopadding">
								App secret:
							</label>
			              	<div class="controls">
			               		${oauth.clientSecret }
			              	</div>
			            </div>
			            </c:if>
			            
			            <div class="control-group">
			              	<label class="control-label nopadding">
								<fmt:message key='develop.oauth.modify.appType'/><fmt:message key='common.maohao'/>        	
							</label>
			              	<div class="controls">
			               		<c:choose>
			               			<c:when test="${ oauth.appType=='webapp'}">
			               				<fmt:message key='develop.oauth.modify.appType.webapp'/>
			               			</c:when>
			               			<c:when test="${ oauth.appType=='phoneapp'}">
			               				<fmt:message key='develop.oauth.modify.appType.phoneapp'/>
			               			</c:when>
			               		</c:choose>
			              	</div>
			            </div>
						<div class="control-group">
			              	<label class="control-label nopadding">
								<fmt:message key="develop.oauth.website"/>
							</label>
			              	<div class="controls">
			               		${oauth.clientWebsite }
			              	</div>
			            </div>
						<div class="control-group">
			              	<label class="control-label nopadding">
								<fmt:message key="develop.oauth.redirect.url"/>
							</label>
			              	<div class="controls">
			               		${oauth.redirectURI }
			              	</div>
			            </div>
						<div class="control-group">
			              	<label class="control-label nopadding">
								<fmt:message key="develop.oauth.description"/>
							</label>
			              	<div class="controls">
			               		${oauth.description }
			              	</div>
			            </div>
					</div>
				</c:forEach>
				<div class="addNewOauth">
					<button type="button" id="modifyDeveloperInfo" class="btn btn-primary btn-long"><fmt:message key='develop.oauth.add'/></button>
				</div>
			</div>
			<div class="sub-content" style="display:none" id="developDoc">  
				<jsp:include page="oauthDoc.jsp"/>
			</div>
			
		</div>
		<div id="edit-popup" data-backdrop="true" tabindex="-1" class="modal hide fade smallBottom" style="width:750px;">
			<div class="modal-header">
	           <button type="button" class="close" data-dismiss="modal">×</button>
	           <h3><span id="modalTitle"></span><fmt:message key="develop.oauth.modify.title"/></h3>
	        </div>
			<form method="post" id="editBasic" name="edit-baseinfo" class="form-horizontal no-bmargin" style="margin:0" action="<umt:url value='/user/developer.do'/>">
				<fieldset>
				<div class="modal-body">
					<input type="hidden" id="id" name="id" value="0" />
					<input type="hidden" id="act" name="act" value="addOauth" /> 
					<input type="hidden" id="thirdParty" name="thirdParty" value=""/>
					<div class="control-group">
	         			<label class="control-label"><fmt:message key="develop.oauth.modify.clientName"/></label>
	          			<div class="controls">
	            			<input maxlength="254" type="text" name="clientName" id="clientName" class="register-xlarge"/>
	            			<span class="error help-inline" id="clientName_error"></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key='develop.oauth.modify.appType'/><fmt:message key='common.maohao'/></label>
	          			<div class="controls">
	            			 <label style="display:inline;" for="appTypeWeb">
	           					<input type="radio" name="appType" checked="checked" id="appTypeWeb" value="webapp"/><fmt:message key='develop.oauth.modify.appType.webapp'/>
	            			 </label> 
	            			 <label style="display:inline;"  for="appTypePhone">
	           					<input type="radio" name="appType" id="appTypePhone" value="phoneapp" /><fmt:message key='develop.oauth.modify.appType.phoneapp'/>
	            			 </label>
	            			<span class="error help-inline" id="appType_error"></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key="develop.oauth.modify.website"/></label>
	          			<div class="controls">
	            			<input maxlength="250" type="text" name="clientWebsite" id="clientWebsite" class="register-xlarge"/>
	            			<span class="error help-inline" id="clientWebsite_error"></span>
	            			<p class="help-inline"><fmt:message key='develop.oauth.hint.website'/></p>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key="develop.oauth.redirect.url"/></label>
	          			<div class="controls">
	            			<input maxlength="250" type="text" name="redirectURI" id="redirectURI" class="register-xlarge"/>
	            			<span class="error help-inline" id="redirectURI_error"></span>
	            			<p class="help-inline"><fmt:message key='develop.oauth.hint.callbak'/></p>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key="develop.oauth.description"/></label>
	          			<div class="controls">
	            			<textarea name="description" id="description" style="width:25em; height:3em; resize:none;"></textarea>
	            			<span class="error help-inline" id="description_error"></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key="develop.oauth.modify.applicant"/></label>
	          			<div class="controls">
	            			<input name="applicant" type="text" id="applicant" class="register-xlarge"/>
	            			<span class="error help-inline" id="applicant_error"></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key="develop.oauth.modify.company"/></label>
	          			<div class="controls">
	            			<input name="company" type="text" id="company" class="register-xlarge"/>
	            			<span class="error help-inline" id="company_error"></span>
	          			</div>
	        		</div>
	        		<div class="control-group">
	         			<label class="control-label"><fmt:message key="develop.oauth.modify.contactInfo"/></label>
	          			<div class="controls">
	            			<input name="contactInfo" type="text" id="contactInfo" class="register-xlarge"/>
	            			<span class="error help-inline" id="contactInfo_error"></span>
	          			</div>
	        		</div>
				</div>
				<div class="modal-footer">
					<a data-dismiss="modal" class="btn" href="#"><fmt:message key="inputpassword.cancel"/></a>
					<button type="submit" class="btn btn-primary"><fmt:message key="userinfo.submit"/></button>
		        </div>
		        </fieldset>
	        </form>
		</div>
		<jsp:include flush="true" page="bottom2013.jsp"></jsp:include>
	</body>
	
	<script type="text/javascript" >
		$(document).ready(function(){
			// bind tab click
			$('.sub-nav>li>a').on('click',function(){
				$('.sub-nav>li').removeClass("active");
				$(this).parent().addClass("active");
				var subContentId=$(this).attr("for");
				$('.sub-content').hide();
				$('#'+subContentId).show();
			});
			//bind edit buttion click
			$('#modifyDeveloperInfo').on('click',function(){
				$('#id').val('0');
				$('#applicant').val('');
				$('#clientName').val('');
				$('#clientWebsite').val('');
				$('#company').val('');
				$('#contactInfo').val('');
				$('#description').val('');
				$('#redirectURI').val('');
				$('#act').val('addOauth');
				$('input[name=appType][value=webapp]').click();
				$('#thirdParty').val('');
				$('.error').html("");
				$('#modalTitle').html('<fmt:message key="develop.oauth.modify.title.add"/>');
				$('#edit-popup').modal('show');
			});
			//验证长度 
			$.validator.addMethod("startWith", function(value, element,params){ 
				var reg=new RegExp("^"+$('#'+params).val());     
			    return reg.test(value);
		    }, "<fmt:message key='develop.oauth.validate.startWith'/>");
			//form validator
			$('#editBasic').validate({
				rules:{
					applicant:{required:true,maxlength:45},
					clientName:{required:true,maxlength:45},
					clientWebsite:{required:true,url:true,maxlength:250},
					company:{required:true,maxlength:25},
					contactInfo:{required:true,maxlength:45},
					description:{required:true,maxlength:500},
					redirectURI:{required:true,url:true,startWith:'clientWebsite',maxlength:250}
				},
				messages:{
					applicant:{required:'<fmt:message key="develop.oauth.validate.applicant.required"/>',maxlength:'<fmt:message key="develop.oauth.validate.content.too.long"/>'},
					clientName:{required:'<fmt:message key="develop.oauth.validate.clientName.required"/>',maxlength:'内容超长'},
					clientWebsite:{required:'<fmt:message key="develop.oauth.validate.website.required"/>',url:'<fmt:message key="develop.oauth.validate.url.format"/>',maxlength:'<fmt:message key="develop.oauth.validate.content.too.long"/>'},
					company:{required:'<fmt:message key="develop.oauth.validate.company.required"/>',maxlength:'<fmt:message key="develop.oauth.validate.content.too.long"/>'},
					contactInfo:{required:'<fmt:message key="develop.oauth.validate.contactInfo.required"/>',maxlength:'<fmt:message key="develop.oauth.validate.content.too.long"/>'},
					description:{required:'<fmt:message key="develop.oauth.validate.description.required"/>',maxlength:'<fmt:message key="develop.oauth.validate.content.too.long"/>'},
					redirectURI:{required:'<fmt:message key="develop.oauth.validate.redirectUrl.required"/>',url:'<fmt:message key="develop.oauth.validate.url.format"/>',maxlength:'<fmt:message key="develop.oauth.validate.content.too.long"/>'}
				},
				errorPlacement: function(error, element){
					 var sub="_error";
					 var errorPlaceId="#"+$(element).attr("name")+sub;
					 	$(errorPlaceId).html("");
					 	error.appendTo($(errorPlaceId));
				}
				
			});
			//updateOauth button
			$('.updateOauth').on('click',function(){
				var data=($(this).parent().parent().data('json'));
				$('#id').val(data.id);
				$('#applicant').val(data.applicant);
				$('#clientName').val(data.clientName);
				$('#clientWebsite').val(data.clientWebsite);
				$('#company').val(data.company);
				$('#contactInfo').val(data.contactInfo);
				$('#description').val(data.description);
				$('#redirectURI').val(data.redirectURI);
				$('input[name=appType][value='+data.appType+']').click();
				$('#act').val('updateOauth');
				$('#thirdParty').val(data.thirdParty);
				$('.error').html("");
				$('#modalTitle').html('<fmt:message key="develop.oauth.modify.title.update"/>');
				$('#edit-popup').modal('show');  
			});
			//delete sth
			$('.deleteOauth').on('click',function(){
				var flag=confirm("<fmt:message key='develop.oauth.delete.confirm'/>");
				if(flag){
					var data=($(this).parent().parent().data('json'));
					$.post("<umt:url value='/user/developer.do'/>",{id:data.id,act:'deleteOauth'}).done(function(){
						window.location.reload();
					});
				}
			});
		});
	</script>
</html>
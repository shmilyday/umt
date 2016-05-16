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
				<c:if test="${empty oauths }">
					<h3 style="color:gray;text-align:center">
						<fmt:message key='empty.${viewType }.apps'></fmt:message>
					</h3>
				</c:if>
				<c:forEach items="${oauths }" var="oauth">
					<div class="oAuthList form-horizontal" data-json='{"thirdParty":"${oauth.thirdParty }","clientName":"${oauth.clientName }","id":"${oauth.id }","clientWebsite":"${oauth.clientWebsite }","description":"${oauth.description }","redirectURI":"${oauth.redirectURI }","applicant":"${oauth.applicant}","company":"${oauth.company }","contactInfo":"${oauth.contactInfo}","appType":"${oauth.appType }","enableAppPwd":"${oauth.enableAppPwd }"}'>
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
						
						<div class="appdescription">
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
			            
			            <div class="uploadbox">
			            <div class="uploadImageDiv" id="uploadImageDiv_${oauth.id }" data-oauth-id="${oauth.id }">
			           			<div class="img-XL">
			            			<img class="100p" style="float:left;width:100px;height:100px" src="${context }/logo?logoId=${oauth.logo100m100 }"/>
			            			<a id="upload-XL" class="uploadLogo" data-target="100p">上传</a>
			            			<p class="imgupload-text">
			            				上传大图,系统会自动压缩成其他尺寸
			            			</p>
			            			<p id="imgname-XL">(100*100)</p>
			            		</div>
			            		
			            		<div class="img-L">
			            			<img class="64p" style="width:64px;height:64px" src="${context }/logo?logoId=${oauth.logo64m64 }"/>
			            			<p  id="imgname-L">(64*64)</p>
			            			<a id="upload-L" class="uploadLogo" data-target="64p">上传</a>
			            			
			            		</div>
			            		
			            		<div class="img-M">
			            			<img class="32p" style="width:32px;height:32px" src="${context }/logo?logoId=${oauth.logo32m32 }"/>
			            			<p  id="imgname-M">(32*32)</p>
			            			<a id="upload-M" class="uploadLogo" data-target="32p">上传</a>
			            			
			            		</div>
			            	
			            		<div class="img-S">
			            			<img class="16p" style="width:16px;height:16px" src="${context }/logo?logoId=${oauth.logo16m16}"/>
			            			<p  id="imgname-S">(16*16)</p>
			            			<a id="upload-S"  class="uploadLogo" data-target="16p">上传</a>
			            			
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
		<jsp:include flush="true" page="editPopup.jsp"></jsp:include>
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
				//bind edit buttion click
				$('#modifyDeveloperInfo').on('click',showNewDialog);
				
				//updateOauth button
				$('.updateOauth').on('click',function(){
					var data=($(this).parent().parent().data('json'));
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
					$('#clientWebsite').val(data.clientWebsite);
					$('#company').val(data.company);
					$('#contactInfo').val(data.contactInfo);
					$('#description').val(data.description);
					$('#redirectURI').val(data.redirectURI);
					$('#closedPrivSpan').show();
					$('input[name=appType][value='+data.appType+']').click();
					$('#act').val('updateOauth');
					$('#thirdParty').val(data.thirdParty);
					$('.error').html("");
					$('#ldapAppType').hide();
					$('input[name=enableAppPwd][value='+data.enableAppPwd+']').click();
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
				//bind file uploader
				$('.uploadLogo').each(function(i,n){
					new qq.FileUploader({
						element : n,
				        template: '<div class="qq-uploader">' + 
		                '<div class="qq-upload-drop-area"><span>Drop files here to upload</span></div>' +
		                '<div class="qq-upload-button"><div id="uploadText">上传</div></div><br/>'+
		                '<ul style="display:none" class="qq-upload-list"></ul>' + 
		             '</div>',
						action : '${context}/user/developer.do?act=uploadImg',
						params:{
							'target':$(n).data('target'),
							'beanId':$(n).closest('div.uploadImageDiv').data('oauthId')
						},
						sizeLimit : 2*1024 * 1024,
						allowedExtensions:['gif','png','jpg'],
						multiple: false,
						maxConnections:1,
						onComplete : function(id, fileName, data) {
							if(data.success){
								for(var i=0;i<data.updatedList.length;i++){
									var updatedItem=data.updatedList[i];
									$('#uploadImageDiv_'+data.clientId+" img."+updatedItem.target).attr("src","${context}/logo?logoId="+updatedItem.clbId+"&_="+Math.random());
								} 
							}else{
								alert(data.desc);
							}
						},
						messages:{
				        	typeError:"请上传png,gif,jpg",
				        	emptyError:"请不要上传空文件",
				        	sizeError:"大小超过2M限制"
				        },
				        showMessage: function(message){
				        	alert(message);
				        },
				        onProgress: function(id, fileName, loaded, total){
				        	//$('#uploadText').html("<fmt:message key='configPhoto.uploadFile'/>("+Math.round((loaded/total)*100)+"%)");
				        },
				        multiple:false
					});
				});
			});
		</script>
	</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>

<fmt:setBundle basename="application" />	

<%
	pageContext.setAttribute("ContextPath", request.getContextPath());
%>


<div class="content-container">
	<div class="toolBar">		
		<fmt:message key="emailtemplate.edittitle"/>
	</div>
	
		<ul class="sub-nav"> 
			<li id="setParameter_tab"><a id="mailConfigBtn" href="${ContextPath}/admin/editTemplate.do?act=setParameter"><fmt:message key="emailtemplate.sendparameter"></fmt:message> </a>
			</li>
			<li id="register_tab">
			<a class="mailTempBtn" href="${ContextPath}/admin/editTemplate.do?act=register"><fmt:message key="emailtemplate.useregister"></fmt:message> </a>
			</li>
			<li id="approve_tab"><a class="mailTempBtn" href="${ContextPath}/admin/editTemplate.do?act=approve"><fmt:message key="emailtemplate.userapprove"></fmt:message> </a>
			</li>
			<li id="deny_tab"><a class="mailTempBtn" href="${ContextPath}/admin/editTemplate.do?act=deny"><fmt:message key="emailtemplate.userdeny"></fmt:message> </a>
			</li>
			<li id="password_tab"><a class="mailTempBtn" href="${ContextPath}/admin/editTemplate.do?act=password"><fmt:message key="emailtemplate.passmodify"></fmt:message> </a></li>
		<div class="clear"></div>
		</ul>
		
		<div class="sub-content">
		<form id="mailConfigForm" action="editTemplate.do" method="post" class="form-horizontal" style="margin-top:10px">
			<input type="hidden" name="reAct" value="${act }"/>
			<div id="mailConfigCnt">
				<input type="hidden" name="act" value="saveConfig" />
				 <div class="control-group">
						 <label class="control-label" >
							<fmt:message key="emailtemplate.email"></fmt:message></label>
						  <div class="controls">
							<input type="text" name="email" value="${config.email}" />
						</div>
						</div>
				 <div class="control-group">
					<label class="control-label" >
							<fmt:message key="emailtemplate.pass"></fmt:message>
						</label>
						<div class="controls">
							<input type="text" name="password"
								value="${config.password}" />
						</div>
						</div>
					
					<div class="control-group">
						<label class="control-label" >
							<fmt:message key="emailtemplate.smtp"></fmt:message>
						</label>
						<div class="controls">
							<input type="text" name="smtp" value="${config.smtp}" />
						</div>
						</div>
                       	<div  class="control-group">
				<div class="controls"><button type="submit" class="btn btn-primary" id="mailConfigSave" ><fmt:message key="emailtemplate.save"/></button>
				</div>
			</div>
				</div>
			
			
		</form>

		<form id="mailTempForm" action="editTemplate.do" method="post"class="form-horizontal">
		<input type="hidden" name="reAct" value="${act }"/>
			<div id="mailTempCnt" >
				<input type="hidden" name="act" value="saveTemplate" />
				<input type="hidden" name="target" value="${template.target}" />
				
					<div class="control-group">
						<label class="control-label" >
							<fmt:message key="emailtemplate.title"></fmt:message>
						</label>
						<div class="controls">
							<input type="text" name="title" value="${template.title}"/>
						</div>
					</div>
					<div class="control-group">
						<label class="control-label" >
							<fmt:message key="emailtemplate.content"></fmt:message>
						</label>
						<div class="controls">
							<textarea name="content" cols="100" rows="15">${template.content}</textarea>
						</div>
					</div>
				<div class="control-group">
				  <div class="controls">
				    <button type="submit" id="mailTempSave" class="btn btn-primary"><fmt:message key="emailtemplate.save"/></button>
			        </div>
			     </div>
			   </div>
		</form>
</div>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		
		var tabtype="";
		tabtype='${tabtype}';
		
		$("#mailTempCnt").hide();
		$("#mailConfigCnt").show();
		
		if(tabtype=='parameter'){
			$("#mailTempCnt").hide();
			$("#mailConfigCnt").show();
		} else {
			$("#mailConfigCnt").hide();
			$("#mailTempCnt").show();
		}
		
		var succ='${succ}';
		if(succ!='') {
			alert('<fmt:message key="${succ}"/>');
		}
		
		$("a.mailTempBtn").live('click',function(){
			$("#mailConfigCnt").hide();
			$("#mailTempCnt").show();
		});

		$("#mailConfigBtn").click(function(){
			$("#mailTempCnt").hide();
			$("#mailConfigCnt").show();
		});
		$(".nav li.current").removeClass("current");
		$("#emailTemplateMenu").addClass("current");
		$('#${act}_tab').addClass("active"); 
	});
</script>

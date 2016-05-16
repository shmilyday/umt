<%@ page language="java" pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />
<script type="text/javascript" src="../js/zebra.js"></script>
<script type="text/javascript" src="../js/string.js"></script>
<script type="text/javascript" src="../js/resize.js"></script>
<script type="text/javascript" src="<umt:url value="/thirdparty/bootstrap/js/bootstrap.min.js"/>"></script>
<script type="text/javascript"  src="../js/jquery.validate.min.js"></script>
<script type="text/javascript"  src="../js/ext_validate.js"></script>
<script type="text/javascript"  src="../js/jquery.tmpl.min.js"></script>
<script id="detailUserInfo" type="text/x-jquery-tmpl" >
<tr>
	<td>UMTID:</td>
	<td>{{= umtId}}</td>
</tr>
<tr>
	<td>姓名2:</td>
	<td>{{= trueName}}</td>
</tr>

<tr>
	<td><fmt:message key='admin.usermanage.cstnetId'/>:</td>
	<td>{{= cstnetId}}({{= primaryLoginName.status}})</td>
</tr>
{{if secondaryLoginName}}
<tr>
	<td><fmt:message key='admin.usermanage.secondary'/>:</td>
	<td>
		<table>
		<tr>
			{{each secondaryLoginName}}
				{{= $value.loginName}}({{= $value.status}})<br/>
			{{/each}}
		</tr>
		</table>
	</td>
</tr>
{{/if}}
{{if securityEmail}}
<tr>
	<td><fmt:message key='admin.usermanage.security'/>:</td>
	<td>{{= securityEmail}}</td>
</tr>
{{/if}}
{{if createTime}}
	<tr>
		<td><fmt:message key='admin.usermanage.regist.time'/>:</td>
		<td>{{= createTime}}</td>
	</tr>
{{/if}}

{{if logs}}
<tr>
	<td><fmt:message key='admin.usermanage.login.log'/>:</td>
	<td>
		<table border="1px" style="text-align:center">
		{{each logs}}
			<tr>
				<td>{{= $value.appName}}</td>
				<td>{{= $value.ip}}</td>
				<td>{{= $value.occurTime}}</td>
				<td>{{= $value.country}}</td>
				<td>{{= $value.city}}</td>
			</tr>
		{{/each}}
		</table>
		
	</td>
</tr>
{{/if}}
</script>
<script type="text/javascript">
	var message={
		msgSelectUser:"<fmt:message key='usermanage.message.selectuser'/>",
		msgOneUser:"<fmt:message key='usermanage.message.onuser'/>",
		msgConfirmRemove:"<fmt:message key='usermanage.message.confirmremove'/>",
		tleCreate:"<fmt:message key='usermanage.create.title'/>",
		msgCheckPassword:"<fmt:message key='usermanage.message.passwordcheck'/>",
		tleModify:"<fmt:message key='usermanage.modify.title'/>",
		tleReset:"<fmt:message key='usermanage.reset.title'/>",
		msgPageInput:"<fmt:message key='pagebean.msg.integer'/>"
	};
	function toggleAll(){
		$("input[name='userids']").attr("checked", $('#toggler').attr("checked"));
	}
	function gotoPage(page){
		if (page==null){
			page=$('#pageinput').attr('value');
			if (!/^[0-9]*[1-9][0-9]*$/.test(page)){
				alert(message.msgPageInput);
				return;
			}
			page=page-1;
		}
		document.changePage.page.value=page;
		document.changePage.submit();
	}
	function showCreateDialog(){
		document.createUnit.reset();
		$("#CreateUnitDialog").modal('show');
	}
	function showPasswordDialog(){
		var count=0;
		var username;
		$.each($('input[name=userids]'), function(i, checkbox){
			if ($(checkbox).attr('checked')){
				count++;
				username=$(checkbox).attr('cstnetId');
			}
		});
		if (count==0){
			alert(message.msgSelectUser);
			return;
		}
		if (count>1){
			alert(message.msgOneUser);
			return;
		}
		document.resetPassword.reset();
		document.resetPassword.username.value=username;
		$('#resetpasspword').modal('show');
	}
	
	function searchUser(v, cont){
		$.get("manageUnit.do?act=search",{q:v},
			function(obj){
				var res=[];
				for (var i=0;i<obj.length;i++){
					res.push({id:obj[i].u, value:obj[i].u, info:obj[i].t});
				};
				cont(res);
			},'json');
	}
	function removeCheckedUnit(){
		var ids="";
		var count=0;
		$.each($("input[name='unitids']"), function(i, checkbox){
			if ($(checkbox).attr("checked")){
				count++;
				if(count==1){
					ids+=$(checkbox).val();
				}else{
					ids+=","+$(checkbox).val();
				}
			}
		});
		if (count==0){
			alert("请选择删除的单位");
			return;
		};
		removeUnit(ids);
	}
	
	function removeUnit(ids){
		if (confirm("确定删除？")){
			document.mainform.removeIds.value=ids;
			document.mainform.submit();
		};
	}
	
	
	function sumitSearch(){
		document.showuser.submit();
	}
	$(document).ready(function(){
		$('#btnSearch').click(sumitSearch);
		$('#toggler').click(toggleAll);
		$('#createBtn').click(showCreateDialog);
		$('#resetBtn').click(showPasswordDialog);
		$('#removeBtn').click(removeCheckedUnit);
		$('.removeBtn').click(function(){
			removeUnit($(this).attr("unitId"));
		});
		$('#lockBtn,#normalBtn,#limitBtn').click(function(){
			var $checkedItem=$("input[name='userids']:checked");
			if($checkedItem.size()==0){
				alert(message.msgSelectUser);
				return;
			}
			var result=$.map($checkedItem, function(item){
				return $(item).attr("value");
			});
			
			var toStatus=$(this).data('toStatus');
			var toStatusDesc=$(this).data('toStatusDesc');
			$.post("manageUnit.do?act=updateAccountStatus",{"userIds[]":result,"toStatus":toStatus}).done(function(){
				$checkedItem.parent().parent().find("td.account_status").html(toStatusDesc);
			});
		});
		$('.modifyBtn').on('click',function(){
			
			var unitId=$(this).attr("unitId");
			
			$.ajax({
				url:"manageUnit.do?act=loadUnit",
				type:"POST",
				data:{'id':unitId},
				dataType:"json",
				beforeSend:function(){}, 
				complete:function(){
				},
				error:function(){},
				success:function(data){
					var result=data.result;
					if(result==true){
						var unitDomain=data.data;
						var form = document.modifyform;
						form.id.value=unitDomain.id;  
						form.name.value=unitDomain.name;  
						form.enName.value=unitDomain.enName;
						form.rootDomain.value=unitDomain.rootDomain;
						form.mailDomain.value=unitDomain.mailDomain;
						form.symbol.value=unitDomain.symbol;
						$('#modifydialog').modal("show"); 
					}
				}
			});
		});
		
		
				
		resizeWindow();
		$(".nav li.current").removeClass("current");
		$("#unitMenu").addClass("current");
		
		
		(function(allId,itemClass){
			var $all=$('#'+allId);
			var $items=$('.'+itemClass);
			$all.on('click',function(){
				var checked=$all.attr("checked");
				$items.each(function(i,n){
					if(checked){
						$(n).attr("checked","checked");
					}else{
						$(n).removeAttr("checked");
					}
				});
			});
			$items.on('click',function(){
				var allSelect=true;
				$items.each(function(i,n){
					if($(n).attr("checked")){
						allSelect&=true;
					}else{
						allSelect&=false;	
					}
				});
				if(allSelect){
					$all.attr("checked","checked");
				}else{
					$all.removeAttr("checked");
				}
			});
			
		})("toggler","checkUserId");
		
		
		$('#createUnitForm').validate({
			 submitHandler :function(form){
				 form.submit();
			 },
			 rules: {
				 name:{
					 required:true,
					 remote:{
						 type: "GET",
						 url: '',
						 data:{ 
							 'act':'checkName',
							 "name":function(){
											return $("#name").val();
								}	   		
				  		}
					 }
			 	 },
				 rootDomain:{
					 required:true,
					 remote:{
						 type: "GET",
						 url: '',
						 data:{ 
							 'act':'checkRootDomain',
							 "rootDomain":function(){
											return $("#rootDomain").val();
								}	   		
				  		}
					 }
					 },
				 mailDomain:{required:true},
			 },
			 messages: {
				 name: {
					 required:'单位名称不能为空',
					 remote:'单位已存在'
				 },
				 rootDomain:{required:'主域不能为空',
					 remote:'主域已经存在'
				},
				 mailDomain:{
					 required:'邮箱域不能为空',
				 }
			 },
			 errorPlacement: function(error, element){
				 var sub="_error";
				 var errorPlaceId="#"+$(element).attr("name")+sub;
				 	$(errorPlaceId).html("");
				 	error.appendTo($(errorPlaceId));
			}
		});
		$('.detailUserInfo').on('click',function(){
			$.get('manageUnit.do?act=loadDetailUserInfo',{"uid":$(this).data('uid')}).done(function(data){
				$('#detailUserContent').html($('#detailUserInfo').tmpl(data));
				$('#detailUserModal').modal('show');
			});

		});
	});
</script>
<div class="content-container">
	<div class="toolBar">
		<fmt:message key="usermanage.title" />
		<div class="fright">
		<form class="form-search" style="margin:0" action="manageUnit.do" method="post"
			name="showuser">
			<input type="hidden" name="act" value="showUnits" /> 
			<input
				class="input-medium" id="query" name="query" type="text"
				maxlength="255"
				value="<c:out value='${PageBean.query}' escapeXml='true'/>" /> 
				<button id="btnSearch" class="btn btn-warning"  style="margin:-6PX 10px 0 0;" type="button" ><fmt:message key="common.search"/></button> 
				<input type="submit" style="display: none" />
		</form>
		 <button type="button"
				id="createBtn" class="fright btn btn-primary" style="margin-top:0PX;"
				value="添加单位" >添加单位</button>
		</div>
	</div>
		    
	<div class="userTable">	
		<form action="manageUnit.do" method="post" name="mainform">
			<input type="hidden" name="act" value="removeUnit" /> 
			<input type="hidden" name="removeIds" value="" /> 
			<input type="hidden" name="page" value="${PageBean.currentPage}" />
			<input type="hidden" name="total" value="${PageBean.size}" />
			<table id="sessions" width="100%" border="0" class="table table-hover">
				<tr>
					<td align="left" valign="middle"><input type="checkbox" id="toggler" /></td>
					<td>ID</td>
					<td>单位名称</td>
					<td>英文名称</td>
					<td>单位缩写</td>
					<td>主域</td>
					<td>邮箱域</td>
					<td>操作</td>
				</tr>
				<c:if test="${PageBean.items!=null}">
					<c:forEach items="${PageBean.items}" var="unit" varStatus="status">
						<tr>
							<td><input type="checkbox" class="checkUserId" name="unitids"  value="${unit.id}" /></td>
							<td>${unit.id}</td>
							<td>${unit.name}</td>
							<td>${unit.enName}</td>
							<td>${unit.symbol}</td>
							<td>${unit.rootDomain}</td>
							<td>${unit.mailDomain}</td>
							<td>
								<button type="button"  class="btn btn-primary removeBtn"  unitId="${unit.id }"><fmt:message key='usermanage.btn.remove'/></button>
							    <button type="button"  class="btn btn-primary modifyBtn" unitId="${unit.id }" ><fmt:message key='usermanage.btn.modify'/></button>
							</td>
						</tr>
						<c:if test="${status.last}">
							<c:set var="count" value="${status.count}" scope="page" />
						</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${count==null}">
					<c:set var="count" value="0" scope="page" />
				</c:if>
			</table>
		</form>
</div>
		<div id="pagecontrol">
			<c:if test="${PageBean.pageCount>1}">
				<fmt:message key="pagebean.stauts">
					<fmt:param value="${PageBean.pageCount}" />
					<fmt:param value="${PageBean.currentPage+1}" />
				</fmt:message>&nbsp;&nbsp;
							<c:if test="${PageBean.firstPage==true}">
					<span style="color: #d0d0d0;margin-right:0px;"><span><fmt:message
							key="pagebean.page.first" /></span>
							<span> <fmt:message
							key="pagebean.page.prevous" /></span></span>
				</c:if>
				<c:if test="${!PageBean.firstPage==true}">
					<a href="#" onclick="gotoPage(0)"><fmt:message
							key="pagebean.page.first" /> </a>
					<a href="#" onclick="gotoPage(${PageBean.currentPage-1})"><fmt:message
							key="pagebean.page.prevous" /> </a>
				</c:if>
				<c:if test="${PageBean.lastPage==true}">
					<span style="color: #d0d0d0;margin-right:0;"><span><fmt:message
							key="pagebean.page.next" /></span> <span> <fmt:message
							key="pagebean.page.last" /></span> 
					</span>
				</c:if>
				<c:if test="${!PageBean.lastPage==true}">
					<a href="#" onclick="gotoPage(${PageBean.currentPage+1})"><fmt:message
							key="pagebean.page.next" /> </a>
					<a href="#" onclick="gotoPage(${PageBean.pageCount-1})"><fmt:message
							key="pagebean.page.last" /> </a>
				</c:if>
				<input type="text"  id="pageinput" />
				<input type="button" onclick="gotoPage()"  class="but btn-large" value="GO" />
			</c:if>
		</div>
		<div class="clearboth but-center">
		<form action="manageUnit.do" method="post" name="changePage">
			<input type="hidden" name="act" value="showUnits" /> <input
				type="hidden" name="page" /> <input type="hidden" name="total"
				value="${PageBean.size}" /> <input type="hidden" name="query"
				value="<c:out value="${PageBean.query}" escapeXml="true"/>" /> <input
				type="submit" style="display: none" />
		</form>

			<button type="button" id="removeBtn" class="btn btn-primary" ><fmt:message key='usermanage.btn.remove'/></button>
  </div>
</div>
<div id="CreateUnitDialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
			<h4 id="operateTitle">新加单位</h4>
		</div>
		<form id="createUnitForm" action="manageUnit.do" method="post" name="createUnit">
		<div class="modal-body">
			<p id="content">
			
		<input type="hidden" name="act" value="createUnit" />
		<input type="hidden" name="total" value="${PageBean.size}" />
		<input type="hidden" name="page" value="${PageBean.currentPage}" />
		<input type="submit" style="display: none" />
		<table>
			<tr>
				<td class="rtl">单位名称
				</td>
				<td><input type="text" name="name" id="name"/>
				<span id="name_error"></span>
				</td>
			</tr>
			<tr>
				<td class="rtl">英文名称
				</td>
				<td><input type="text" name="enName"  id="enName"/><span id="enName_error"></span>
				</td>
			</tr>
			<tr>
				<td class="rtl">单位缩写
				</td>
				<td><input type="text" name="symbol" id="symbol"/><span id="symbol_error"></span></td>
			</tr>
			<tr>
				<td class="rtl">主域</td>
				<td><input type="text" name="rootDomain"  id="rootDomain"/><span id="rootDomain_error"></span></td>
			</tr>
			<tr>
				<td class="rtl">邮件域</td>
				<td><input type="text" name="mailDomain"  id="mailDomain"/><span id="mailDomain_error"></span></td>
			</tr>
		</table>
		</div>
		<div class="modal-footer">
			<input type="submit" class="btn btn-primary"  value="<fmt:message key='common.submit' />" />
			<a class="btn" data-dismiss="modal" aria-hidden="true" id="no"> <fmt:message key="inputpassword.cancel"/></a>
		</div>
		</form>
</div>
<div id="modifydialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<h4 id="operateTitle"><fmt:message key="admin.modify.user"/></h4>
		</div>
		<form name="modifyform" method="post" action="manageUnit.do">
		<input type="hidden" name="act" value="modify" /> <input
			type="hidden" name="page" value="${PageBean.currentPage}" /> <input
			type="hidden" name="query"
			value="<c:out value="${PageBean.query}" escapeXml="true"/>" /> <input
			type="hidden" name="total" value="${PageBean.size}" /> <input
			type="submit" style="display: none" />
			<input type="hidden" name="id"  />
		<div class="modal-body">
			<p id="content">
			<table>
			<tr>
				<td class="rtl">单位名称
				</td>
				<td><input type="text" name="name" /></td>
			</tr>
			<tr>
				<td class="rtl">英文名称
				</td>
				<td><input type="text" name="enName" /></td>
			</tr>
			<tr>
				<td class="rtl">单位简写
				</td>
				<td><input type="text" name="symbol" /></td>
			</tr>
			<tr>
				<td class="rtl">主域
				</td>
				<td><input type="text" name="rootDomain" /></td>
			</tr>
			<tr>
				<td class="rtl">邮件域
				</td>
				<td><input type="text" name="mailDomain" /></td>
			</tr>
		</table>
			</p>
		</div>
		<div class="modal-footer">
			<input type="submit" class="btn btn-primary"  value="<fmt:message key="common.submit"/>"/>
			<a class="btn" data-dismiss="modal" aria-hidden="true" id="no"> <fmt:message key="inputpassword.cancel"/></a>
		</div>
		</form>
</div>



<div id="detailUserModal" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
			<h4 id="operateTitle">用户详细信息</h4>
		</div>
		<div class="modal-body">
			<table id="detailUserContent">
			
			</table>
		</div>
		<div class="modal-footer">
			<a class="btn" data-dismiss="modal" aria-hidden="true" id="no"> <fmt:message key="inputpassword.cancel"/></a>
		</div>
</div>


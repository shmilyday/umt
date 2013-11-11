<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
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
		document.createUser.reset();
		$("#CreateUserDialog").modal('show');
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
		$.get("manageUser.do?act=search",{q:v},
			function(obj){
				var res=[];
				for (var i=0;i<obj.length;i++){
					res.push({id:obj[i].u, value:obj[i].u, info:obj[i].t});
				};
				cont(res);
			},'json');
	}
	function removeUser(){
		var count=0;
		$.each($("input[name='userids']"), function(i, checkbox){
			if ($(checkbox).attr("checked")){
				count++;
			}
		});
		if (count==0){
			alert(message.msgSelectUser);
			return;
		};
		if (confirm(message.msgConfirmRemove)){
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
		$('#removeBtn').click(removeUser);
		$('#modifyBtn').on('click',function(){
			var count=0;
			var username;
			$.each($("input[name='userids']"), function(i, checkbox){
				if ($(checkbox).attr("checked")){
					count++;
					username = $(checkbox).attr("value");
				}
			});
			if (count==0){
				alert(message.msgSelectUser);
				return;
			};
			$.get("manageUser.do?act=loadUser&q="+username, function(obj){
				obj=eval(obj);
				var form = document.modifyform;
				form.username.value=obj[0].username;  
				form.truename.value=obj[0].truename;
				$('#modifydialog').modal("show"); 
			});
		});
		
		
				
		resizeWindow();
		$(".nav li.current").removeClass("current");
		$("#manageUserMenu").addClass("current");
		
		
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
		
		
		$('#createUserForm').validate({
			 submitHandler :function(form){
				 form.submit();
			 },
			 rules: {
				 username:{
					 required:true,
					 email:true,
					 remote:{
						 type: "GET",
						 url: '../createRequest.do',
						 data:{ 
							 'act':'usercheck',
							 "username":function(){
											return $("#username").val();
								}	   		
				  		}
					 }
			 	 },
				 truename:{required:true},
				 password:{required:true,minlength:8,passwordNotEquals:{
					 notEquals:function(){
						 return $('#username').val();
					 }},
					 passwordAllSmall:true,
					 passwordAllNum:true,
					 passwordAllBig:true,
					 passwordHasSpace:true},
				 retype:{required:true,equalTo:"#password"}
			 },
			 messages: {
				 username: {
					 required:'<fmt:message key="common.validate.email.required"/>',
					 email:'<fmt:message key="common.validate.email.invalid"/>',
					 remote:'<fmt:message key="regist.user.exist"/>'
				 },
				 truename:{required:'<fmt:message key="common.validate.truename.required"/>'},
				 password:{
					 required:'<fmt:message key="common.validate.password.required"/>',
					 minlength:'<fmt:message key="common.validate.password.minlength"/>',
					 passwordNotEquals:'<fmt:message key="common.validate.password.notEuals"/>',
					 passwordAllSmall:'<fmt:message key="common.validate.password.allSmall"/>',
					 passwordAllNum:'<fmt:message key="common.validate.password.allNumber"/>',
					 passwordAllBig:'<fmt:message key="common.validate.password.allBig"/>',
					 passwordHasSpace:'<fmt:message key="common.validate.password.hasSpace"/>'
					 
				 },
				 retype:{
					 required:'<fmt:message key="common.validate.repassword.required"/>',
					 minlength:'<fmt:message key="common.validate.repassword.minlength"/>',
					 equalTo:'<fmt:message key="common.validate.password.retype.not.equals"/>'
				 }
			 },
			 errorPlacement: function(error, element){
				 var sub="_error";
				 var errorPlaceId="#"+$(element).attr("name")+sub;
				 	$(errorPlaceId).html("");
				 	error.appendTo($(errorPlaceId));
			}
		});
		
	});
</script>
<div class="content-container">
	<div class="toolBar">
		<fmt:message key="usermanage.title" />
		<div class="fright">
		<form class="form-search" style="margin:0" action="manageUser.do" method="post"
			name="showuser">
			<input type="hidden" name="act" value="showUsers" /> <input
				class="input-medium" id="query" name="query" type="text"
				maxlength="255"
				value="<c:out value='${PageBean.query}' escapeXml='true'/>" /> 
				<button id="btnSearch" class="btn btn-warning"  style="margin:-6PX 10px 0 0;" type="button" ><fmt:message key="common.search"/></button> 
				<input type="submit" style="display: none" />
		</form>
		 <button type="button"
				id="createBtn" class="fright btn btn-primary" style="margin-top:0PX;"
				value="<fmt:message key='usermanage.btn.createuser'/>" ><fmt:message key='usermanage.btn.createuser'/></button>
		</div>
	</div>
		    
	<div class="userTable">	
		<form action="manageUser.do" method="post" name="mainform">
			<input type="hidden" name="act" value="removeUser" /> <input
				type="hidden" name="page" value="${PageBean.currentPage}" /> <input
				type="hidden" name="total" value="${PageBean.size}" />
			<table id="sessions" width="100%" border="0" class="table table-hover">
				<tr>
					
					<td align="left" valign="middle"><input type="checkbox"
						id="toggler" /></td>
					<td>UMT ID</td>
					<td><fmt:message key="usermanage.list.username" /></td>
					<td><fmt:message key="usermanage.list.truename" /></td>
					<td><fmt:message key="usermanage.list.email" /></td>
					<td>type</td>
				</tr>
				<c:if test="${PageBean.items!=null}">
					<c:forEach items="${PageBean.items}" var="user" varStatus="status">
						<tr>
							<td><input type="checkbox" class="checkUserId" name="userids"
								cstnetId="${user.cstnetId}" value="${user.id}" /></td>
								<td>${user.umtId}</td>
							<td>${user.cstnetId}</td>
							<td>${user.trueName}</td>
							<td>${user.cstnetId}</td>
							<td>${user.type}</td>
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
							<c:if test="${PageBean.firstPage}">
					<span style="color: #d0d0d0;margin-right:0px;"><span><fmt:message
							key="pagebean.page.first" /></span>
							<span> <fmt:message
							key="pagebean.page.prevous" /></span></span>
				</c:if>
				<c:if test="${!PageBean.firstPage}">
					<a href="#" onclick="gotoPage(0)"><fmt:message
							key="pagebean.page.first" /> </a>
					<a href="#" onclick="gotoPage(${PageBean.currentPage-1})"><fmt:message
							key="pagebean.page.prevous" /> </a>
				</c:if>
				<c:if test="${PageBean.lastPage}">
					<span style="color: #d0d0d0;margin-right:0;"><span><fmt:message
							key="pagebean.page.next" /></span> <span> <fmt:message
							key="pagebean.page.last" /></span> 
					</span>
				</c:if>
				<c:if test="${!PageBean.lastPage}">
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
		<form action="manageUser.do" method="post" name="changePage">
			<input type="hidden" name="act" value="showUsers" /> <input
				type="hidden" name="page" /> <input type="hidden" name="total"
				value="${PageBean.size}" /> <input type="hidden" name="query"
				value="<c:out value="${PageBean.query}" escapeXml="true"/>" /> <input
				type="submit" style="display: none" />
		</form>

		<button type="button" id="removeBtn" class="btn btn-primary"
			 ><fmt:message key='usermanage.btn.remove'/></button>
			 <button type="button" id="resetBtn" class="btn btn-primary"
			><fmt:message key='usermanage.btn.resetpassword'/></button>
			 <button 
			type="button" id="modifyBtn" class="btn btn-primary"
			><fmt:message key='usermanage.btn.modify'/></button>

      </div>
	
</div>
<div id="CreateUserDialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
			<h4 id="operateTitle"><fmt:message key="admin.create.user"/></h4>
		</div>
		<form id="createUserForm" action="manageUser.do" method="post" name="createUser">
		<div class="modal-body">
			<p id="content">
			
		<input type="hidden" name="act" value="createUser" />
		<input type="hidden" name="total" value="${PageBean.size}" />
		<input type="hidden" name="page" value="${PageBean.currentPage}" />
		<input type="submit" style="display: none" />
		<table>
			<tr>
				<td class="rtl"><fmt:message key="usermanage.list.username" />
				</td>
				<td><input type="text" name="username" id="username"/>
				<span id="username_error"></span>
				</td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="usermanage.list.truename" />
				</td>
				<td><input type="text" name="truename"  id="truename"/><span id="truename_error"></span>
				</td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="usermanage.form.password" />
				</td>
				<td><input type="password" name="password" id="password"/><span id="password_error"></span></td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="usermanage.form.retype" /></td>
				<td><input type="password" name="retype"  id="retype"/><span id="retype_error"></span></td>
			</tr>
		</table>
		</div>
		<div class="modal-footer">
			<input type="submit" class="btn btn-primary"  value="<fmt:message key="common.submit"/>"/>
			<a class="btn" data-dismiss="modal" aria-hidden="true" id="no"> <fmt:message key="inputpassword.cancel"/></a>
		</div>
		</form>
</div>
<div id="modifydialog" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
			<h4 id="operateTitle"><fmt:message key="admin.modify.user"/></h4>
		</div>
		<form name="modifyform" method="post" action="manageUser.do">
		<input type="hidden" name="act" value="modify" /> <input
			type="hidden" name="page" value="${PageBean.currentPage}" /> <input
			type="hidden" name="query"
			value="<c:out value="${PageBean.query}" escapeXml="true"/>" /> <input
			type="hidden" name="total" value="${PageBean.size}" /> <input
			type="submit" style="display: none" />
		<div class="modal-body">
			<p id="content">
			<table>
			<tr>
				<td class="rtl"><fmt:message key="usermanage.list.username" />
				</td>
				<td><input type="text" name="username" readonly /></td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="usermanage.list.truename" />
				</td>
				<td><input type="text" name="truename" /></td>
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

<div id="resetpasspword" class="modal hide fade" tabindex="-1" role="dialog" data-backdrop='true' aria-hidden="true">
		<div class="modal-header">
			<!-- <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button> -->
			<h4 id="operateTitle"><fmt:message key="remindpass.input.title"/></h4>
		</div>
		<form name="resetPassword" method="post" action="manageUser.do">
		<input type="hidden" name="act" value="resetPassword" /> <input
			type="hidden" name="page" value="${PageBean.currentPage}" /> <input
			type="hidden" name="query"
			value="<c:out value="${PageBean.query}" escapeXml="true"/>" /> <input
			type="hidden" name="total" value="${PageBean.size}" /> <input
			type="submit" style="display: none" />
		<div class="modal-body">
			<p id="content">
			<table>
			<tr>
				<td class="rtl"><fmt:message key="usermanage.list.username" />
				</td>
				<td><input type="text" name="username" readonly /></td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="usermanage.form.password" />
				</td>
				<td><input type="password" name="password" /></td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="usermanage.form.retype" /></td>
				<td><input type="password" name="retype" /></td>
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


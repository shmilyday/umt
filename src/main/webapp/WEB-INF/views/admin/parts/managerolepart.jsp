<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />
<script type="text/javascript" src="../js/jquery-ui-1.7.2.custom.min.js"></script>
<script type="text/javascript" src="../js/zebra.js"></script>
<script type="text/javascript" src="../js/string.js"></script>
<script type="text/javascript" src="../js/formcheck.js"></script>
<script type="text/javascript" src="../js/resize.js"></script>
<script type="text/javascript" src="../js/jquery.autocomplete-1.4.2.js"></script>

<link href="../css/cupertino/jquery-ui-1.7.2.custom.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function addMember(){
		document.addform.reset();
		$('#addmember').dialog('open');
		$('#query').autocomplete({ajax_get:searchUser,autowidth:true,cache:false,onreturn:sumitSearch});
	}
	
	function sumitSearch(){
		document.addform.submit();
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
	
	function removeMember(){
		if ($("input[name='usernames']").length<2){
			alert("<fmt:message key='privilege.message.lastuser'/>");
			return;
		}
		
		
		var count=0;
		$.each($("input[name='usernames']"), function(i, input){
			if ($(input).attr('checked')){
				count++;
			}
		});
		if (count==0){
			alert("<fmt:message key='privilege.message.selectuser'/>");
			return;
		}
		
		
		if (confirm("<fmt:message key='privilege.message.confirmremove'/>")){
			document.memberform.submit();
		};
	}
	function toggle(){
		var val = $('#toggler').attr("checked");
		$.each($("input[name='usernames']"), function(i, input){
			$(input).attr('checked',val);
		});
	}
	$(document).ready(function(){
		$('#toggler').click(toggle);
		$('#btnRemove').click(removeMember);
		$('#btnAdd').click(addMember);
		$(".bluetable").zebraTable({
			topClass:'top',
			evenClass:'blue_tr',
		 	oddClass:'',
		 	firstClass:'firstline'});
		$("#addmember").dialog({
			'position':['center', 100],
			'bgiframe':true,
			'autoOpen':false,
			'width':400,
			'height':170,
			'modal':true,
			'title':'<fmt:message key='privilege.dialog.addtitle'/>',
			'buttons':{
				"<fmt:message key='privilege.dialog.cancel'/>":function(){
					$("#addmember").dialog('close');
				},
				"<fmt:message key='privilege.dialog.submit'/>":function(){
					var form = document.addform;
					if (form.username.value==""){
						alert("<fmt:message key='privilege.message.usenamerequire'/>");
					}else{
						$("#addmember").dialog('close');
						form.submit();
					}
				}
			}
		});
		resizeWindow();
	});
</script>
<div class="content-container">
	<div class="toolBar">

		<fmt:message key='privilege.title' />
	</div>
	<div class="userTable">
		<fmt:message key="privilege.message.description" />
		<form action="manageRole.do" method="post" name="memberform">
			<input type="hidden" name="act" value="remove" />
			<table class="table table-hover" border="0" width="100%">
				<tr>
					<th width="32"><input type="checkbox" id="toggler" /></th>
					<th><fmt:message key="privilege.list.username" /></th>
					<th><fmt:message key="privilege.list.truename" /></th>
					<th><fmt:message key="privilege.list.email" /></th>
				</tr>
				<c:if test="${users!=null}">
					<c:forEach var="user" varStatus="status" items="${users}">
						<tr>
							<td align="center"><input type="checkbox" name="usernames"
								value="${user.cstnetId}" /></td>
							<td>${user.cstnetId}</td>
							<td>${user.trueName}</td>
							<td>${user.securityEmail}</td>
						</tr>
						<c:if test="${status.last}">
							<c:set var="count" value="${status.count}" scope="page" />
						</c:if>
					</c:forEach>
				</c:if>

				<umt:Loop end="5" start="${count}">
					<tr>
						<td></td>
						<td></td>
						<td></td>
						<td></td>
					</tr>
				</umt:Loop>
			</table>
			<div class="buttons">
				<input type="button" id="btnRemove"
					value="<fmt:message key="privilege.btn.remove"/>" /> <input
					type="button" id="btnAdd"
					value="<fmt:message key="privilege.btn.add"/>" />
			</div>
		</form>
	</div>
</div>
<div id="addmember" class="dialog">
	<form name="addform" method="post" action="manageRole.do">
		<input type="hidden" name="act" value="add" />
		<table>
			<tr>
				<td class="rtl"><fmt:message key="privilege.list.username" /></td>
				<td><input type="text" id="query" name="username" /></td>
			</tr>
		</table>
	</form>
</div>

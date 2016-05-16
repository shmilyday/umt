<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />

<script type="text/javascript" src="../js/jquery-ui-1.7.2.custom.min.js"></script>
<script type="text/javascript" src="../js/string.js"></script>
<script type="text/javascript" src="../js/zebra.js"></script>
<script type="text/javascript" src="../js/formcheck.js"></script>
<script type="text/javascript" src="../js/resize.js"></script>
<script type="text/javascript" src="../js/Validiator.js"></script>
<link href="../css/cupertino/jquery-ui-1.7.2.custom.css"
	rel="stylesheet" type="text/css" />

<script type="text/javascript">
	var message={
		btnSubmit:'<fmt:message key="appmanage.create.btnsubmt"/>',
		btnCancel:'<fmt:message key="appmanage.create.btncancel"/>',
		createApp:'<fmt:message key="appmanage.create.title"/>',
		noSelect:'<fmt:message key="appmanage.updatekey.noselect"/>',
		multiSelect:'<fmt:message key="appmanage.updatekey.multiselected"/>',
		noFile:'<fmt:message key="appmanage.updatekey.nofile"/>',
		fileFormat:'<fmt:message key="appmanage.updatekey.fileformat"/>',
		updateKeyTitle:'<fmt:message key="appmanage.updatekey.title"/>',
		confirmRemove:'<fmt:message key="appmanage.remove.confirm"/>',
		removeNoSelect:'<fmt:message key="appmanage.remove.noselect"/>',
		msgPageInput:'<fmt:message key="pagebean.msg.integer"/>',
		selectToModify:'<fmt:message key="appmanage.selectToModify"/>'
	}
	function changePage(pg){
		if (pg==null){
			pg = document.changepage.page.value;
			if (!form_check_integer(pg)){
				alert(message.msgPageInput);
				return;
			}
		}else{
			document.changepage.page.value=pg;
		}
		
		document.changepage.submit();
	}
	function showModify(){
		var appid=0;
		var count=0;
		$("input[name='appids']").each(function(i, input){
			if ($(input).attr('checked')){
				count++;
				appid=$(input).attr('value');
			}
		});
		if (count!=1){
			alert(message.selectToModify);
			return;
		}
		
		var buttons={
			"<fmt:message key="appmanage.create.btncancel"/>":function(){
				$("#appdialog").dialog('close');
			},
			"<fmt:message key="appmanage.create.btnsubmt"/>":function(){
				$("#appdialog").dialog('close');
				document.createapp.submit();
			}
		};
		$("#appdialog").dialog('option', 'title', message.createApp);
		$("#appdialog").dialog('option', 'buttons', buttons);
			
		$.get("manageApplication.do?act=loadApp",{q:appid},
			function(obj){
				var form = document.createapp;
				form.act.value="modifyApp";
				form.appid.value=appid;
				form.name.value=obj[0].appname;
				form.description.value=obj[0].description;
				form.url.value=obj[0].url;
				form.serverType.value=obj[0].serverType;
				form.allowOperate.value=obj[0].allowOperate;
				$('#modifydialog').dialog('open');
		},'json');
		
		$("#appdialog").dialog('open');
	}
	
	function showAddDialog(){
		var buttons={
			"<fmt:message key="appmanage.create.btncancel"/>":function(){
				$("#appdialog").dialog('close');
			},
			"<fmt:message key="appmanage.create.btnsubmt"/>":function(){
				$("#appdialog").dialog('close');
				document.createapp.submit();
			}
		};
		document.createapp.act.value="add";
		document.createapp.reset();
		$("#appdialog").dialog('option', 'title', message.createApp);
		$("#appdialog").dialog('option', 'buttons', buttons);
		$("#appdialog").dialog('open');
	}
	
	function showKeyDialog(){
		var count=0;
		var appid=-1;
		$.each($("input[name='appids']"), function(i, input){
			if ($(input).attr('checked')){
				count++;
				appid=$(input).attr('value');
			}
		});
		if (count==0){
			alert(message.noSelect);
			return;
		}
		if (count>1){
			alert(message.multiSelect);
			return;
		}
		
		var buttons={
			"<fmt:message key="appmanage.create.btncancel"/>":function(){
				$("#keydialog").dialog('close');
			},
			"<fmt:message key="appmanage.create.btnsubmt"/>":function(){
				var filename=$("#keyfile").attr("value");
				if (filename==""){
					alert(message.noFile);
					return;
				}
				if (!filename.toLowerCase().endWith(".txt")){
					alert(message.fileFormat);
					return;
				}
				
				$("#keydialog").dialog('close');
				document.keyform.submit();
			}
		};
		$("#keyappid").attr("value", appid);
		$("#keydialog").dialog('option', 'title', message.updateKeyTitle);
		$("#keydialog").dialog('option', 'buttons', buttons);
		$("#keydialog").dialog('open');
	}
	
	function removeApp(){
		var count=0;
		$.each($("input[name='appids']"), function(i, obj){
			if ($(obj).attr("checked"))
				count++;
		});
		if (count>0){
			if (confirm(message.confirmRemove))
				doSubmit('remove');
		}else{
			alert(message.removeNoSelect);
		}
	}
	
	function toggleSelect(){
		$("input[name='appids']").attr("checked", $('#toggleAll').attr("checked") );
	}
	
	function doSubmit(act){
		document.appform.act.value=act;
		document.appform.submit();
	}

	$(document).ready(function(){
		$('#toggleAll').click(toggleSelect);
		$('#btnremove').click(removeApp);
		$('#btnadd').click(showAddDialog);
		$('#btnupdate').click(showKeyDialog);
		$('#btnModify').click(showModify);
		
		$('#appdialog').dialog({
			'position':['center', 100],
			'bgiframe':true,
			'autoOpen':false,
			'width':400,
			'height':270,
			'modal':true
		});
		$('#keydialog').dialog({
			'position':['center', 100],
			'bgiframe':true,
			'autoOpen':false,
			'width':350,
			'height':170,
			'modal':true
		});
		$(".bluetable").zebraTable({
			topClass:'top',
			evenClass:'blue_tr',
		 	oddClass:'',
		 	firstClass:'firstline'});
		resizeWindow();
	});
</script>
<div class="content-container">
	<div class="toolBar">
		<fmt:message key="appmanage.title" />
	</div>
	<div class="clearboth red">
		<c:if test="${error!=null}">

			<fmt:message key="${error}"></fmt:message>

		</c:if>
	</div>
	<form action="manageApplication.do" method="post"
		style="display: inline; padding: 0; margin: 0;" name="appform">
		<input type="hidden" name="act" />
		<div class="clearboth">
			<input type="button" class="btn  btn-primary" id="btnremove"
				value="<fmt:message key='appmanage.btn.remove'/>" /> <input
				type="button" id="btnadd" class="btn  btn-primary"
				value="<fmt:message key='appmanage.btn.add'/>" /> <input
				type="button" id="btnModify" class="btn  btn-primary"
				value="<fmt:message key='appmanage.btn.modify'/>" /> <input
				type="button" id="btnupdate" class="btn  btn-primary"
				value="<fmt:message key='appmanage.btn.updatekey'/>" />
		</div>
		<div class="userTable" style="height: 430px;">
			<table class="table table-hover">
				<tr>
					<td><input type="checkbox" id="toggleAll" /></td>
					<td><fmt:message key="appmanage.appname" /></td>
					<td><fmt:message key="appmanage.descript" /></td>
					<td><fmt:message key="appmanage.url" /></td>
					<td><fmt:message key="appmanage.type" /></td>
					<td><fmt:message key="appmanage.allowOperate" /></td>
					<td><fmt:message key="appmanage.pubkey" /></td>
				</tr>
				<c:if test="${PageBean.items!=null}">
					<c:forEach var="app" varStatus="status" items="${PageBean.items}">
						<tr>
							<td><input type="checkbox" name="appids" value="${app.id}" />
							</td>
							<td>${app.name}</td>
							<td>${app.description}</td>
							<td><a href="${app.url}">${app.url}</a></td>
							<td>${app.serverType}</td>
							<td><c:if test="${app.allowOperate}">
									<img src="../images/ok.gif" />
								</c:if></td>
							<td><c:if test="${app.keyid!=0}">
									<a target="blank" href="../pubkey?keyid=${app.keyid}"><fmt:message
											key="appmanage.download" /></a>
								</c:if></td>
						</tr>
						<c:if test="${status.last}">
							<c:set var="count" value="${status.count}" scope="page" />
						</c:if>
					</c:forEach>
				</c:if>
				<c:if test="${count==null}">
					<c:set var="count" value="0" />
				</c:if>
				<umt:Loop end="5" start="${count}">
					<tr>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
						<td>&nbsp;</td>
					</tr>
				</umt:Loop>
			</table>
		</div>
	</form>
	<div id="pagecontrol">
		<form action="manageApplication.do" name="changepage" method="post">
			<input type="hidden" name="act" value="showApps" /> <input
				type="hidden" name="total" value="${PageBean.size}" />
			<c:if test="${PageBean.pageCount>1}">
				<fmt:message key="pagebean.stauts">
					<fmt:param value="${PageBean.pageCount}" />
					<fmt:param value="${PageBean.currentPage+1}" />
				</fmt:message>
				<c:if test="${PageBean.firstPage}">
					<span style="color: #d0d0d0"><fmt:message
							key="pagebean.page.first" /> <fmt:message
							key="pagebean.page.prevous" /></span>
				</c:if>
				<c:if test="${!PageBean.firstPage}">
					<a href="#" onclick="changePage(0)"><fmt:message
							key="pagebean.page.first" /></a>
					<a href="#" onclick="changePage(${PageBean.currentPage-1})"><fmt:message
							key="pagebean.page.prevous" /></a>
				</c:if>
				<c:if test="${PageBean.lastPage}">
					<span style="color: #d0d0d0"> <fmt:message
							key="pagebean.page.next" /> <fmt:message key="pagebean.page.last" /></span>
				</c:if>
				<c:if test="${!PageBean.lastPage}">
					<a href="#" onclick="changePage(${PageBean.currentPage+1})"><fmt:message
							key="pagebean.page.next" /></a>
					<a href="#" onclick="changePage(${PageBean.pageCount-1})"><fmt:message
							key="pagebean.page.last" /></a>
				</c:if>
				<input type="text" style="width: 30px" size="5" name="page" />
				<input type="button" value="GO" onclick="changePage()" />
			</c:if>
		</form>
	</div>

</div>
<div id="appdialog" class="dialog">
	<form name="createapp" action="manageApplication.do" method="post">
		<input type="hidden" name="act" value="add" /> <input
			type="hidden" name="page" value="${PageBean.currentPage}" /> <input
			type="hidden" name="total" value="${PageBean.size}" /> <input
			type="hidden" name="appid" value="0" /> <input type="submit"
			style="display: none" onclick="return Validator.Validate(this,3)" />
		<table width="100%">
			<tr>
				<td class="rtl"><fmt:message key="appmanage.appname" /></td>
				<td><input type="text" name="name" required='true'
					datatype='alpha'
					message="<fmt:message key='appmanage.create.appnameformat'/>" /></td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="appmanage.descript" /></td>
				<td><input type="text" name="description" /></td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="appmanage.url" /></td>
				<td><input name="url" type="text" required='true'
					datatype='url'
					message="<fmt:message key='appmanage.create.urlrequired'/>" /></td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="appmanage.type" /></td>
				<td><select name="serverType">
						<option value="JSP" selected>JSP</option>
						<option value="PHP">PHP</option>
						<option value="ASP">ASP/.Net</option>
				</select></td>
			</tr>
			<tr>
				<td class="rtl"><fmt:message key="appmanage.allowOperate" /></td>
				<td><select name="allowOperate">
						<option value="false" selected>
							<fmt:message key="appmanage.deny" />
						</option>
						<option value="true">
							<fmt:message key="appmanage.allow" />
						</option>
				</select></td>
			</tr>
		</table>
	</form>
</div>
<div id="keydialog" class="dialog">
	<form name="keyform" action="manageApplication.do" method="post"
		enctype="multipart/form-data">
		<input type="hidden" name="act" value="updatekey" /> <input
			type="hidden" name="total" value="${PageBean.size}" /> <input
			type="hidden" name="appid" id="keyappid" /> <input type="hidden"
			name="page" value="${PageBean.currentPage}" /> <input type="submit"
			style="display: none" />
		<table width="100%">
			<tr>
				<td class="rtl"><fmt:message key="appmanage.updatekey.keyfile" />
				</td>
				<td><input type="file" id="keyfile" name="keyfile"
					style="width: 180px" /></td>
			</tr>
		</table>
	</form>
</div>

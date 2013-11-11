<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />

<script type="text/javascript" src="../js/zebra.js"></script>
<script type="text/javascript" src="../js/string.js"></script>
<script type="text/javascript" src="../js/formcheck.js"></script>
<link href="../css/cupertino/jquery-ui-1.7.2.custom.css"
	rel="stylesheet" type="text/css" />
<script type="text/javascript">
	function discard(){
		document.saveform.act.value="discard";
		document.saveform.submit();
	}
	
	function create(){
		document.saveform.act.value="create";
		document.saveform.submit();
	}
	
	$(document).ready(function(){
		$('#submitButton').click(create);
		$('#cancelbutton').click(discard);
		$(".bluetable").zebraTable({
			topClass:'top',
			evenClass:'blue_tr',
		 	oddClass:'',
		 	firstClass:'firstline'});
	});
</script>
<div class="content_body">
	<div class="body_img">
		<img src="../images/jt_05.gif" />
		<span class="blue_font"><fmt:message key='batch.title' /> </span>
	</div>
	<div class="body_bg">
		<c:if test="${PageBean!=null}">
			<form action="batchCreate.do" method="post" name="saveform">
				<input type="hidden" name="act" value="discard" />
				<img src="../images/warn.png" />
				<fmt:message key='batch.message.existswarn' />
				<table class="bluetable" border="1" width="100%">
					<tr>
						<th width="32"></th>
						<th>
							<fmt:message key="batch.list.username" />
						</th>
						<th>
							<fmt:message key="batch.list.truename" />
						</th>
						<th>
							<fmt:message key="batch.list.email" />
						</th>
						<th>
							<fmt:message key="batch.list.password" />
						</th>
					</tr>
					<c:forEach var="user" varStatus="status" items="${PageBean.items}">
						<tr>
							<td align="center">
								<c:if test="${user.exists}">
									<img src="../images/warn.png" />
								</c:if>
							</td>
							<td>
								${user.username}
							</td>
							<td>
								${user.trueName}
							</td>
							<td>
								${user.email}
							</td>
							<td>
								${user.password}
							</td>
						</tr>
						<c:if test="${status.last}">
							<c:set var="count" value="${status.count}" scope="page" />
						</c:if>
					</c:forEach>
					<umt:Loop end="5" start="${count}">
						<tr>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
					</umt:Loop>
				</table>
				<c:if test="${PageBean.pageCount>1}">
					<div id="pagecontrol">
						<fmt:message key="pagebean.stauts">
							<fmt:param value="${PageBean.pageCount}" />
							<fmt:param value="${PageBean.currentPage+1}" />
						</fmt:message>
						<c:if test="${PageBean.firstPage}">
							<a><fmt:message key='pagebean.page.first' /> </a>
							<a><fmt:message key='pagebean.page.prevous' /> </a>
						</c:if>
						<c:if test="${!PageBean.firstPage}">
							<a href="batchCreate.do?act=changePage&page=0"><fmt:message
									key='pagebean.page.first' /> </a>
							<a
								href="batchCreate.do?act=changePage&page=${PageBean.currentPage-1}"><fmt:message
									key='pagebean.page.prevous' /> </a>
						</c:if>
						<c:if test="${PageBean.lastPage}">
							<a><fmt:message key='pagebean.page.next' /> </a>
							<a><fmt:message key='pagebean.page.last' /> </a>
						</c:if>
						<c:if test="${!PageBean.lastPage}">
							<a
								href="batchCreate.do?act=changePage&page=${PageBean.currentPage+1}"><fmt:message
									key='pagebean.page.next' /> </a>
							<a
								href="batchCreate.do?act=changePage&page=${PageBean.pageCount-1}"><fmt:message
									key='pagebean.page.last' /> </a>
						</c:if>
						<input type="text" id="pageinput" style="width:30px">
						<input type="button" onclick="gotoPage()" value="GO" />
					</div>
					<script type="text/javascript">
						function gotoPage(page){
							if (page==null){
								page = $('#pageinput').attr('value');
								if (!form_check_integer(page)){
									alert('<fmt:message key="pagebean.msg.integer"/>');
									return;
								}
								page=page-1;
							}
							document.changePage.page.value=page;
							document.changePage.submit();
						}
					</script>
				</c:if>
				<div class="buttons">
					<input type="button" id="submitButton"
						value="<fmt:message key='batch.form.submit'/>" />
					<input type="button" id="cancelbutton"
						value="<fmt:message key='batch.form.cancel'/>" />
				</div>
			</form>
			<form action="batchCreate.do" method="post" name="changePage">
				<input type="hidden" name="act" value="changePage" />
				<input type="hidden" name="page" />
			</form>
		</c:if>
	</div>
</div>

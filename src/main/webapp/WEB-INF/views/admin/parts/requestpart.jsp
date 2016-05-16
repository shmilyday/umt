<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>

<fmt:setBundle basename="application" />

<script type="text/javascript" src="../js/zebra.js"></script>
<script type="text/javascript" src="../js/resize.js"></script>
<script type="text/javascript">
	var selectmessage="<fmt:message key='requests.message.selectrequest'/>";
	function gotoPage(page){
		var form = document.requestForm;
		form.page.value=page;
		form.act.value="showRequests";
		form.submit();
	}
	
	function approve(){
		var count=0;
		$.each($("input[name='rid']"), function(i, radio){
			if ($(radio).attr('checked')){
				count++;
			}
		});
		if (count==0){
			alert(selectmessage);
			return;
		}
		
		var form = document.requestForm;
		form.act.value="approve";
		form.submit();
	}
	
	function deny(){
		var count=0;
		$.each($("input[name='rid']"), function(i, radio){
			if ($(radio).attr('checked')){
				count++;
			}
		});
		if (count==0){
			alert(selectmessage);
			return;
		}
		
		var form = document.requestForm;
		form.act.value="deny";
		form.submit();
	}
	function remove(){
		var count=0;
		$.each($("input[name='rid']"), function(i, radio){
			if ($(radio).attr('checked')){
				count++;
			}
		});
		if (count==0){
			alert(selectmessage);
			return;
		}
		
		var form = document.requestForm;
		form.act.value="remove";
		form.submit();
	}
	
	$(document).ready(function(){
		$('#approveBtn').click(approve);
		$('#rejectBtn').click(deny);
		$('#removeBtn').click(remove);
		$('.messagebar').fadeOut(1000);
		$('#requests').zebraTable({
			topClass:'top',
			evenClass:'blue_tr',
		 	oddClass:'',
		 	firstClass:'firstline'});
		resizeWindow();
	});
</script>
<div class="content-container">
	<div class="toolBar">	
		<fmt:message key="requests.title" />		
	</div>
	
		<c:if test="${message!=null}">
			<div class="clearboth red">
				${message}
			</div>
		</c:if>
		<form action="manageRequests.do" name="requestForm" method="post">
			<input type="hidden" name="act" />
			<div class="clearboth red">
			<c:if test="${PageBean.size==0}">
							<fmt:message key="requests.message.norequest" />
						</c:if>
			</div>
			<div >
						<c:if test="${PageBean.size>0}">
							<table  id="requests" border="0" width="100%">
								<tr>
									<td align="left" valign="middle"></td>
									<td>
										<fmt:message key="requests.list.username" />
									</td>
									<td>
										<fmt:message key="requests.list.truename" />
									</td>
									<td>
										<fmt:message key="requests.list.email" />
									</td>
									<td>
										<fmt:message key="requests.list.createtime" />
									</td>
									<td>
										<fmt:message key="requests.list.contact" />
									</td>
									<td>
										<fmt:message key="requests.list.orgnization" />
									</td>
								</tr>
								<c:if test="${PageBean.items!=null}">
									<c:forEach var="ur" items="${PageBean.items}"
										varStatus="status">
										<c:if test="${status.last}">
											<c:set var="count" value="${status.count}" scope="page" />
										</c:if>
										<tr>
											<td>
												<input type="radio" name="rid" value="${ur.id}" />
											</td>
											<td>
												${ur.username}
											</td>
											<td>
												${ur.truename }
											</td>
											<td>
												${ur.email}
											</td>
											<td>
												<fmt:formatDate value="${ur.createTime}"
													pattern="yyyy-MM-dd hh:mm:ss" />
											</td>
											<td>
												${ur.phonenumber}
											</td>
											<td>
												${ur.orgnization}
											</td>
										</tr>
									</c:forEach>
								</c:if>
								<c:if test="${count==null}">
									<c:set var="count" value="0" scope="page" />
								</c:if>
								<umt:Loop end="5" start="${count}">
									<tr>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
										<td></td>
									</tr>
								</umt:Loop>
							</table>
						</c:if>
		     </div>
				
				<c:if test="${PageBean.size>0}">
					
							<input type="hidden" name="total" value="${PageBean.size}" />
							<input type="hidden" name="page" value="${PageBean.currentPage}" />
							<c:if test="${PageBean.pageCount>1}">
								<div id="pagecontrol">
								<fmt:message key="pagebean.stauts">
									<fmt:param value="${PageBean.pageCount}" />
									<fmt:param value="${PageBean.currentPage+1}" />
								</fmt:message>
									<c:if test="${PageBean.firstPage}">
										<a><fmt:message key="pagebean.page.first" />
										</a>
										<a><fmt:message key="pagebean.page.prevous" />
										</a>
									</c:if>
									<c:if test="${!PageBean.firstPage}">
										<a href="#" onclick="gotoPage(0)"><fmt:message
												key="pagebean.page.first" />
										</a>
										<a href="#" onclick="gotoPage(${PageBean.currentPage-1})"><fmt:message
												key="pagebean.page.prevous" />
										</a>
									</c:if>
									<c:if test="${PageBean.lastPage}">
										<a><fmt:message key="pagebean.page.next" />
										</a>
										<a><fmt:message key="pagebean.page.last" />
										</a>
									</c:if>
									<c:if test="${!PageBean.lastPage}">
										<a href="#" onclick="gotoPage(${PageBean.currentPage+1})"><fmt:message
												key="pagebean.page.next" />
										</a>
										<a href="#" onclick="gotoPage(${PageBean.size-1})"><fmt:message
												key="pagebean.page.last" />
										</a>
									</c:if>
								</div>
							</c:if>
					
					<div class="clearboth but-center">
							<input type="button" id="approveBtn"
								value="<fmt:message key='requests.btn.approve'/>" />
							<input type="button" id="rejectBtn"
								value="<fmt:message key='requests.btn.deny'/>" />
							<input type="button" id="removeBtn"
								value="<fmt:message key='requests.btn.remove'/>" />
					</div>
				</c:if>
			
			
		</form>
	

</div>
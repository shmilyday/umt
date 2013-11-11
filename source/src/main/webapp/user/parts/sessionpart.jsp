<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<fmt:setBundle basename="application" />
<script type="text/javascript" src="../js/zebra.js"></script>
<script type="text/javascript">
<!--
	$(document).ready(function(){
		$(".bluetable").zebraTable({
			topClass:'top',
			evenClass:'blue_tr',
		 	oddClass:'',
		 	firstClass:'firstline'});
	});
//-->
</script>
<div class="content-container">
	<div class="toolBar">
		<fmt:message key="sessions.title"/>
	</div>
	
	    <div class="clearboth red">
		<c:if test="${records==null}">
			<fmt:message key="sessions.norecord"/>
		</c:if>
		</div>
		<div >
		<c:if test="${records!=null}">
			<table id="sessions" width="100%" border="0">
				<tr>
					<th>
						<fmt:message key="sessions.appname"/>
					</th>
					<th>
						<fmt:message key="sessions.appurl"/>
					</th>
					<th>
						<fmt:message key="sessions.logintime"/>
					</th>
					<th>
						<fmt:message key="sessions.userip"/>
					</th>
					<th></th>
				</tr>
				<c:forEach items="${records}" var="record">
					<tr>
						<td>
							${record.appdesc}
						</td>
						<td>
							${record.appurl}
						</td>
						<td>
							<fmt:formatDate value="${record.logintime}"
								pattern="yyyy-MM-dd hh:mm:ss" />
						</td>
						<td>
							${record.userip}
						</td>
						<td><a href="userSessions.do?act=logout&id=${record.id}"><fmt:message key="sessions.logout"/></a></td>
					</tr>
				</c:forEach>
			</table>
		</c:if>
	</div>
</div>

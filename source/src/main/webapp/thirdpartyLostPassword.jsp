<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
  </head>
  
  <body>
     <table>
       <tr>
         <td>
          <c:choose>
	        <c:when test="${tip=='onlyCstnetUserLoginMail'}">
	            只有通过科技网邮箱登陆的用户才能访问邮件系统！
	        </c:when>
	        <c:when test="${tip=='onlyCstnetUserLoginOnlineStorage'}">
	            只有通过科技网邮箱登陆的用户才能访问网络硬盘！
	        </c:when>
	        <c:when test="${tip=='ssoLostPassword'}">
	           请注销重新使用科技网邮箱登录！<a href="../logout?WebServerURL=${WebServerURL}">注销</a>
	        </c:when>
	     </c:choose>
         </td>
       </tr>
     </table>
    
  </body>
</html>

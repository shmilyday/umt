<%@page import="cn.vlabs.umt.services.user.UserService"%>
<%@page import="cn.vlabs.umt.common.mail.MessageSender"%>
<%@page import="cn.vlabs.umt.services.account.IAccountDAO"%>
<%@page import="cn.vlabs.umt.common.job.impl.DiffrentRegisterJob"%>
<%@page import="cn.vlabs.umt.common.job.JobThread"%>
<%@page import="cn.vlabs.umt.services.user.utils.ServiceFactory"%>
<%@page import="java.util.Date"%>
<%@page import="cn.vlabs.umt.domain.UMTLog"%>
<%@page import="cn.vlabs.umt.domain.GEOInfo"%>
<%@page import="cn.vlabs.umt.ui.UMTContext"%>
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jstl/fmt_rt" prefix="fmt"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="WEB-INF/tld/umt.tld" prefix="umt"%>
<%
	UMTContext context=new UMTContext(request);
String ip=request.getParameter("ip");
UMTLog umtLog=new UMTLog();
umtLog.setEventType("login");
umtLog.setAppName("测试");
umtLog.setAppUrl("");
umtLog.setUid(context.getCurrentUMTUser().getId());
umtLog.setUserIp(ip);
umtLog.setOccurTime(new Date());
umtLog.setBrowserType("");
umtLog.setRemark("");
IAccountDAO ad=(IAccountDAO)UMTContext.getFactory().getBean("AccountDAO");
UserService us=(UserService)UMTContext.getFactory().getBean("UserService");
MessageSender mailSender=(MessageSender)UMTContext.getFactory().getBean("Email");
ad.log(umtLog);
JobThread.addJobThread(new DiffrentRegisterJob(mailSender, us,ad, umtLog.getUid(),umtLog.getId()));
%>

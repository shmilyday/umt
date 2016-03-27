/*
VERSION:	escience 
AUTHOR:		Vera. zhangshixiang@cnic.cn / shivera2004@163.com
DATE:		Jan, 2013
*/


/* CommonBanner*/
$(document).ready(function(){
	commonSimpleFooter();
});

function commonSimpleFooter(){
	var isZh=(!umtLocale||umtLocale=='zh_CN');
	var html='';
	if(isZh){
		html='<div class="dface container footer">'+
		'	<ul class="simple-footer-nav">'+
		'		<li id="footer-logo"><a target="_blank" href="http://www.escience.cn"></a></li>'+
		'		<li><a target="_blank" href="http://ddl.escience.cn" target="_blank">文档库</a></li>'+
		'		<li><a target="_blank" href="http://csp.escience.cn" target="_blank">会议服务平台</a></li>'+
		'		<li><a target="_blank" href="http://www.escience.cn/people" target="_blank">科研主页</a></li>'+
		'		<li><a target="_blank" href="http://www.escience.cn/site" target="_blank">中国科技网资源导航</a></li>'+
		'		<li><a target="_blank" href="http://mail.escience.cn" target="_blank">邮箱</a></li>'+
		'		<li><a target="_blank" href="http://dc.cstnet.cn" target="_blank">桌面会议系统</a></li>'+
		'		<li><a target="_blank" href="http://www.weibo.com/dcloud" target="_blank">官方微博</a></li>'+
		'		<li><a target="_blank" href="http://www.escience.cn/aboutus.html" target="_blank">关于我们</a></li>'+
		'		<div class="clear"></div>'+
		'	</ul>'+
		'	<p>'+
		'		Powered by'+
		'		<a target="_blank" href="http://duckling.escience.cn/"> Duckling 3.0 </a>'+
		'		<span id="app-version"></span> （京ICP备09112257号-1 京公网安备11010802017084）'+
		'	</p>'+
		'	<p>'+
		'		Copyright © 2007-2015'+
		'		<a target="_blank" href="http://www.cstnet.net.cn/">中国科技网</a>'+
		'		All Rights Reserved '+
		'	</p>'+
		'</div>';
	}else{
		html='<div class="dface container footer">'+
		'	<p>'+
		'		Powered by'+
		'		<a target="_blank" href="http://duckling.escience.cn/"> Duckling 3.0 </a>'+
		'		<span id="app-version"></span> （京ICP备09112257号-1 京公网安备11010802017084）'+
		'	</p>'+
		'	<p>'+
		'		Copyright © 2007-2015'+
		'		<a target="_blank" href="http://www.cstnet.net.cn/">CSTNET</a>'+
		'		All Rights Reserved '+
		'	</p>'+
		'</div>';
	}
	
	$("#footer").append(html);
}

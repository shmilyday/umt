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
	$("body").append(		
			'<div class="dface container footer">'+
			'	<ul class="simple-footer-nav">'+
			'		<li id="footer-logo"><a target="_blank" href="http://www.escience.cn"></a></li>'+
			'		<li><a target="_blank" href="http://ddl.escience.cn" target="_blank">文档库</a></li>'+
			'		<li><a target="_blank" href="http://csp.escience.cn" target="_blank">会议服务平台</a></li>'+
			'		<li><a target="_blank" href="http://www.escience.cn/people" target="_blank">科研主页</a></li>'+
			'		<li><a target="_blank" href="http://www.escience.cn/site" target="_blank">中国科技网资源导航</a></li>'+
			'		<li><a target="_blank" href="http://mail.escience.cn" target="_blank">邮箱</a></li>'+
			'		<li><a target="_blank" href="http://rol.escience.cn" target="_blank">实验室信息系统</a></li>'+
			'		<li><a target="_blank" href="http://www.weibo.com/dcloud" target="_blank">官方微博</a></li>'+
			'		<li><a target="_blank" href="http://www.escience.cn/aboutus.html" target="_blank">关于我们</a></li>'+
			'		<div class="clear"></div>'+
			'	</ul>'+
			'	<p>'+
			'		Powered by'+
			'		<a target="_blank" href="http://duckling.escience.cn/"> Duckling 3.0 </a>'+
			'		<span id="app-version"></span> （京ICP备09112257号-1）'+
			'	</p>'+
			'	<p>'+
			'		Copyright © 2007-2013'+
			'		<a target="_blank" href="http://www.cstnet.net.cn/">中国科技网</a>'+
			'		All Rights Reserved '+
			'	</p>'+
			'</div>'
	);
}

/*
VERSION:	escience 
AUTHOR:		Vera. zhangshixiang@cnic.cn / shivera2004@163.com
DATE:		Jan, 2013
*/


/* CommonBanner*/
$(document).ready(function(){
	commonFooter();
});
function commonFooter(){
	$("body").append(		
			'<div class="footer">'+
			'	<div class="container">'+
			'		<div class="footer-left">'+
			'			<a class="cstnet-logo"></a>'+
			'			<p>中国科技网是中国科学院领导下的学术性、非盈利的科研计算机网络。</p>'+
			'			<p>'+
			'				<span class="escience-logo-s"></span>是中国科技网面向科研学者的科研应用服务门户。'+
			'			</p>'+
			'			<p style="margin-top:20px;">'+
			'				Copyright © 2007-2013 <a href="http://www.cstnet.net.cn/" target="_blank">中国科技网</a>  All Rights Reserved'+
			'			</p>'+
			'			<p>Powered by'+
			'				<a href="http://duckling.escience.cn/" target="_blank"> Duckling 3.0 </a> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 京ICP备09112257号-1'+
			'			</p>'+
			'		</div>'+
			'		<div class="footer-mid">'+
			'			<a class="footer-logo" href="http://www.escience.cn" target="_blank"></a>'+
			'			<ul class="footer-nav">'+
			'				<li><a href="http://ddl.escience.cn" target="_blank">文档库</a></li>'+
			'				<li><a href="http://www.escience.cn/people/" target="_blank">科研主页</a></li>'+
			'				<li><a href="http://csp.escience.cn" target="_blank">会议服务平台</a></li>'+
			'				<li><a href="http://mail.escience.cn" target="_blank">邮箱</a></li>'+
			'				<li><a href="http://www.escience.cn/site" target="_blank">中国科技网资源导航</a></li>'+
			'				<li><a href="http://dc.cstnet.cn" target="_blank">桌面会议系统</a></li>'+
			'				<div class="clear"></div>'+			
			'			</ul>'+
			'		</div>'+		
			'		<div class="footer-right">'+
			'			<p>联系方式</p>'+
			'			<ul class="footer-nav">'+
			'				<li><a href="http://www.weibo.com/dcloud/" target="_blank">官方微博</a></li>'+
			'				<li><a href="aboutus.html">关于我们</a></li>'+
			'				<li><a href="links.html">友情链接</a></li>'+
			'				<div class="clear"></div>'+			
			'			</ul>'+
			'		</div>'+
			'		<div class="clear"></div>'+
			'	</div>'+
			'</div>'
	); 
}

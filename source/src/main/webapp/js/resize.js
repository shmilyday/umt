function resizeWindow(){
	var bodyheight = $('.content_auto').height();
	var menuheight=$('.nav').height();
	if (menuheight!=bodyheight){
		var realheight = bodyheight>menuheight?bodyheight:menuheight;
		$('.content_auto').height(realheight);
		$('.nav').height(realheight);
		$('.content').height(realheight);
	}
}
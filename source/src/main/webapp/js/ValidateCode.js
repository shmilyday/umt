function ValidateImage(imageid,baseurl){
	var realurl;
	if (baseurl!=null){
		realurl = baseurl+"/validcode.jpg";
	}else{
		realurl = "/validcode.jpg";
	}
	var obj = {
		target:document.getElementById(imageid),
		baseurl:realurl,
		changeImage:function(){
		       this.target.src=this.baseurl+"?timestamp="+(new Date()).getTime();
		       $(this.target).addClass("validateImg");
		       
		}
	};
	if(obj.target!=null){
		obj.changeImage();
	}
	return obj;
}
function ValidateImage(imageid){
	var obj = {
		target:document.getElementById(imageid),
		baseurl:"servlet/validcode",
		changeImage:function(){
		       this.target.src=this.baseurl+"?timestamp="+(new Date()).getTime();
		}
	};
	if(obj.target!=null){
		obj.changeImage();
	}
	return obj;
}
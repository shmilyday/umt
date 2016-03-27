$(document).ready(function(){
	if(!isIE6Or7()){
		$('.preparePopover').popover().on('click',function(e){
			e.stopPropagation();
		}).on('mouseenter',function(){
			$(this).popover('show');
		});
		$('body').on('click',function(e){
			if($(e.target).closest('div.popover').size()==0){
				$('.preparePopover').popover('hide');
			}
		});
	}else{
		$('.preparePopover').each(function(i,n){
			$(n).attr('title',$($(n).data('content')).text());
		});
	}
	function autoFocus(selector){
		var $input=$(selector);
		var loginName=$input.val();
		if($.trim(loginName)!=''){
			$('#password').focus(); 
		}else{
			$input.focus();
		}
	}
	autoFocus('#userName');
	
});
function isIE6Or7(){
	var browser=navigator.appName ;
	var b_version=navigator.appVersion;
	var version=b_version.split(";"); 
	if(version.length<2){
		return false;
	}
	var trim_Version=version[1].replace(/[ ]/g,""); 
	if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE7.0") 
	{ 
		return true;
	} 
	else if(browser=="Microsoft Internet Explorer" && trim_Version=="MSIE6.0") 
	{ 
		return true;
	} 
	return false;
}
function request(paras) {  
    var url = location.href;  
    var paraString = url.substring(url.indexOf("?") + 1, url.length).split("&");  
    var paraObj = {} ;
    for (var i = 0;i<paraString.length; i++) {
      var j = paraString[i];
        paraObj[j.substring(0, j.indexOf("=")).toLowerCase()] = j.substring(j.indexOf("=") + 1, j.length);  
    }  
    var returnValue = paraObj[paras.toLowerCase()];  
    if (typeof (returnValue) == "undefined") {  
        return "";  
    } else {  
        return returnValue;  
    }  
}
function toRed(str){
	return "<font color='#cc0000'>"+str+'</font>';
}
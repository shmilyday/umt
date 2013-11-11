/**
 * 验证用户是否验证太弱了
 * @param value 密码
 * @param notEquals 不应该与此相同
 * */
function isPassWeak(value,notEquals){
	return passWordAllSmall(value)||passWordAllNum(value)||passWordAllBig(value)||notEquals==value||regixHasSpace.test(value);
}
/**
 * 验证密码是否都是小写字母组成
 * @param value
 * @return boolean
 * */
function passWordAllSmall(value){
	var regixAllSmall=/^[a-z]+$/;
	return regixAllSmall.test(value);
}
/**
 * 验证密码是否都是数字组成
 * @param value
 * @return boolean
 * */
function passWordAllNum(value){
	var regixAllNum=/^[0-9]+$/;
	return regixAllNum.test(value);
}

/**
 * 验证密码是否都是大写字母组成
 * @param value
 * @return boolean
 * */
function passWordAllBig(value){
	var regixAllBig=/^[A-Z]+$/;
	return regixAllBig.test(value);
}
/**
 * 验证密码是否有空格
 * */
function passWordHasSpace(value){
	var regixAllBig=/ /;
	return regixAllBig.test(value);
}
jQuery.validator.addMethod("passwordNotEquals",  //addMethod第1个参数:方法名称
function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
			if(value==''||params.notEquals()==''){
				return true;
			}
			return value!=params.notEquals();
 },'password  cant Not Equals');

jQuery.validator.addMethod("passwordAllSmall",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return !passWordAllSmall(value)&&params;
 },'Password is All Smaill');

jQuery.validator.addMethod("passwordAllNum",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return !passWordAllNum(value)&&params;
 },'Password is All Num');

jQuery.validator.addMethod("passwordAllBig",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return !passWordAllBig(value)&&params;
 },'Password is All Big');

jQuery.validator.addMethod("passwordHasSpace",  //addMethod第1个参数:方法名称
		function(value, element, params) {     //addMethod第2个参数:验证方法，参数（被验证元素的值，被验证元素，参数）
		if(params==null){
			return true;
		}
	    return !passWordHasSpace(value)&&params;
 },'Password has Space');
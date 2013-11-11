function checkform(form){
	var childinputs=form.elements;
	if (childinputs!=null){
		for (var i=0;i<childinputs.length;i++){
			if (!form_checkInput(childinputs[i])){
				return false;
			}
		}
	}
	return true;
}

function form_cancel_event(event){
	event.cancelBubble=true;
}
function form_getAttribute(input, key){
	var nameditem = input.attributes.getNamedItem(key);
	if (nameditem!=null)
		return nameditem.value;
	else
		return null;
}

function form_checkInput(input){
	var datatype=form_getAttribute(input, "datatype");
	
	var required=form_getAttribute(input, "required");
	var value=input.value;
	
	if (required=='true' && (value=="" || value==null)){
			form_alert(input);
			return false;
	}
	
	if (datatype!=null){
		if (value==null)
			return true;
		value=$.trim(value);
		var canpass=false;
		switch (datatype){
			case 'email':
				canpass=form_check_email(value);
				break;
			case 'integer':
				canpass=form_check_integer(value);
				break;
			case 'float':
				canpass=form_check_integer(value);
				break;
			default:
				alert("无法识别的数据类型");
				return false;
				break;
		}
		if (canpass==false){
			form_alert(input);
			return false;
		}
	}
	return true;
}

function form_check_email(value){
	var pattern=/^[_.0-9a-z-a-z-]+@([0-9a-z][0-9a-z-]+.)+[a-z]{2,4}$/;  
	return pattern.exec(value.trim())!=null;
}

function form_check_integer(value){
	return value.match("^[-]{0,1}[0-9]+$")!=null;
}

function form_check_float(vlaue){
	return value.match("^[1-9]+[0-9]*(.[0-9]*)$")!=null;
}

function form_alert(input){
	var msg=form_getAttribute(input, "message");
	alert(msg);
}

function Cookie() {
	this.setCookie = function (name, value, option) {
     var str=name+"="+escape(value);  
     if(option){
            if(option.expireDays){
                   var date=new Date();
                   var ms=option.expireDays*24*3600*1000;
                   date.setTime(date.getTime()+ms);
                   str+="; expires="+date.toGMTString();
            } 
            if(option.path)str+="; path="+option.path;
            if(option.domain)str+="; domain"+option.domain;
            if(option.secure)str+="; true";
     }
     document.cookie=str;
	};
	this.getCookieVal = function (offset) {
		var endstr = document.cookie.indexOf(";", offset);
		if (endstr == -1) {
			endstr = document.cookie.length;
		}
		return unescape(document.cookie.substring(offset, endstr));
	};
	this.getCookie = function (name) {
		var arg = name + "=";
		var alen = arg.length;
		var clen = document.cookie.length;
		var i = 0;
		while (i < clen) {
			var j = i + alen;
			if (document.cookie.substring(i, j) == arg) {
				return this.getCookieVal(j);
			}
			i = document.cookie.indexOf(" ", i) + 1;
			if (i === 0) {
				break;
			}
		}
		return null;
	};
	
	this.deleteCookie=function(name, path){
		if (path!=null)
			this.setCookie(name,"",{expireDays:-1, path:path});
		else
			this.setCookie(name,"",{expireDays:-1});
	};
}
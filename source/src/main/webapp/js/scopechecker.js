function ScopeChecker(scopeString){
	this.scopes = scopeString.split(";");
	this.isValidScope=function(username){
		var atIndex = username.indexOf('@');
		if (atIndex!=-1){
			var domain = username.substr(atIndex+1);
			for(var i=0;i<this.scopes.length;i++){
				if(domain==this.scopes[i])
					return true;
			}
			return false;
		}else{
			return true;
		}
	};
	return this;
}
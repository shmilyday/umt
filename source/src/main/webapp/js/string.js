String.prototype.equalsIgnoreCase=myEqualsIgnoreCase;
String.prototype.equals=myEquals;

function myEquals(arg){
        return (this.toString()==arg.toString());
}
function hideEmail(email){
	return email.substr(0,1)+"******"+email.substr(email.indexOf("@")-1);
}

function myEqualsIgnoreCase(arg)
{               
        return (new String(this.toLowerCase())==(new String(arg)).toLowerCase());
}

/*String.prototype.trim = function()
{
    return this.replace(/(^[\\s]*)|([\\s]*$)/g, "");
}*/

String.prototype.lTrim = function()
{
    return this.replace(/(^[\\s]*)/g, "");
}
String.prototype.rTrim = function()
{
    return this.replace(/([\\s]*$)/g, "");
}

String.prototype.endWith=function(s){
      if(this.length<s.length)  
        return   false;  
      if(this==s)  
        return   true;  
      if(this.substring(this.length-s.length)==s)  
          return   true;  
      return   false;  
}
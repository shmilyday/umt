/**
 * Example：
 * 	$('#table2').zebraTable({
 * 		topClass:'top',
 * 		evenClass:'even',
 * 		oddClass:'odd'
 * 	});
 */
(function($) {
$.fn.zebraTable = function(options) {
	options = $.extend({}, $.zebraTabler.defaults, options);
	
	this.each(function(){
		new $.zebraTabler(this,options);
	});
	return this;
}

$.zebraTabler = function(tabler, options) {
	var rows = tabler.rows;
	for (var i=0;i<rows.length;i++){
		if (i==0){
			rows[0].className=options.topClass;
		}else{
			if (i%2==0){
				rows[i].className=options.evenClass;
			}else{
				if (i!=1){
					rows[i].className=options.oddClass;
				}else{
					rows[i].className=options.firstClass+" "+options.oddClass;
				}
			}
		}
	}
	return {}
};

$.zebraTabler.defaults = {
	topClass: 'top',
	evenClass: 'White', //even line's css style
	oddClass: 'Gray', // odd line's css style
	firstClass:''
}

})(jQuery);
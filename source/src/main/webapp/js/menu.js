/**
 * Example：
 * 	$('#table2').zebraTable({
 * 		topClass:'top',
 * 		evenClass:'even',
 * 		oddClass:'odd'
 * 	});
 */
(function($) {
$.fn.menu = function(options){return this.each(function(){
	options = $.extend({
		menuid:''
	}, options);
	
	var me=$(this);
	var menuobj=$(options.menuid);
	menuobj.css('display','none');
	menuobj.css('position', 'absolute');
	
	me.mouseover(function(){
		showMenu(me, menuobj);
	});
	me.mouseout(function(){
		hideMenu(menuobj);
	});
	menuobj.mouseover(function(){
		showMenu(me, menuobj);
	});
	menuobj.mouseout(function(){
		hideMenu(menuobj);
	});
	function showMenu(me, menuobj){
		menuobj.css('display','block');
		var position=me.position();
		var top=position.top+me.height()-2;
		var left=position.left+me.width()-menuobj.width();
		menuobj.css('top', top+'px');
		menuobj.css('left', left+'px');	
	};
	function hideMenu(menuobj){
		menuobj.css('display', 'none');
	}
})}


})(jQuery);
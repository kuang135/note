$.fn.extend({
	alertWhileClick : function() {
		$(this).click(function() { 
			alert($(this).val()); 
		});
	}
}); 

$(function(){
	$("#input1").alertWhileClick();
});
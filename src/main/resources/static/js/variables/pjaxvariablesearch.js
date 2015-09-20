/*$("#searchVariables").on('input', function() {
	updateSearch();
});*/

/*$("#searchVariables").on('click', function() {
	updateSearch();
});*/

/*function updateSearch() {
	var form = $('#searchVariables');
	$.pjax({
		container : "#searchResults",
		data : form.serialize()
	});
}*/
function variableSearch(){
	$( ".data-pjax" ).submit();
	getLastFocused();
	initDatepicker();
}
$(document).on('submit', 'form[data-pjax]', function(event) {
  $.pjax.submit(event, '#pjax-container');
});
$(document).ready(function() {
	var lastFocused = $.cookie("lastFocused");
	if(!lastFocused){
		lastFocused="query";
	}
	if(lastFocused.search("surveyPeriod") >= 0){
		initDatepicker();
	}
	$("#" + lastFocused).focus();
	getLastFocused();
});
//set the mode for datepicker
function getMode() {
	return 'search';
}
//get id of last focused element and save it as cookie
function getLastFocused() {
	$("input").focus(function() {
		$.cookie("lastFocused",replaceId($(this).attr('id')));
	});
}
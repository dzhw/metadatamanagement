$("#searchVariables").on('input', function() {
	updateSearch();
});

$("#searchVariables").on('click', function() {
	updateSearch();
});

function updateSearch() {

	// sets the value in the html input field
	$("#query").attr("value", $("#query").val());

	// init the date picker
	initDatepicker();

	// serialize the form
	var form = $('#searchVariables');
	$.pjax({
		container : "#searchResults",
		data : form.serialize()
	});

	// $.pjax.submit(event, '#searchVariables');
}

//this method set the correct value to the input field and the value field on history back
window.addEventListener('popstate', function(e) {
	$("#query").val(getUrlParameter('query'));
	$("#query").attr("value", $("#query").val());
});

// function variableSearch(){
// $( ".data-pjax" ).submit();
// getLastFocused();
// initDatepicker();
// }
// $(document).on('submit', 'form[data-pjax]', function(event) {
// $.pjax.submit(event, '#pjax-container');
// });
$(document).ready(function() {
	var lastFocused = $.cookie("lastFocused");
	if (!lastFocused) {
		lastFocused = "query";
	}
	if (lastFocused.search("surveyPeriod") >= 0) {
		initDatepicker();
	}
	$("#" + lastFocused).focus();
	getLastFocused();
});
// set the mode for datepicker
function getMode() {
	return 'search';
}
// get id of last focused element and save it as cookie
function getLastFocused() {
	$("input").focus(function() {
		$.cookie("lastFocused", replaceId($(this).attr('id')));
	});
}

var getUrlParameter = function getUrlParameter(sParam) {
	var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL
			.split('&'), sParameterName, i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? true : sParameterName[1];
		}
	}
};
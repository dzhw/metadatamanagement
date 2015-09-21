var VariableSearchForm = {};

// this function starts elasticsearch
VariableSearchForm.updateSearch = function () {
	"use strict";
	
	// serialize the form
	event.preventDefault();
	$.pjax.submit(event, '#pjax-container');
};

$(document).ready(function() {
	"use strict";
	//if there is any input, update the search
	$("#searchVariables").on('input', ".pjax-input", VariableSearchForm.updateSearch);

	// if there is any click, update the search
	$("#searchVariables").on('click', ".pjax-filter", VariableSearchForm.updateSearch);

	//if there is any change on the datepicker
	$("#searchVariables").on('change', ".datepicker", VariableSearchForm.updateSearch);
	
	// this method set the correct value to the input field and the value field on
	// history back
	window.addEventListener('popstate', function(e) {
		$("#query").val(getUrlParameter('query'));
	});
	
	$(document).on('pjax:success', function() {
		// init the date picker
		initDatepicker();
	});
});

// set the mode for datepicker
function getMode() {
	return 'search';
}
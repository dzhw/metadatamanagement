var VariableSearchForm = {};

// this function starts elasticsearch
VariableSearchForm.search = function(event) {
	"use strict";
	
	// prevent the default submit action
	event.preventDefault();
	
	// clone the event for the pjax api
	var clonedEvent = {};
	clonedEvent.currentTarget = event.currentTarget.form;
	clonedEvent.preventDefault = function() {};
	
	VariableSearchForm.throttledSubmit(clonedEvent);
};

VariableSearchForm.throttledSubmit = _.throttle(function(event) {
	$.pjax.submit(event, '#pjax-container');
},500);

$(document).ready(function() {
	"use strict";
	//if there is any input, update the search
	$("#searchVariables").on('input', ".pjax-input", VariableSearchForm.search);

	// if there is any click, update the search
	$("#searchVariables").on('click', ".pjax-filter", VariableSearchForm.search);

	//if there is any change on the datepicker
	$("#searchVariables").on('change', ".datepicker", VariableSearchForm.search);
	
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
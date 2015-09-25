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
	"use strict";
	$.pjax.submit(event, '#pjax-container');
},250);

VariableSearchForm.lastFocusedElement = '';

$(document).ready(function() {
	"use strict";
	//if there is any input, update the search
	$("#searchVariables").on('input', ".pjax-input", VariableSearchForm.search);

	// if there is any click, update the search
	$("#searchVariables").on('click', ".pjax-filter", VariableSearchForm.search);
	
	// if there is any change on the datepickers
	// for some reason pjax fires another change event when enter is clicked on datepicker
	// therefore we need to debounce and ignore the second call
	$("#searchVariables").on('change', ".datepicker", _.debounce(VariableSearchForm.search,250,true));
	
	// remember the id of the element which received focus in order to
	// restore it after partial page request
	$("#searchVariables").on('focus', '.datepicker', function() {
		VariableSearchForm.lastFocusedElement = this.id;
	});
	
	$("#searchVariables").on('focus', '.pjax-filter', function() {
		VariableSearchForm.lastFocusedElement = this.id;
	});
	
	$("#searchVariables").on('focus', '.pjax-input', function() {
		VariableSearchForm.lastFocusedElement = this.id;
	});
	
	// this method set the correct value to the input field and the value field on
	// history back
	window.addEventListener('popstate', function(e) {
		$("#query").val(getUrlParameterValue('query'));
	});
	
	$(document).on('pjax:end', function() {
		// re-init the datepickers after partial page refresh
		Datepicker.initAll();
		// reset the focus but not for query field since this keeps the focus anyway
		if (VariableSearchForm.lastFocusedElement !== 'query') {			
			$('#' + Datepicker.escapeId(VariableSearchForm.lastFocusedElement)).focus();
		}
	});
	
	// first setup of the datepickers
	Datepicker.initAll();
});
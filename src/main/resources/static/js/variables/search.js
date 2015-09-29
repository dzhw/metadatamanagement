var VariableSearchForm = {};

// search as the user types but not more then every 500 ms
VariableSearchForm.search = _.throttle(function(event) {
	"use strict";
	var $form = $('#searchVariables');
	
	var formData = $form.serialize();
	var searchUrl = $form.attr('action');

	// return only a div container of the search results
	// and replace the searchResults div contrainer
	$.get(searchUrl, formData, function(response) {
		$("#searchResults").replaceWith(response);

		var newUrl = searchUrl + '?' + formData;
		// change the browsers url
		history.pushState({}, '', newUrl);

		// re-init the datepickers after reload
		Datepicker.initAll();
		// reset the focus but not for query field since this keeps the focus anyway
		if (VariableSearchForm.lastFocusedElement !== 'query') {			
			$('#' + Datepicker.escapeId(VariableSearchForm.lastFocusedElement)).focus();
		}
	});
}, 500);

VariableSearchForm.lastFocusedElement = '';

$(document).ready(function() {
	"use strict";
	//if there is any input, update the search
	$("#searchVariables").on('input', ".pjax-input", VariableSearchForm.search);

	// if there is any click, update the search
	$("#searchVariables").on('click', ".pjax-filter", VariableSearchForm.search);
	
	// if there is any change on the datepickers
	// for some reason the datepicker fires another change event when enter is clicked on the datepicker
	// TODO remove datepicker
	$("#searchVariables").on('change', ".datepicker", VariableSearchForm.search);
	
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
	
	window.addEventListener('popstate', function(e) {
		if (event.state) {
			// reload if back button has been clicked
			location.reload();
			// re-init the datepickers after reload
			Datepicker.initAll();
			// reset the focus but not for query field since this keeps the focus anyway
			if (VariableSearchForm.lastFocusedElement !== 'query') {			
				$('#' + Datepicker.escapeId(VariableSearchForm.lastFocusedElement)).focus();
			}
		}
	});
		
	// first setup of the datepickers
	Datepicker.initAll();
});
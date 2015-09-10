VariableSearchForm = {};

// search as the user types but not more then every 500 ms
VariableSearchForm.search = _.throttle(function(form) {
	"use strict";
	var formData = $(form).serialize();
	var searchUrl = $(form).attr('action');
	
	// return only a div container of the search results
	// and replace the searchResults div contrainer
	$.get(searchUrl, formData, function(response) {
		$("#searchResults").replaceWith(response);

		var newUrl = searchUrl + '?' + formData;
		// change the browsers url
		history.pushState({},'' , newUrl);
		initDatePicker();
	});
},500);

$(document).ready(function() {
	window.onpopstate = function(event) {
		"use strict";
		if (event.state) {
			// reload if back button has been clicked
			location.reload();
		}
	};	
	initDatePicker();
});
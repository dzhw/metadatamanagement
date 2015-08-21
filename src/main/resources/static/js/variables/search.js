VariableSearchForm = {};

VariableSearchForm.search = function(form) {
	"use strict";
	var formData = $(form).serialize();
	var searchUrl = $(form).attr('action');
	// return only a div container of the search results
	// and replace the searchResults div contrainer
	$.get(searchUrl, formData, function(response) {
		$("#searchResults").replaceWith(response);

		var newUrl = searchUrl + '?' + formData;
		history.pushState({}, '', newUrl);
	});
};

window.onpopstate = function(event) {
	"use strict";
	// browser is not loading page automatically for back/forward
	if (event.state) {
		location.reload();
	}
};
VariableSearchForm = {};

// search if the user stopped typing for 200 ms
VariableSearchForm.search = _.debounce(function(form) {
	"use strict";
	var formData = $(form).serialize();
	var searchUrl = $(form).attr('action');
	//fix for missing query string in document.documentElement.innerHTML
	$('#query').attr('value',$('#query').val());
	// return only a div container of the search results
	// and replace the searchResults div contrainer
	$.get(searchUrl, formData, function(response) {
		$("#searchResults").replaceWith(response);

		var newUrl = searchUrl + '?' + formData;
		// change the browsers url and cache the current search results
		history.pushState({html: document.documentElement.innerHTML},'' , newUrl);
	});
},200);

window.onpopstate = function(event) {
	"use strict";
	if (event.state) {
		// if there is a cached search result display it
		document.documentElement.innerHTML = event.state.html;
	} else {
		// if not reload the page from the server
		location.reload();
	}
};
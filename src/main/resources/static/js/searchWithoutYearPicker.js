var SearchForm = {};

// search as the user types but not more then every 500 ms
SearchForm.search = _.throttle(function() {
	"use strict";
	var $form = $('#searchForm');
	
	var formData = $form.serialize();
	var searchUrl = $form.attr('action');
	// return only a div container of the search results
	// and replace the searchResults div contrainer
	$.get(searchUrl, formData, function(response) {
		$("#searchResults").replaceWith(response);

		var newUrl = searchUrl + '?' + formData;
		// change the browsers url
		history.pushState({}, '', newUrl);
	});
}, 500);

SearchForm.lastFocusedElement = '';

$(document).ready(function() {
	"use strict";
	//if there is any input, update the search
	$("#searchForm").on('input', ".pjax-input", SearchForm.search);

	// if there is any click, update the search
	$("#searchForm").on('click', ".pjax-filter", SearchForm.search);
	
	$("#searchForm").on('focus', '.pjax-filter', function() {
		SearchForm.lastFocusedElement = this.id;
	});
	
	$("#searchForm").on('focus', '.pjax-input', function() {
		SearchForm.lastFocusedElement = this.id;
	});
	window.addEventListener('popstate', function(e) {
	if (event.state) {
		// reload if back button has been clicked
		location.reload();
	}
	});
	
	// setup autocomplete for search box
	$( "#query" ).autocomplete({
		source: _.throttle(function(request,render) {
			var suggestUrl = $( "#query" ).data("suggest-url");
			$.get(suggestUrl, "term=" + request.term, function(response) {
				render(response.suggestions);
			});
		},250),
		minLength: 2,
		delay: 0, 
		select: function( event, ui ) {
			SearchForm.search();
		},
	});
});
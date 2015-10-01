var SearchForm = {};

// search as the user types but not more then every 500 ms
SearchForm.search = _.throttle(function(event) {
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

		// re-init the datepickers after reload
		Datepicker.initAll();
		// reset the focus but not for query field since this keeps the focus anyway
		if (SearchForm.lastFocusedElement !== 'query') {			
			$('#' + Datepicker.escapeId(SearchForm.lastFocusedElement)).focus();
		}
	});
}, 500);

SearchForm.lastFocusedElement = '';

$(document).ready(function() {
	"use strict";
	//if there is any input, update the search
	$("#searchForm").on('input', ".pjax-input", SearchForm.search);

	// if there is any click, update the search
	$("#searchForm").on('click', ".pjax-filter", SearchForm.search);
	
	// if there is any change on the datepickers
	// for some reason the datepicker fires another change event when enter is clicked on the datepicker
	// TODO remove datepicker
	$("#searchForm").on('change', ".datepicker", SearchForm.search);
	
	// remember the id of the element which received focus in order to
	// restore it after partial page request
	$("#searchForm").on('focus', '.datepicker', function() {
		SearchForm.lastFocusedElement = this.id;
	});
	
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
			// re-init the datepickers after reload
			Datepicker.initAll();
			// reset the focus but not for query field since this keeps the focus anyway
			if (SearchForm.lastFocusedElement !== 'query') {			
				$('#' + Datepicker.escapeId(SearchForm.lastFocusedElement)).focus();
			}
		}
	});
		
	// first setup of the datepickers
	Datepicker.initAll();
	
	// setup autocomplete for search box
	$( "#query" ).autocomplete({
		source: function(request,render) {
			var suggestUrl = $( "#query" ).data("suggest-url");
			$.get(suggestUrl, "term=" + request.term, function(response) {
				render(response.suggestions);
			});
		},
		minLength: 2,
		select: function( event, ui ) {
			SearchForm.search(event);
		},
	});
});
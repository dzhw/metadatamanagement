var SearchForm = {};
var FillInputFields = {};

//set values for date input fields
FillInputFields.fill = function(){
	
	var startDateId = "#" + YearPicker.escapeId("surveyPeriod.startDate"),
	startDateValue = YearPicker.trimm(document.querySelector(startDateId+"_alt").value),
	endDateId = "#" + YearPicker.escapeId("surveyPeriod.endDate"),
	endDateValue = YearPicker.trimm(document.querySelector(endDateId+"_alt").value);
	
	document.querySelector(startDateId).value=startDateValue;
	document.querySelector(endDateId).value=endDateValue;
};

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

		// reset the focus but not for query field since this keeps the focus anyway
		if (SearchForm.lastFocusedElement !== 'query') {			
			$('#' + YearPicker.escapeId(SearchForm.lastFocusedElement)).focus();
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
	
	$("#searchForm").on('focus', '.pjax-filter', function() {
		SearchForm.lastFocusedElement = this.id;
	});
	
	$("#searchForm").on('focus', '.pjax-input', function() {
		SearchForm.lastFocusedElement = this.id;
	});
	FillInputFields.fill();
		window.addEventListener('popstate', function(e) {
		if (event.state) {
			// reload if back button has been clicked
			location.reload();
			// reset the focus but not for query field since this keeps the focus anyway
			if (SearchForm.lastFocusedElement !== 'query') {			
				$('#' + YearPicker.escapeId(SearchForm.lastFocusedElement)).focus();
			}
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
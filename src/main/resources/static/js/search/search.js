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

$(document).ready(function() {
	"use strict";
	searchFormChanges();
	
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
	autoCompleteSearchBox();
});
$(document).ready(function() {
	"use strict";
	searchFormChanges();

	window.addEventListener('popstate', function(e) {
	if (event.state) {
		// reload if back button has been clicked
		location.reload();
	}
	});
	
	// setup autocomplete for search box
	autoCompleteSearchBox();
});

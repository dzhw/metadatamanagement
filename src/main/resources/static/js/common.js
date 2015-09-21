(function(GlobalSetup) {
	"use strict";
	// jquery setup
	$.ajaxSetup({
		beforeSend : function(xhr) {
			// add ajax header to all requests
			xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");
		}
	});
})();

/* fix default focus of bootstrap modals */
$(document).ready(function() {
	"use strict";
	$("#aboutarea").on('shown.bs.modal', function() {
		$("#close-button").focus();
	});
});

//This method is from the URL: http://www.jquerybyexample.net/2012/06/get-url-parameters-using-jquery.html
function getUrlParameter(sParam) {
	"use strict";
	var sPageURL = decodeURIComponent(window.location.search.substring(1)), sURLVariables = sPageURL
			.split('&'), sParameterName, i;

	for (i = 0; i < sURLVariables.length; i++) {
		sParameterName = sURLVariables[i].split('=');

		if (sParameterName[0] === sParam) {
			return sParameterName[1] === undefined ? true : sParameterName[1];
		}
	}
}
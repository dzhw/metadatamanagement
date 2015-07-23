"use strict";

(function(GlobalSetup) {
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
	$("#aboutarea").on('shown.bs.modal', function() {
		$("#close-button").focus();
	});
});
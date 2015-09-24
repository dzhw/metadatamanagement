var Datepicker = {};

// initiate all datepickers
Datepicker.initAll = function() {
	"use strict";
	var locale = $('html').attr('lang');
	$.datepicker.setDefaults($.datepicker.regional[locale]);
	$('.datepicker').each(function() {
		$(this).datepicker({
			altField : "#" + Datepicker.escapeId($(this).attr('id')) + "_alt",
			altFormat : "yy-mm-dd",
			gotoCurrent : true,
			onSelect : function(d,i) {
				//allways trigger on change event cause
				//this function is also called when pressing enter
				$(this).change();					
		    },
			changeMonth : true,
			changeYear : true,
			yearRange : '1970:2040',
			firstDay : 1,
			autoclose : true,
			showButtonPanel: true
		});
	});
	
	// we need to clear the alt field when the user cleared the visible input
	$(".datepicker").change(function(){
		if (!$(this).val()) {
			$("#" + Datepicker.escapeId($(this).attr('id')) + "_alt").val('');
		}
	});
	
};

// escape the dot in id attribute
Datepicker.escapeId = function(id) {
	"use strict";
	return id.replace(/\./g, "\\.");
};

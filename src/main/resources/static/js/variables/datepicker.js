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
			onSelect : function(d,i){
		          if(d !== i.lastVal){
		              //trigger on change event
		        	  $(this).change();
		          }
		    },
			showOn : "focus",
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

// replace the dot in id attribute
Datepicker.escapeId = function(id) {
	"use strict";
	return id.replace(/\./g, "\\.");
};

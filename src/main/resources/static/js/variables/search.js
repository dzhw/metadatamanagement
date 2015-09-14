// var $focusedDatepicker=null;

VariableSearchForm = {};

// search as the user types but not more then every 500 ms
VariableSearchForm.search = _.throttle(function(form) {
	"use strict";
	var formData = $(form).serialize();
	var searchUrl = $(form).attr('action');
	
	// return only a div container of the search results
	// and replace the searchResults div contrainer
	$.get(searchUrl, formData, function(response) {
		$("#searchResults").replaceWith(response);

		var newUrl = searchUrl + '?' + formData;
		// change the browsers url
		history.pushState({},'' , newUrl);
		initDatePicker('search');
		
		//set focus on datepicker when used
		/*if($focusedDatepicker){
			console.log($focusedDatepicker.attr('id'));
			$focusedDatepicker.css("background-color", "#FFFFCC");
		}
		getLastFocused();
		console.log("-> "+$focusedDatepicker.parent().attr('class'));
		$focusedDatepicker.parent().focusin();*/
	});
},500);

$(document).ready(function() {
	window.onpopstate = function(event) {
		"use strict";
		if (event.state) {
			// reload if back button has been clicked
			location.reload();
		}
	};
	initDatePicker('search');
	//getLastFocused();
});

/*function getLastFocused(){
	$("input").focusin(function(){
	    var correntClass = $(this).attr("class");
	    if(correntClass === 'form-control datepicker hasDatepicker'){
	    	$focusedDatepicker=$(this);
	    	$focusedDatepicker.css("background-color", "#FFFFCC");
	    }else{
	    	$focusedDatepicker=null;
	    }
	});
}*/

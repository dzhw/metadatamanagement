VariableSearchForm = {};

VariableSearchForm.search = function(form){
	var formData = $(form).serialize();
	var searchUrl = $(form).attr('action');
	
	// return only a div container of the search results 
	//and replace the searchResults div contrainer
	$.get(searchUrl, formData, function(response) {
	     $("#searchResults").replaceWith(response);
	});
};
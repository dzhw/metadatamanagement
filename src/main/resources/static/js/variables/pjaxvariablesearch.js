$("#query").on('input', function() {
	updateSearch();
});

$("#collapseSurveyTitlePanel").on('click', function() {
	updateSearch();
});

$("#collapseScaleLevelPanel").on('click', function() {
	updateSearch();
});

function updateSearch() {
	var form = $('#searchVariables');
	$.pjax({
		container : "#searchResults",
		data : form.serialize()
	});
}

//set the mode for datepicker
function setmode() {
	return 'search';
}
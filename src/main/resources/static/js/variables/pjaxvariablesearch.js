$("#searchVariables").on('input', function() {
	updateSearch();
});

$("#searchVariables").on('click', function() {
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
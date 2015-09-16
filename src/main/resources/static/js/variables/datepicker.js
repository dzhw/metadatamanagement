var mode = '';
function triggerDatepicker(id) {
	event.preventDefault();
	locale = $('html').attr('lang');
	mode = setmode();
	$.datepicker.setDefaults($.datepicker.regional[locale]);
	convertedId = replaceId(id);
	$element = $("#" + convertedId);
	$element.datepicker({
		altField : "#" + convertedId + "_alt",
		altFormat : "yy-mm-dd",
		gotoCurrent : true,
		onSelect : function() {
			triggerOnValidDate(mode, $element);
		},
		autoclose : true,
		changeMonth : true,
		changeYear : true,
		yearRange : '1970:2040',
		firstDay : 1
	}).datepicker('show');
}
function triggerOnKeyup(id) {
	convertedId = replaceId(id);
	$element = $("#" + convertedId);
	$element.datepicker("hide");
	if (event.keyCode == 46) {
		$element.val('');
	}
	event.preventDefault();
	$altFiledInput = $("#" + convertedId + "_alt");
	var date = '';
	try {
		switch (locale) {
		case 'de':
			date = $.datepicker.parseDate('dd.mm.yy', $element.val());
			break;
		case 'en':
			date = $.datepicker.parseDate('mm/dd/yy', $element.val());
			break;
		default:
			break;
		}
		var day = ("0" + (date.getDate())).slice(-2);
		var year = date.getFullYear();
		// JavaScript months are 0-11
		var month = ("0" + (date.getMonth() + 1)).slice(-2);
		$altFiledInput.val(year + '-' + month + '-' + day);
		triggerOnValidDate(mode, $element);
	} catch (e) {
		triggerOnInvalidDate(mode, $element);
	}
}
function replaceId(oldId) {
	return oldId.replace(/\./g, "\\.");
}

function triggerOnValidDate(mode, element) {
	if (mode === 'modify') {
		VariableModifyForm.validate(element.closest("form"));
		$element.unbind('focus');
	} else {
		VariableSearchForm.search(element.closest("form"));
	}
}
function triggerOnInvalidDate(mode, element) {
	if (mode === 'modify') {
		$altFiledInput.val('invalid date');
		VariableModifyForm.validate(element.closest("form"));
	} else {
		if (element.val() === '') {
			$altFiledInput.val('');
			VariableSearchForm.search(element.closest("form"));
		}
	}
}
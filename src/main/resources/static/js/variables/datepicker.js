function triggerDatepicker(id){
	locale = $('html').attr('lang');
	mode = setmode();
	$.datepicker.setDefaults($.datepicker.regional[locale]);
	convertedId=replaceId(id);
	$element=$("#" + convertedId);
	$element.datepicker({
		altField : "#" +convertedId+ "_alt",
		altFormat : "yy-mm-dd",
		gotoCurrent : true,
		onSelect : function() {
			triggerOnValidDate(mode, $element.closest("form"));
			 $element.off( "focus" );
		},
		changeMonth : true,
		changeYear : true,
		yearRange : '1970:2040',
		firstDay : 1
	}).datepicker('show').on('keyup', function() {
		$element.datepicker("hide");
		$altFiledInput = $("#" +convertedId + "_alt");
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
			triggerOnValidDate(mode,  $element.closest("form"));
		} catch (e) {
			triggerOnInvalidDate(mode, $element.val(), $element.closest("form"));
		}
	});
}

function replaceId(oldId) {
	return oldId.replace(/\./g, "\\.");
}

function triggerOnValidDate(mode, form) {
	if (mode === 'modify') {
		VariableModifyForm.validate(form);
	} else {
		VariableSearchForm.search(form);
	}
}
function triggerOnInvalidDate(mode, value, form) {
	if (mode === 'modify') {
		$altFiledInput.val('invalid date');
		VariableModifyForm.validate(form);
	} else {
		$altFiledInput.val('');
		if (value === '') {
			VariableSearchForm.search(form);
		}
	}
}
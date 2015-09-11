function initDatePicker(mode) {
	$(".datepicker").each(function() {
		locale = $('html').attr('lang');
		$.datepicker.setDefaults($.datepicker.regional[locale]);
		$(this).datepicker({
			altField : "#" + replaceId($(this).attr('id')) + "_alt",
			altFormat : "yy-mm-dd",
			gotoCurrent : true,
			onSelect : function() {
				triggerOnValidDate(mode,this.form);
			},
			changeMonth : true,
			changeYear : true,
			yearRange : '1970:2040',
			firstDay : 1
		}).on('keyup', function() {
			$(this).datepicker("hide");
			$altFiledInput = $("#" + replaceId($(this).attr('id')) + "_alt");
			var date = '';
			try {
				switch (locale) {
				case 'de':
					date = $.datepicker.parseDate('dd.mm.yy', $(this).val());
					break;
				case 'en':
					date = $.datepicker.parseDate('mm/dd/yy', $(this).val());
					break;
				default:
					break;
				}
				var day = ("0" + (date.getDate())).slice(-2);
				var year = date.getFullYear();
				// JavaScript months are 0-11
				var month = ("0" + (date.getMonth() + 1)).slice(-2);
				$altFiledInput.val(year + '-' + month + '-' + day);
				triggerOnValidDate(mode,this.form);
			} catch (e) {
				triggerOnInvalidDate(mode,$(this).val(),this.form);
			}
		});
	});
}

function replaceId(oldId) {
	return oldId.replace(/\./g, "\\.");
}

function triggerOnValidDate(mode,form){
	if(mode === 'modify'){
		$("form").trigger("submit");
		}else{
			VariableSearchForm.search(form);
		}
}
function triggerOnInvalidDate(mode,value,form){
	if (mode === 'modify') {
		$altFiledInput.val('invalid date');
	} else {
		$altFiledInput.val('');
		if (value === '') {
			VariableSearchForm.search(form);
		}
	}
}
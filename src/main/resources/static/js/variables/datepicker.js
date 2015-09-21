
$(function() {
	initDatepicker();
});

// initiate the datepicker
function initDatepicker(){	
	locale = $('html').attr('lang');
	$.datepicker.setDefaults($.datepicker.regional[locale]);
	$('.datepicker').each(function(){
	 $(this).datepicker({
		altField : "#" + replaceId($(this).attr('id')) + "_alt",
		altFormat : "yy-mm-dd",
		gotoCurrent : true,
		onSelect : function() {
		triggerOnValidDate(getMode(), $(this));
		},
		clickInput:true,
		changeMonth : true,
		changeYear : true,
		yearRange : '1970:2040',
		firstDay : 1,
		autoclose:true,
	}).on('keyup',function(){
		//$(this).datepicker('hide');
		 eventTrigger($(this),event);
	});
	});
}

// this will be triggered when user want to put date manually
function eventTrigger($element,event){
	convertedId = replaceId($element.attr('id'));
	$altFiledInput = $("#" + convertedId + "_alt");
	var date = '';
	// ignore event from keyup,keydown, keyleft and keyright
	if((event.which != 37) && (event.which != 38) && (event.which != 39) && (event.which != 40)){
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
		var day='';
		var month='';
		var tempMonthValue=(date.getMonth()+1);
		var year ='';
		
		// set date format to a compatible iso form  
		if((date.getDate() <= 9) &&(getMode==='modify')){
			day = ("0" + (date.getDate())).slice(-2);
		}else{
			day = date.getDate();
		}
		if((tempMonthValue <= 9)&&(getMode==='modify')){
			month = ("0" + tempMonthValue).slice(-2);
		}else{
			month = tempMonthValue;
		}
			correntDate = date.getFullYear() + '-' +month + '-' + day;
			$altFiledInput.val(correntDate);
			triggerOnValidDate(getMode(), $altFiledInput);
	} catch (e) {
		triggerOnInvalidDate(getMode(),$element);
	}}
}

// replace the dot in id attribute
function replaceId(oldId) {
	return oldId.replace(/\./g, "\\.");
}

function triggerOnValidDate(mode, element) {
	if (mode === 'modify') {
		VariableModifyForm.validate(element.closest("form"));
	} else {
		// number of characters in input field
		if(element.val().length === 10){
			updateSearch();
//		variableSearch();
		}else{
			$altFiledInput.val('');
		}
	}
}

function triggerOnInvalidDate(mode, element) {
	if (mode === 'modify') {
		$altFiledInput.val('invalid date');
		VariableModifyForm.validate(element.closest("form"));
	} else {
		if (element.val() === '') {
			$altFiledInput.val('');
			variableSearch();
		}
	}
}

var VariableModifyForm = {};

// validate the form if the user stopped typing for 250 ms
VariableModifyForm.validate = _.debounce(function(form) {
	"use strict";
	var data = "";
	var validateUrl = $(form).data('validate-url');

	data = $(form).serialize();

	$.post(validateUrl, data, function(response) {
		//first remove all previous validation results
		$(form).find('.has-feedback').removeClass('has-error');
		$(form).find('.has-feedback').removeClass('has-success');
		$(form).find('.help-block').empty();
		$(form).find('.form-control-feedback').removeClass('glyphicon-remove');
		$(form).find('.form-control-feedback').removeClass('glyphicon-ok');
		$(form).find('.global-error').removeClass('alert alert-danger');
		$(form).find('.global-error').empty();

		var $inputs = $(form).find('.form-control');
		var $globalErrorDivs = $(form).find('.global-error');
		
		for (var j = 0; j < $globalErrorDivs.length; j++){
			var globalError = response.errorMessageMap[$globalErrorDivs[j].id];
			if(globalError){
				  $($globalErrorDivs[j]).first().addClass('alert alert-danger');
				$($globalErrorDivs[j]).html(
						'<p>' + globalError + '</p>');
			}
		}	
		
		for (var i = 0; i < $inputs.length; i++) {
			var error = response.errorMessageMap[$inputs[i].name];
			var $formGroup = $($($inputs[i]).closest('.form-group'));
			if (error) {
				//field has error thus add messages and styles
				$formGroup.addClass('has-error');
				$formGroup.find('.form-control-feedback').addClass(
						'glyphicon-remove');
				$formGroup.find('.help-block').html(
						'<p>' + error.sort().join('<br>') + '</p>');
			} else {
				//field is valid
				$formGroup.addClass('has-success');
				$formGroup.find('.form-control-feedback').addClass(
						'glyphicon-ok');
			}
		}	
	}, 'json');
},250);

$(document).ready(function() {
	"use strict";
	// scroll to the button which has just created dynamic form fields
	var focusElementId = $('body').data('focus-element-id');
	
	if (focusElementId) {		
		//focus the button which has just created dynamic form fields
		$('#' + focusElementId).focus();
	}
	
	//scroll to the last scroll position (0 if the page is loaded for the first time)
	$(window).scrollTop($('#windowYPosition').val());
});

// bind y scroll position to hidden input field 
$(window).scroll(function() {
	$('#windowYPosition').val($(window).scrollTop());
});


var VariableModifyForm = {};

// validate the form if the user stopped typing for 200 ms
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

		var $inputs = $(form).find('.form-control');
		for (var i = 0; i < $inputs.length; i++) {
			var error = response.errorMessageMap[$inputs[i].name];
			var $formGroup = $($inputs[i].closest('.form-group'));
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
},200);

$(document).ready(function() {
	"use strict";
	// scroll to the button which has just created dynamic form fields
	var focusElementId = $('body').data('focus-element-id');
	if (focusElementId) {		
		$('html, body').animate({
			scrollTop: $('#' + focusElementId).offset().top
		}, 1000);
		$('#' + focusElementId).focus();
	}
});

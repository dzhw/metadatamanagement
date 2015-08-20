var VariableModifyForm = {};

VariableModifyForm.initForm = function(FormSetup) {
	// attach validation to age input
	$("#age").on('change', function() {
		validate(this.form);
	});
	
	// attach validation to eyeColor input
	$("#eyeColor").on('change', function() {
		validate(this.form);
	});
	
	// attach validation to birthDay input
	$("#birthDay").on('change', function() {
		validate(this.form);
	});

	// validate form
	function validate(form, reset) {
		var data = "";
		var validateUrl = $(form).data('validate-url');
		if (!reset) {
			data = $(form).serialize();
		}

		$.post(validateUrl, data, function(response) {

			$(form).find('.has-feedback').removeClass('has-error');
			$(form).find('.has-feedback').removeClass('has-success');

			$(form).find('.help-block').empty();
			$(form).find('.form-control-feedback').removeClass(
					'glyphicon-remove');
			$(form).find('.form-control-feedback').removeClass('glyphicon-ok');

			var $rows = $(form).find('.form-group');
			for (var i = 0; i < $rows.length; i++) {
				var error = response.errorMessageMap[($rows[i].id).replace(
						"-group", "")];
				if (error) {
					$($rows[i]).addClass('has-error');
					$($rows[i]).find('.form-control-feedback').addClass(
							'glyphicon-remove');
					$($rows[i]).find('.help-block').html(
							'<p>' + error.sort().join('<br>') + '</p>');
				} else {
					$($rows[i]).addClass('has-success');
					$($rows[i]).find('.form-control-feedback').addClass(
							'glyphicon-ok');
				}
			}

		}, 'json');
	}
};

(function(FormSetup) {
	VariableModifyForm.initForm();
})();
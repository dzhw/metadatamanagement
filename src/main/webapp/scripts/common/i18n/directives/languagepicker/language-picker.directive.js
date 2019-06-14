'use strict';

angular.module('metadatamanagementApp').directive('languagePicker', function() {
  return {
    restrict: 'E',
    controller: 'LanguagePickerController',
    templateUrl: 'scripts/common/i18n/directives/languagepicker/' +
      'language-picker.html.tmpl',
    scope: {
      languages: '=',
      requireLanguage: '=?'
    }
  };
});

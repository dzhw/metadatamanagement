'use strict';

angular.module('metadatamanagementApp').directive('unitValuePicker',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/surveymanagement/directives/' +
        'unitValuePicker.html.tmpl',
      controller: 'UnitValuePickerController',
      scope: {
        unit: '='
      }
    };
  });

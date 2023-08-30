'use strict';

angular.module('metadatamanagementApp').directive('sampleTypePicker',
  function() {
    return {
      restrict: 'E',
      templateUrl: 'scripts/surveymanagement/directives/' +
        'sampleTypePicker.html.tmpl',
      controller: 'SampleTypePickerController',
      scope: {
        sampleType: '='
      }
    };
  });

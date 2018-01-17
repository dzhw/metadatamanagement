'use strict';

angular.module('metadatamanagementApp')
  .directive('fdzAutocompleteRequired', function($compile, $document) {
    return function(scope, element) {
        angular.element($document).ready(function() {
          var inputField = element.find('input');
          inputField.attr('fdz-required', '');
          element.removeAttr('fdz-autocomplete-required');
          $compile(element.contents())(scope);
        });
      };
  });

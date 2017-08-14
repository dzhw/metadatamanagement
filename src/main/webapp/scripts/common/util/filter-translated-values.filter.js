/* global _*/
'use strict';

angular.module('metadatamanagementApp').filter('filterTranslatedValues',
  function($translate) {
  return function(input, searchTerm, translatePrefix) {
    if (!searchTerm) {
      return input;
    }
    var filteredInput = [];
    translatePrefix = translatePrefix || '';
    input = input || [];
    input.forEach(function(value) {
      var translatedValue = $translate.instant(translatePrefix + value);
      if (_.includes(translatedValue.toLowerCase(),
          searchTerm.toLowerCase())) {
        filteredInput.push(value);
      }
    });
    return filteredInput;
  };
});

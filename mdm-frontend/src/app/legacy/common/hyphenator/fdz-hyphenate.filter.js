/* global Hyphenator*/
'use strict';

angular.module('metadatamanagementApp').filter('fdzHyphenate', ['LanguageService',
  function(LanguageService) {
  return function(string) {
      if (string && string !== '') {
        return Hyphenator.hyphenate(string,
          LanguageService.getCurrentInstantly());
      } else {
        return string;
      }
    };
}]);

/* global Hyphenator*/
'use strict';

angular.module('metadatamanagementApp').filter('fdzHyphenate',
  function(LanguageService) {
  return function(string) {
      return Hyphenator.hyphenate(string,
        LanguageService.getCurrentInstantly());
    };
});

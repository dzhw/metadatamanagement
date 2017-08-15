/* global _*/
'use strict';

angular.module('metadatamanagementApp').filter('displayI18nString',
  function(LanguageService) {
  return function(i18nString) {
    var toBeDisplayed = '';
    var currentLanguage = LanguageService.getCurrentInstantly();
      if (_.isObject(i18nString) &&
      !_.isArray(i18nString)) {
        if (i18nString[currentLanguage]) {
          toBeDisplayed = i18nString[currentLanguage];
        } else {
          var secondLanguage = currentLanguage === 'de' ? 'en' : 'de';
          toBeDisplayed = i18nString[secondLanguage];
        }
      }
      return toBeDisplayed;
  };
});

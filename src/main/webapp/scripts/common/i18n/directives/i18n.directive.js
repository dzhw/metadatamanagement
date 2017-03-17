/* global _*/
'use strict';

angular.module('metadatamanagementApp').directive('displayI18nString',
    function(LanguageService) {
        var link = function(scope, element) {
            var toBeDisplayed;
            var currentLanguage = LanguageService.getCurrentInstantly();
            if (_.isObject(scope.displayI18nString) &&
                !_.isArray(scope.displayI18nString)) {
              if (scope.displayI18nString[currentLanguage]) {
                toBeDisplayed = scope.displayI18nString[currentLanguage];
                element.attr('lang', currentLanguage);
              } else {
                var secondLanguage = currentLanguage === 'de' ? 'en' : 'de';
                toBeDisplayed = scope.displayI18nString[secondLanguage];
                element.attr('lang', secondLanguage);
              }
            }
            if (element[0].tagName === 'IMG') {
              element.attr('alt', toBeDisplayed);
              element.attr('title', toBeDisplayed);
            } else {
              element.empty().append(toBeDisplayed + '&nbsp;');
            }
          };
        return {
          restrict: 'A',
          link: link,
          scope: {
              displayI18nString: '='
            }
        };
      });

/* global _*/
'use strict';

angular.module('metadatamanagementApp').directive('displayI18nString',
    function(LanguageService, $filter) {
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
            if (!toBeDisplayed) {
              toBeDisplayed = '';
            }
            if (element[0].tagName === 'IMG') {
              element.attr('alt', toBeDisplayed);
            } else {
              //if the limitTo Filter is set, use it!
              if (scope.limitTo) {
                var originalLength  = toBeDisplayed.length;
                toBeDisplayed = $filter('limitTo')
                  (toBeDisplayed, scope.limitTo, 0);
                if (originalLength > scope.limitTo) {
                  toBeDisplayed += '...';
                }
              }
              element.empty().append(toBeDisplayed += '&nbsp;');
            }
          };
        return {
          restrict: 'A',
          link: link,
          scope: {
              displayI18nString: '=',
              limitTo: '='
            }
        };
      });

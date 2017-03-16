/* global _*/
'use strict';

angular.module('metadatamanagementApp').directive('displayI18nString',
    function() {
        var link = function(scope, element) {
            var htmlElement;
            if (_.isObject(scope.i18nString) &&
                !_.isArray(scope.i18nString)) {
              if (scope.i18nString[scope.currentLanguage]) {
                htmlElement = scope.i18nString[scope.currentLanguage];
                element.attr('lang', scope.currentLanguage);
              } else {
                var secondLanguage = scope.
                currentLanguage === 'de' ? 'en' : 'de';
                htmlElement = scope.i18nString[secondLanguage];
                element.attr('lang', secondLanguage);
              }
            }
            element.empty().append(htmlElement + '&nbsp;');
          };
        return {
          restrict: 'A',
          link: link,
          scope: {
              i18nString: '=',
              currentLanguage: '='
            }
        };
      });

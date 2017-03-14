/* global _*/
'use strict';

angular.module('metadatamanagementApp').directive('i18n',
  function($compile) {
    var compile = function() {
                    return {
                      pre: function(scope, element, attrs) {
                        var htmlElement;
                        if (attrs.label) {
                          htmlElement = $compile('<div lang="{{language}}" ' +
                          'style="margin-bottom: 0.5em;" ' +
                          'ng-if="toDisplayString"><span style="font-weight: ' +
                          'bold;">{{toDisplayLabel}}:' +
                          '</span><span>{{toDisplayString}}</span></div>')
                          (scope);
                        } else {
                          htmlElement = $compile('<p lang="{{language}}">' +
                          '{{toDisplayString}}</p>')
                          (scope);
                        }
                        element.append(htmlElement);
                      }
                    };
                  };
    var controller = function($scope, $translate, $filter) {
      if (_.isObject($scope.i18nString) && !_.isArray($scope.i18nString)) {
        if ($scope.i18nString[$scope.currentLanguage]) {
          $scope.toDisplayString = $scope.i18nString[$scope.currentLanguage];
          $scope.toDisplayLabel = $filter('translate')($scope.label);
          $scope.language = $scope.currentLanguage;
        } else {
          var secondLanguage = $scope.currentLanguage === 'de' ? 'en' : 'de';
          // should be changed...
          $translate.use(secondLanguage);
          $scope.toDisplayString = $scope.i18nString[secondLanguage];
          $scope.toDisplayLabel = $filter('translate')($scope.label);
          $scope.language = secondLanguage;
          $translate.use($scope.currentLanguage);
        }
      }
    };
    return {
      restrict: 'E',
      compile: compile,
      controller: controller,
      scope: {
        i18nString: '=',
        label: '@',
        currentLanguage: '='
      }
    };
  });

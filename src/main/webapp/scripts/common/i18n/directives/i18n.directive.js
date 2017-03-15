/* global _*/
'use strict';

angular.module('metadatamanagementApp').directive('i18n',
  function($compile) {
    var compile = function() {
                    return {
                      pre: function(scope, element, attrs) {
                        var htmlElement;
                        switch (attrs.type) {
                          case 'table-cell':
                            htmlElement = $compile('<span lang="{{language}}' +
                            '">{{toDisplayString}}&nbsp;</span>')(scope);
                          break;
                          case 'image':
                            htmlElement = $compile('<div style="max-height:' +
                            '211px; overflow-y:hidden;"><img lang="' +
                            '{{language}}" alt="{{toDisplayString}}" style=' +
                            '"width: 100%; height: auto" ng-src="{{sourceLink' +
                            '}}"/><md-tooltip hide-xs hide-sm md-direction=' +
                            '"bottom" style="height: auto !important;">' +
                            '{{toDisplayString}}</md-tooltip></div>')(scope);
                          break;
                          default:
                            if (attrs.icon) {
                              htmlElement = $compile('<div class="' +
                              'fdz-truncate-string" flex><a ui-sref="' +
                              '{{state.name}}({{state.params}})"><md-icon ' +
                              'md-svg-src="/assets/images/icons/{{icon}}' +
                              '.svg"></md-icon>&nbsp;{{toDisplayLabel}}' +
                              ':&nbsp;{{toDisplayString}}</a></div>')(scope);
                            } else {
                              if (attrs.label) {
                                htmlElement = $compile('<div lang="{{language' +
                                '}}" style="margin-bottom: 0.5em;" ' +
                                'ng-if="toDisplayString"><span style="' +
                                'font-weight: bold;">{{toDisplayLabel}}:' +
                                '&nbsp;</span><span>{{toDisplayString}}' +
                                '</span></div>')(scope);
                              } else {
                                htmlElement = $compile('<p lang="{{language}}' +
                                '">{{toDisplayString}}</p>')(scope);
                              }
                            }
                          break;
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
        currentLanguage: '=',
        type: '@',
        sourceLink: '@',
        icon: '@',
        state: '='
      }
    };
  });

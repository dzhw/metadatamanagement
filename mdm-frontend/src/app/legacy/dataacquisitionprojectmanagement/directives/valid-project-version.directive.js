'use strict';

angular.module('metadatamanagementApp')
  .directive('validProjectVersion', ['$rootScope',  function($rootScope) {
    return {
      restrict: 'A',
      require: 'ngModel',
      scope: {
        validProjectVersion: '='
      },
      /* jshint -W098 */
      link: function(scope, element, attributes, ctrl) {
        /* jshint +W098 */
        ctrl.$validators['valid-project-version'] = function(modelValue) {
          if (ctrl.$isEmpty(modelValue)) {
            // consider empty models to be valid
            return true;
          }
          if (!scope.validProjectVersion) {
            // valid if there is no version yet
            return true;
          }

          return $rootScope.bowser
            .compareVersions([scope.validProjectVersion, modelValue]) <= 0;
        };
      }
    };
  }]);


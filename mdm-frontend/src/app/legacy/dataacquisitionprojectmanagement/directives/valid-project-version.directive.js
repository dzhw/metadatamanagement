'use strict';

angular.module('metadatamanagementApp')
  .directive('validProjectVersion', ['$rootScope',  function($rootScope) {
    return {
      restrict: 'A',
      require: 'ngModel',
      scope: {
        validProjectVersion: '=',
        isPreRelease: '='
      },
      /* jshint -W098 */
      link: function(scope, element, attributes, ctrl) {
        /* jshint +W098 */
        ctrl.$validators['valid-project-version'] = function(modelValue) {
          if (ctrl.$isEmpty(modelValue)) {
            // consider empty models to be valid
            return true;
          }
          // in case of pre-releases only allow minor and patch version changes
          if (scope.isPreRelease) {
            if (!scope.validProjectVersion) {
              return $rootScope.bowser
                .compareVersions(['1.0.0', modelValue]) === 1;
            } else {
              var major = scope.validProjectVersion.split(".")[0];
              var highestMajorVersion = +major + 1;
              var highestVersion = highestMajorVersion + ".0.0";
              return $rootScope.bowser
                .compareVersions([scope.validProjectVersion, modelValue]) <= 0 
                && $rootScope.bowser
                  .compareVersions([highestVersion, modelValue]) > 0;
            }
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


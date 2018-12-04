/* global _ */
'use strict';

angular.module('metadatamanagementApp').directive('fdzRequireOne',
  function() {
    return {
      require: 'ngModel',
      restrict: 'A',
      scope: {
        fdzRequireOne: '=',
        siblingPath: '@'
      },
      /* jshint -W098 */
      link: function(scope, elm, attr, ctrl) {

        var getSiblingValue = function() {
          return _.get(scope.fdzRequireOne, scope.siblingPath);
        };

        /*
         * Make sure we trigger validation if the other field changes it's
         * value. Otherwise our field will still display an error.
         */
        scope.$watch(getSiblingValue, function(oldValue, newValue) {
          if (oldValue !== newValue) {
            ctrl.$validate();
          }
        });

        ctrl.$validators['fdz-require-one'] = function(modelValue, viewValue) {
          if (viewValue) {
            return true;
          }

          var siblingValue = getSiblingValue();

          if (siblingValue !== undefined && siblingValue !== null) {
            return (siblingValue + '').trim();
          } else {
            return false;
          }
        };
      }
    };
  });

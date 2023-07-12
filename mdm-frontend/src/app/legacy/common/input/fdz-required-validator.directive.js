'use strict';

angular.module('metadatamanagementApp').directive('fdzRequired',
  function() {
        return {
            require: 'ngModel',
            restrict: 'A',
            /* jshint -W098 */
            link: function(scope, elm, attr, ctrl) {
              ctrl.$validators['fdz-required'] = function(modelValue,
                viewValue) {
                if (!attr.required) {
                  return true;
                }
                /* jshint +W098 */
                if (!viewValue || (typeof viewValue === 'string' &&
                  ctrl.$isEmpty(viewValue.trim()))) {
                  return false;
                }

                return true;
              };

              scope.$watch(attr.fdzRequired, function(value) {
                if (value != null) {
                  attr.required = value;
                } else {
                  attr.required = true;
                }
                ctrl.$validate();
              });
            }
          };
      });

'use strict';

angular.module('metadatamanagementApp').directive('fdzAutofocus', ['$timeout', 
  function($timeout) {
        return {
            restrict: 'A',
            priority: 1000,
            /* jshint -W098 */
            compile: function(element, attributes) {
              attributes.$set('mdAutofocus', '');
              return function linkFn(scope, iElement, iAttributes) {
                /* jshint +W098 */
                $timeout(function() {
                  var deleteObserver = scope.$watch(iAttributes.fdzAutofocus,
                    function(shouldFocus) {
                      if (shouldFocus || shouldFocus == null) {
                        iElement[0].focus();
                      }
                      deleteObserver();
                    });
                }, 800);
              };
            }
          };
      }]);


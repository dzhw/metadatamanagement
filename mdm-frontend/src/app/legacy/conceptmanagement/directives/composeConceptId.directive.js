'use strict';
angular.module('metadatamanagementApp').directive('fdzComposeConceptId',
  function() {
    return {
      restrict: 'A',
      require: 'ngModel',
      link: function(scope, elem, attrs, ngModelCtrl) { // jshint ignore:line
          /*Register a viewchange listener*/
          ngModelCtrl.$parsers.push(function(value) {
            if (value) {
              return 'con-' + value + '$';
            }
            return value;
          });

          ngModelCtrl.$formatters.push(function(value) {
            if (value) {
              return value.replace(/^(con-)/, '').replace(/(\$)$/, '');
            }
            return value;
          });
        }
    };
  });

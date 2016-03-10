'use strict';

angular.module('metadatamanagementApp').directive('blink', function() {
  return {
    restrict: 'E',
    transclude: true,
    scope: {},
    templateUrl: 'scripts/components/wait/' +
    'wait.html.tmpl',
    controller: 'WaitController',
    replace: true
  };
});

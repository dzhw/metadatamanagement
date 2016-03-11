'use strict';

angular.module('metadatamanagementApp').directive('loading', function() {
  return {
    restrict: 'E',
    scope: {},
    templateUrl: 'scripts/components/wait/' +
    'loading.html.tmpl',
    replace: true
  };
});

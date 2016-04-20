'use strict';

angular.module('metadatamanagementApp').directive('loading', function() {
  return {
    restrict: 'E',
    scope: {},
    templateUrl: 'scripts/common/waitSpinner/' +
    'directives/loading.html.tmpl',
    replace: true
  };
});

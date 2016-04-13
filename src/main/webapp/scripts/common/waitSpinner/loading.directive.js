'use strict';

angular.module('metadatamanagementApp').directive('loading', function() {
  return {
    restrict: 'E',
    scope: {},
    templateUrl: 'scripts/common/waitSpinner/' +
    'loading.html.tmpl',
    replace: true
  };
});

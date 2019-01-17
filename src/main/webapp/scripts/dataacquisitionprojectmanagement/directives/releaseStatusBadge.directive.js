'use strict';
angular.module('metadatamanagementApp')
  .directive('releaseStatusBadge', function() {
    return {
      templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
        'directives/releaseStatusBadge.directive.html.tmpl',
      scope: {
        released: '='
      }
    };
  });

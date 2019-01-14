'use strict';
angular.module('metadatamanagementApp')
  .directive('releaseStatusBadge', function() {
    return {
      templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
        'views/releaseStatusBadge.directive.html.tmpl',
      scope: {
        released: '='
      }
    };
  });

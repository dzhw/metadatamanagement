'use strict';

angular.module('metadatamanagementApp')
  .directive('projectStatusBadge', function() {
    return {
      templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
        'views/projectStatusBadge.directive.html.tmpl',
      scope: {
        assigneeGroup: '@'
      }
    };
  });

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectStatusBadge', function() {
    return {
      templateUrl: 'scripts/dataacquisitionprojectmanagement/' +
        'directives/projectStatusBadge.directive.html.tmpl',
      scope: {
        assigneeGroup: '@'
      }
    };
  });

'use strict';

angular.module('metadatamanagementApp')
  .directive('projectStatusBadge', function() {
    return {
      templateUrl: 'scripts/common/projectstatusbadge/projectStatusBadge' +
        '.directive.html.tmpl',
      scope: {
        assigneeGroup: '@'
      }
    };
  });

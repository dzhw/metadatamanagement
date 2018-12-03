'use strict';

angular.module('metadatamanagementApp')
  .directive('projectStatusLabel', function() {
    return {
      templateUrl: 'scripts/common/projectstatuslabel/projectStatusLabel' +
        '.directive.html.tmpl',
      scope: {
        assigneeGroup: '@'
      }
    };
  });

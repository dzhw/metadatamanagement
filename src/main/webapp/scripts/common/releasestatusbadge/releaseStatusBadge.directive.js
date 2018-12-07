'use strict';
angular.module('metadatamanagementApp')
  .directive('releaseStatusBadge', function() {
    return {
      templateUrl: 'scripts/common/releasestatusbadge/' +
        'releaseStatusBadge.directive.html.tmpl',
      scope: {
        released: '='
      }
    };
  });

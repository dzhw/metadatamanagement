'use strict';

angular.module('metadatamanagementApp').config(["$stateProvider",
  function($stateProvider) {
    $stateProvider.state(
      'error', {
        parent: 'site',
        url: '/error',
        data: {
          authorities: [],
          pageTitle: 'global.error.title'
        },
        onEnter: ["BreadcrumbService", function(BreadcrumbService) {
          BreadcrumbService.updateToolbarHeader({'stateName': 'error'});
        }],
        views: {
          'content@': {
            templateUrl: 'scripts/common/error/' +
              'error.html.tmpl'
          }
        }
      });
  }]);

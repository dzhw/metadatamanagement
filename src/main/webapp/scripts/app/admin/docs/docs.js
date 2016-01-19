'use strict';

angular.module('metadatamanagementApp').config(function($stateProvider) {
  $stateProvider.state('docs', {
    parent: 'admin',
    url: '/docs',
    data: {
      authorities: ['ROLE_ADMIN'],
      pageTitle: 'global.menu.admin.apidocs'
    },
    views: {
      'content@': {
        templateUrl: 'scripts/app/admin/docs/docs.html.tmpl'
      }
    },
    resolve: {
      translatePartialLoader: ['$translate', function($translate) {
        return $translate.refresh();
      }]
    }
  });
});

'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state(
      'error', {
        parent: 'site',
        url: '/error',
        data: {
          authorities: [],
          pageTitle: 'global.error.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/common/error/' +
              'error.html.tmpl'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translatePartialLoader',
            function($translatePartialLoader) {
              $translatePartialLoader.addPart('global');
            }
          ]
        }
      });
  });

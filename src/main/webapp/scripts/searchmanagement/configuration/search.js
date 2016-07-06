'use strict';

angular.module('metadatamanagementApp').config(
    function($stateProvider) {
      $stateProvider.state('search', {
        parent: 'site',
        url: '/',
        data: {
          authorities: [],
          pageTitle: 'global.menu.search.title'
        },
        views: {
          'content@': {
            templateUrl: 'scripts/searchmanagement/views/search.html.tmpl',
            controller: 'SearchController'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate',
            '$translatePartialLoader',
            function($translate, $translatePartialLoader) {
              $translatePartialLoader.addPart('global');
              $translatePartialLoader.addPart('search');
              $translatePartialLoader.addPart('pagination');
              $translatePartialLoader.addPart('dataAcquisitionProject');
              return $translate.refresh();
            }
          ]
        }
      });
    });

'use strict';

angular.module('metadatamanagementApp').config(
    function($stateProvider) {
      $stateProvider.state('variablesearch', {
        parent: 'site',
        url: '/variablesearch:query?',
        data: {
          //authorities: ['ROLE_USER','ROLE_ADMIN']
          authorities: []
        },
        views: {
          'content@': {
            templateUrl: 'scripts/app/search/variablesearch.html',
            controller: 'SearchController'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate', '$translatePartialLoader',
              function($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('variablesearch');
                return $translate.refresh();
              }]
        }
      });
    });

'use strict';

angular.module('metadatamanagementApp').config(
    function($stateProvider) {
      $stateProvider.state('search', {
        parent: 'site',
        url: '/',
        data: {
          authorities: [],
          pageTitle: 'main.title'
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
              $translatePartialLoader.addPart('variablesearch');
              $translatePartialLoader.addPart('pagination');
              return $translate.refresh();
            }
          ]
        }
      });
    })
  //Error Handler for non translated angular js elements.
  .config(function($translateProvider) {
    $translateProvider.useMissingTranslationHandler('translationErrorHandler');
  });

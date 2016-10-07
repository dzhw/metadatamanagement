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
        mainTranslatePartialLoader: ['$translatePartialLoader',
          function($translatePartialLoader) {
            $translatePartialLoader.addPart('search.management');
            $translatePartialLoader.addPart('variable.management');
            $translatePartialLoader.addPart('question.management');
            $translatePartialLoader.addPart('dataSet.management');
            $translatePartialLoader.addPart('survey.management');
            $translatePartialLoader.addPart('study.management');
            $translatePartialLoader.addPart('dataSet.management');
            $translatePartialLoader.addPart('relatedPublication.management');
            $translatePartialLoader.addPart('global');
          }
        ]
      }
    });
  });

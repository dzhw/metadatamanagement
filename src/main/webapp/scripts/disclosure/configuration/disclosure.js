'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('disclosure', {
      parent: 'site',
      url: '/disclosure',
      data: {
        authorities: [],
        pageTitle: 'disclosure.title'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/disclosure/views/' +
            'disclosure.html.tmpl',
          controller: 'DisclosureController'
        }
      },
      resolve: {
        mainTranslatePartialLoader: ['$translate',
          '$translatePartialLoader',
          function($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('disclosure');
          }
        ]
      }
    });
  });

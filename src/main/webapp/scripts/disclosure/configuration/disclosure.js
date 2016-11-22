'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('disclosure', {
      parent: 'site',
      url: '/disclosure',
      data: {
        authorities: []
      },
      views: {
        'content@': {
          templateUrl: 'scripts/disclosure/views/' +
            'disclosure.html.tmpl',
          controller: 'DisclosureController'
        }
      },
      resolve: {
        mainTranslatePartialLoader: ['$translatePartialLoader',
          function($translatePartialLoader) {
            $translatePartialLoader.addPart('disclosure');
          }
        ]
      }
    });
  });

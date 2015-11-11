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
            templateUrl: 'scripts/app/disclosure/disclosure.html',
            controller: 'DisclosureController'
          }
        },
        resolve: {
          mainTranslatePartialLoader: ['$translate', '$translatePartialLoader',
              function($translate, $translatePartialLoader) {
                $translatePartialLoader.addPart('disclosure');
                return $translate.refresh();
              }]
        }
      });
    });

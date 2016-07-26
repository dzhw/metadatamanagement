'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('activate', {
      parent: 'account',
      url: '/activate?key',
      data: {
        authorities: [],
        pageTitle: 'activate.title'
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/activate/' +
            'activate.html.tmpl',
          controller: 'ActivationController'
        }
      },
      resolve: {
        translatePartialLoader: ['$translate', '$translatePartialLoader',
          function($translate, $translatePartialLoader) {
            $translatePartialLoader.addPart('activate');
            // return $translate.refresh();
          }
        ]
      }
    });
  });

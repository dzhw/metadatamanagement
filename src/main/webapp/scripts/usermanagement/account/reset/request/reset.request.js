'use strict';

angular
    .module('metadatamanagementApp')
    .config(
        function($stateProvider) {
          $stateProvider
              .state(
                  'requestReset',
                  {
                    parent: 'account',
                    url: '/reset/request',
                    data: {
                      authorities: []
                    },
                    views: {
                      'content@': {
                        templateUrl: 'scripts/usermanagement/account/' +
                        'reset/request/reset.request.html.tmpl',
                        controller: 'RequestResetController'
                      }
                    },
                    resolve: {
                      translatePartialLoader: ['$translatePartialLoader',
                          function($translatePartialLoader) {
                            $translatePartialLoader.addPart('reset');
                          }]
                    }
                  });
        });

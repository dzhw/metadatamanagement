'use strict';

angular
  .module('metadatamanagementApp')
  .config(
    function($stateProvider) {
      $stateProvider
        .state(
          'user-management', {
            parent: 'admin',
            url: '/user-management',
            data: {
              authorities: ['ROLE_ADMIN'],
              pageTitle: 'user-management.home.title'
            },
            views: {
              'content@': {
                templateUrl: 'scripts/administration/usermanagement/' +
                  'user-management.html.tmpl',
                controller: 'UserManagementController'
              }
            },
            resolve: {
              translatePartialLoader: ['$translate',
                '$translatePartialLoader',
                function($translate, $translatePartialLoader) {
                  $translatePartialLoader.addPart('user.management');
                  // return $translate.refresh();
                }
              ]
            }
          })
        .state(
          'user-management-detail', {
            parent: 'admin',
            url: '/user-management/:login',
            data: {
              authorities: ['ROLE_ADMIN'],
              pageTitle: 'user-management.detail.title'
            },
            views: {
              'content@': {
                templateUrl: 'scripts/administration/' +
                  'usermanagement/user-management-detail.html.tmpl',
                controller: 'UserManagementDetailController'
              }
            },
            resolve: {
              translatePartialLoader: ['$translate',
                '$translatePartialLoader',
                function($translate, $translatePartialLoader) {
                  $translatePartialLoader.addPart('user.management');
                  // return $translate.refresh();
                }
              ]
            }
          });
    });

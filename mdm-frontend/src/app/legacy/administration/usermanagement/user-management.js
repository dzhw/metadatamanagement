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
              authorities: ['ROLE_ADMIN']
            },
            views: {
              'content@': {
                templateUrl: 'scripts/administration/usermanagement/' +
                  'user-management.html.tmpl',
                controller: 'UserManagementController'
              }
            }
          })
        .state(
          'user-management-detail', {
            parent: 'admin',
            url: '/user-management/:login',
            data: {
              authorities: ['ROLE_ADMIN']
            },
            views: {
              'content@': {
                templateUrl: 'scripts/administration/' +
                  'usermanagement/user-management-detail.html.tmpl',
                controller: 'UserManagementDetailController'
              }
            }
          });
    });

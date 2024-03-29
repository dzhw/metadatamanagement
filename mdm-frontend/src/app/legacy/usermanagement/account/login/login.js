'use strict';

angular.module('metadatamanagementApp').config([
  '$stateProvider',

  function($stateProvider) {
    $stateProvider.state('login', {
      parent: 'account',
      url: '/login',
      data: {
        authorities: []
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/login/' +
            'login.html.tmpl',
          controller: 'LoginController'
        }
      }
    });
  }]);

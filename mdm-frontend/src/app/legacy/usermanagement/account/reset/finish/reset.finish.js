'use strict';

angular.module('metadatamanagementApp').config(
  function($stateProvider) {
    $stateProvider.state('finishReset', {
      parent: 'account',
      url: '/reset/finish?key',
      data: {
        authorities: []
      },
      views: {
        'content@': {
          templateUrl: 'scripts/usermanagement/account/' +
            'reset/finish/' +
            'reset.finish.html.tmpl',
          controller: 'ResetFinishController'
        }
      }
    });
  });

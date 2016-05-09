'use strict';

angular.module('metadatamanagementApp')
  .factory('Variable', function($resource, $state) {
    return $resource('api/variables/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        },
        interceptor: {
          responseError: function(response) {
            if (response.status !== 401) {
              $state.go('error');
            }
          }
        }
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE',
      }
    });
  });

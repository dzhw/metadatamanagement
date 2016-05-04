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
          responseError: function() {
            $state.go('error');
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

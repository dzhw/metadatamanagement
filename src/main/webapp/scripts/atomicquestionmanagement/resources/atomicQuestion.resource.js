'use strict';

angular.module('metadatamanagementApp')
  .factory('AtomicQuestionResource', function($resource, $state) {
    return $resource('api/atomic-questions/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        },
        interceptor: {
          responseError: function(response) {
            if (response.status === 404) {
              $state.go('error');
            }
          }
        }
      },
      'save': {
        method: 'PUT'
      }
    });
  });

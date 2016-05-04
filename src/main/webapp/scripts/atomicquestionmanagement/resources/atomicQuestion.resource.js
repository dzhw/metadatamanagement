'use strict';

angular.module('metadatamanagementApp')
  .factory('AtomicQuestion', function($resource, $state) {
    return $resource('api/atomic-questions/:id', {id: '@id'}, {
      'get': {
        method: 'GET',
        params:  {projection: 'complete'},
        interceptor: {
          responseError: function() {
            $state.go('error');
          }
        }
      },
      'save': {
        method: 'PUT'
      }
    });
  });

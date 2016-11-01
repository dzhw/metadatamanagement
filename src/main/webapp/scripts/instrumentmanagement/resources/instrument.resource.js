'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('InstrumentResource', function($resource, $state) {
    return $resource('/api/instrument/:id', {
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
        },
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });

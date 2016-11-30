'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('InstrumentResource', function($resource) {
    return $resource('/api/instruments/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        }
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });

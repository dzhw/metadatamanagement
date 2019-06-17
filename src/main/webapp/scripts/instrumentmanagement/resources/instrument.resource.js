'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('InstrumentResource', function($resource) {
    return $resource('/api/instruments/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });

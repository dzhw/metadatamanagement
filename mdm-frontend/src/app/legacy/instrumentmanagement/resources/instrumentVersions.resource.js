'use strict';

/* Instrument Versions Resource */
angular.module('metadatamanagementApp')
  .factory('InstrumentVersionsResource', function($resource) {
    return $resource('/api/instruments/:id/versions', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

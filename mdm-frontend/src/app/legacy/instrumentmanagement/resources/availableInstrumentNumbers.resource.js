'use strict';

/* Available Instrument Numbers Resource */
angular.module('metadatamanagementApp')
  .factory('AvailableInstrumentNumbersResource', ['$resource',  function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/available-instrument-numbers', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);


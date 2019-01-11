'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllInstrumentsResource', function($resource) {
    return $resource('/api/data-acquisition-projects/:id/instruments', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  });

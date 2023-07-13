'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllInstrumentsResource', ['$resource',  function($resource) {
    return $resource('/api/data-acquisition-projects/:id/instruments', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  }]);


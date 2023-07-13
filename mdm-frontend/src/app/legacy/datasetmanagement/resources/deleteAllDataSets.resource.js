'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllDataSetsResource', ['$resource',  function($resource) {
    return $resource('/api/data-acquisition-projects/:id/data-sets', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  }]);


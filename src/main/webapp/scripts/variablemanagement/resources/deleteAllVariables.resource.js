'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllVariablesResource', function($resource) {
    return $resource('/api/data-acquisition-projects/:id/variables', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  });

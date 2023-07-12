'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllDataPackagesResource', function($resource) {
    return $resource('/api/data-acquisition-projects/:id/data-packages', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  });

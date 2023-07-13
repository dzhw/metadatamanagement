'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllDataPackagesResource', ['$resource',  function($resource) {
    return $resource('/api/data-acquisition-projects/:id/data-packages', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  }]);


'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllPublicationsResource', function($resource) {
    return $resource('/api/data-packages/:id/publications', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  });

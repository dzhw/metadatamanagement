'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllPublicationsResource', function($resource) {
    return $resource('/api/studies/:id/publications', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  });

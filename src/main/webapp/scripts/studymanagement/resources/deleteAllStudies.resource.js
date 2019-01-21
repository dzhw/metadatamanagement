'use strict';

angular.module('metadatamanagementApp')
  .factory('DeleteAllStudiesResource', function($resource) {
    return $resource('/api/data-acquisition-projects/:id/studies', {
      id: '@id'
    }, {
      'deleteAll': {
        method: 'DELETE'
      }
    });
  });

'use strict';

/* Study Versions Resource */
angular.module('metadatamanagementApp')
  .factory('StudyVersionsResource', function($resource) {
    return $resource('/api/studies/:id/versions', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

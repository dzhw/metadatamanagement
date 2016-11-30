'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectResource', function($resource) {
    return $resource('/api/data-acquisition-projects/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        }
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });

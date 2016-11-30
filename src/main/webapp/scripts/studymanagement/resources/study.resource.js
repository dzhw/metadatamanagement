/* Author: Daniel Katzberg */
'use strict';

/* Study Resource */
angular.module('metadatamanagementApp')
  .factory('StudyResource', function($resource) {
    return $resource('/api/studies/:id', {
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

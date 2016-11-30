'use strict';

angular.module('metadatamanagementApp')
  .factory('QuestionResource', function($resource) {
    return $resource('api/questions/:id', {
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
        method: 'DELETE',
      }
    });
  });

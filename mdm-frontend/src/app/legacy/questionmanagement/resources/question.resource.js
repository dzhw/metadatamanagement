'use strict';

angular.module('metadatamanagementApp')
  .factory('QuestionResource', ['$resource',  function($resource) {
    return $resource('api/questions/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE',
      }
    });
  }]);


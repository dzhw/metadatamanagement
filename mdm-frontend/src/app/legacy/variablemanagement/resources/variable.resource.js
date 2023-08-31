'use strict';

angular.module('metadatamanagementApp')
  .factory('VariableResource', ['$resource',  function($resource) {
    return $resource('api/variables/:id', {
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


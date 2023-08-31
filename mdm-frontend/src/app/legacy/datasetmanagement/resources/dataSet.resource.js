'use strict';

angular.module('metadatamanagementApp')
  .factory('DataSetResource', ['$resource',  function($resource) {
    return $resource('api/data-sets/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  }]);


'use strict';

angular.module('metadatamanagementApp')
  .factory('Variable', function($resource) {
    return $resource('api/variables/:id', {id: '@id'}, {
      'get': {
        method: 'GET',
        params:  {projection: 'complete'}
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE',
      }
    });
  });

'use strict';

angular.module('metadatamanagementApp')
  .factory('DataSet', function($resource) {
    return $resource('api/data_sets/:id', {id: '@id'}, {
      'get': {
        method: 'GET',
        params:  {projection: 'complete'}
      },
      'save': {
        method: 'PUT'
      }
    });
  });

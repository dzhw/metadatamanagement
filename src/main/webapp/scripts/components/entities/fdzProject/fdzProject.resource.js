'use strict';

angular.module('metadatamanagementApp')
    .factory('FdzProject', function($resource) {
      return $resource('/api/fdz_projects/:id',
        {id: '@id'}, {
        'get': {
          method: 'GET',
          params:  {projection: 'complete'}
        },
        'save': {
          method: 'PUT'
        },
        'delete': {
          method: 'DELETE'
        }
      });
    });

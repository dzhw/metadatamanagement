'use strict';

angular.module('metadatamanagementApp')
    .factory('FdzProject', function($resource) {
      return $resource('/api/fdz_projects/:id',
        {id: '@id'}, {
        'get': {
          method: 'GET',
          params:  {projection: 'complete'}
        },
        'create': {
          method: 'POST'
        },
        'update': {
          method: 'PUT'
        },
        'delete': {
          method: 'DELETE'
        },
        'findOneByName': {
          url: '/api/fdz_projects/search/findOneByName',
          method: 'GET',
          params:  {projection: 'complete'}
        }
      });
    });

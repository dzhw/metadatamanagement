'use strict';

angular.module('metadatamanagementApp')
    .factory('FdzProject', function($resource) {
      return $resource('/api/fdz_projects/:id',
        {id: '@id'}, {
        'query': {
          method: 'GET',
          params: {projection: 'complete'}
        },
        'get': {
          method: 'GET',
          params: {projection: 'complete'}
        },
        'update': {
          method: 'PUT'
        },
        'create': {
          method: 'POST'
        },
        'delete': {
          method: 'DELETE',
        },
        'findOneByName': {
          method: 'GET',
          params: {projection: 'complete'},
          url: '/api/fdz_projects/search/findOneByName'
        }
      });
    });

'use strict';

angular.module('metadatamanagementApp')
    .factory('DataAcquisitionProject', function($resource) {
      return $resource('/api/data_acquisition_projects/:id',
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

'use strict';

/* Available DataSet Numbers Resource */
angular.module('metadatamanagementApp')
  .factory('AvailableDataSetNumbersResource', function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/available-data-set-numbers', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

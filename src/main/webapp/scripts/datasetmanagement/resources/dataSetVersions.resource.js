'use strict';

/* DataSet Versions Resource */
angular.module('metadatamanagementApp')
  .factory('DataSetVersionsResource', function($resource) {
    return $resource('/api/data-sets/:id/versions', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

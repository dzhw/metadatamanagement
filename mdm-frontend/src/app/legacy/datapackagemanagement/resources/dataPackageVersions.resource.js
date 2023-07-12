'use strict';

/* Data Package Versions Resource */
angular.module('metadatamanagementApp')
  .factory('DataPackageVersionsResource', function($resource) {
    return $resource('/api/data-packages/:id/versions', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

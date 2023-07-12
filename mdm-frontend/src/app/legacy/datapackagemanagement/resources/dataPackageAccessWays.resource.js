'use strict';

/* Data Package AccessWays Resource */
angular.module('metadatamanagementApp')
  .factory('DataPackageAccessWaysResource', function($resource) {
    return $resource('/api/data-packages/:id/access-ways', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

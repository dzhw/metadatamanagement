'use strict';

/* Study AccessWays Resource */
angular.module('metadatamanagementApp')
  .factory('StudyAccessWaysResource', function($resource) {
    return $resource('/api/studies/:id/access-ways', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

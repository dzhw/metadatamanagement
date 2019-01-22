'use strict';

angular.module('metadatamanagementApp')
  .factory('OrderResource', function($resource) {
    return $resource('api/orders/:id', {id: '@id'}, {
      'save': {
        method: 'POST'
      }
    });
  });

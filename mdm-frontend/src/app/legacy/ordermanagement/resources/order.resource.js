'use strict';

angular.module('metadatamanagementApp')
  .factory('OrderResource', ['$resource',  function($resource) {
    return $resource('api/orders/:id', {id: '@id'}, {
      'update': {
        method: 'PUT'
      }
    });
  }]);


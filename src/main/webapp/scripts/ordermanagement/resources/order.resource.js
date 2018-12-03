'use strict';

angular.module('metadatamanagementApp')
  .factory('OrderResource', function($resource) {
    return $resource('api/orders', {}, {
      'save': {
        method: 'POST'
      }
    });
  });

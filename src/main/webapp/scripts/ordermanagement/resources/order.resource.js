'use strict';

angular.module('metadatamanagementApp')
  .factory('OrderResource', function($resource) {
    return $resource('api/order', {}, {
      'save': {
        method: 'POST'
      }
    });
  });

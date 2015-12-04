'use strict';

angular.module('metadatamanagementApp').factory('User', function($resource) {
  return $resource('api/users/:login', {}, {
    'query': {
      method: 'GET',
      isArray: true
    },
    'get': {
      method: 'GET',
      transformResponse: function(data) {
        // data might be empty if 404
        if (data) {
          data = angular.fromJson(data);
          return data;
        }
      }
    },
    'update': {
      method: 'PUT'
    }
  });
});

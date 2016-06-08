'use strict';

angular.module('metadatamanagementApp').factory('LogsResource',
  function($resource) {
    return $resource('api/logs', {}, {
      'findAll': {
        method: 'GET',
        isArray: true
      },
      'changeLevel': {
        method: 'PUT'
      }
    });
  });

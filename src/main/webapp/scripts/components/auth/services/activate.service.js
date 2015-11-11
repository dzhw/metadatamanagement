'use strict';

angular.module('metadatamanagementApp').factory('Activate',
    function($resource) {
      return $resource('api/activate', {}, {
        'get': {
          method: 'GET',
          params: {},
          isArray: false
        }
      });
    });

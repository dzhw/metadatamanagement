'use strict';

angular.module('metadatamanagementApp').factory('ActivateResource',
    function($resource) {
      return $resource('api/activate', {}, {
        'get': {
          method: 'GET',
          params: {},
          isArray: false
        }
      });
    });

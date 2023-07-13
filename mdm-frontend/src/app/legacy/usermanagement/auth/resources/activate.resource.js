'use strict';

angular.module('metadatamanagementApp').factory('ActivateResource', ['$resource', 
    function($resource) {
      return $resource('api/activate', {}, {
        'get': {
          method: 'GET',
          params: {},
          isArray: false
        }
      });
    }]);


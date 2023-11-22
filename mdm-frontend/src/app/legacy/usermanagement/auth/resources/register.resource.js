'use strict';

angular.module('metadatamanagementApp').factory('RegisterResource', ['$resource', 
    function($resource) {
      return $resource('api/register', {}, {});
    }]);


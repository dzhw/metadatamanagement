'use strict';

angular.module('metadatamanagementApp').factory('RegisterResource',
    function($resource) {
      return $resource('api/register', {}, {});
    });

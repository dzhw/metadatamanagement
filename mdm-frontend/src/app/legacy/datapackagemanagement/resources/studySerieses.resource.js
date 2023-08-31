'use strict';

/* Study Serieses Resource */
angular.module('metadatamanagementApp')
  .factory('StudySeriesesResource', ['$resource',  function($resource) {
    return $resource('/api/study-serieses', {}, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);


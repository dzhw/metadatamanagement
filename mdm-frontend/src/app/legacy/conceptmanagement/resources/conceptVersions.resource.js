'use strict';

angular.module('metadatamanagementApp')
  .factory('ConceptVersionsResource', ['$resource',  function($resource) {
    return $resource('/api/concepts/:id/versions', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);


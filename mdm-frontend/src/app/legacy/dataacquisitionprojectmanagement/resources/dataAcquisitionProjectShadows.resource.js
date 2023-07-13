'use strict';
angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectShadowsResource', ['$resource', 
    function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/shadows', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      }
    });
  }]);


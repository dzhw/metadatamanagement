'use strict';
angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectShadowsActionResource', ['$resource', 
    function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/shadows/:version/action', {
      id: '@id',
      version: '@version'
    }, {
      'get': {
        method: 'GET'
      }
    });
  }]);


'use strict';
angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectShadowsResource',
    function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/shadows', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      }
    });
  });

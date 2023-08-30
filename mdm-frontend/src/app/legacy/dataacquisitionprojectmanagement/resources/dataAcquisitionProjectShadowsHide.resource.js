'use strict';
angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectShadowsHideResource', ['$resource', 
    function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/shadows/:version/hidden', {
      id: '@id',
      version: '@version'
    }, {
      'hide': {
        method: 'POST'
      },
      'unhide': {
        method: 'DELETE'
      }
    });
  }]);


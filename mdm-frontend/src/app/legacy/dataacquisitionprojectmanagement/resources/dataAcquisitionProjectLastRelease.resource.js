'use strict';
/*
  Resource url definition for the post validation..
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectLastReleaseResource', ['$resource', 
    function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/releases/last', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      }
    });
  }]);


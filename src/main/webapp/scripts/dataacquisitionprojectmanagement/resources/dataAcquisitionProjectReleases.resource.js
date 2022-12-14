'use strict';
/*
  Resource url definition for the post validation..
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectReleasesResource',
    function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/releases', {
      id: '@id',
      noBeta: true,
      onlyNotHidden: true
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

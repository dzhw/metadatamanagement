'use strict';
/*
  Resource url definition for the relase process.
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectReleaseResource', function($resource) {
    return $resource('/api/data-acquisition-projects/:id/release', {
      id: '@id'
    }, {
      'release': {
        method: 'POST'
      }
    });
  });

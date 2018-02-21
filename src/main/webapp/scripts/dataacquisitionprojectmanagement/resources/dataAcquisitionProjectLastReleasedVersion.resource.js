'use strict';
/*
  Resource url definition for the post validation..
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectLastReleasedVersionResource',
    function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/last-released-version', {
      id: '@id'
    }, {
      'lastReleasedVersion': {
        method: 'GET'
      }
    });
  });

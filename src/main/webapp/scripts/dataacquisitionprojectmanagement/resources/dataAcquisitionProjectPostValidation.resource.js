'use strict';
/*
  Resource url definition for the post validation..
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectPostValidationResource', function($resource) {
    return $resource('/api/data-acquisition-projects/:id/post-validate', {
      id: '@id'
    }, {
      'postValidate': {
        method: 'POST'
      }
    });
  });

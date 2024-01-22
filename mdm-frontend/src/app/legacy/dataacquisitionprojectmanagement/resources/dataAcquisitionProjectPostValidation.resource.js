'use strict';
/*
  Resource url definition for the post validation..
  @author Daniel Katzberg
*/

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectPostValidationResource', ['$resource',  function($resource) {
    return $resource('/api/data-acquisition-projects/:id/post-validate', {
      id: '@id',
      version: '@version'
    }, {
      'postValidate': {
        method: 'POST',
        url: '/api/data-acquisition-projects/:id/post-validate'
      },
      'postValidatePreRelease': {
        method: 'POST',
        url: '/api/data-acquisition-projects/:id/post-validate-pre-release'
      }
    });
  }]);


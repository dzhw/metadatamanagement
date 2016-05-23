'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectPostValidationResource', function($resource,
    $state) {
    return $resource('/api/data-acquisition-projects/:id/post-validate', {
      id: '@id'
    }, {
      'postValidate': {
        method: 'POST'
      }
    });
  });

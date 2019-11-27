'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectAttachmentsResource',
    function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/attachments', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      }
    });
  });

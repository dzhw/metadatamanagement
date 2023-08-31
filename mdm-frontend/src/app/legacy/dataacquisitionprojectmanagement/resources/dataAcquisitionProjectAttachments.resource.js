'use strict';

angular.module('metadatamanagementApp')
  .factory('DataAcquisitionProjectAttachmentsResource', ['$resource', 
    function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/attachments', {
      id: '@id'
    }, {
      'get': {
        method: 'GET'
      }
    });
  }]);


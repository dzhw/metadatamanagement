'use strict';

angular.module('metadatamanagementApp')
  .factory('ConceptAttachmentVersionsResource', ['$resource',  function($resource) {
    return $resource('/api/concepts/:conceptId/attachments/:filename/' +
    'versions', {
      dataPackageId: '@conceptId',
      filename: '@filename'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);


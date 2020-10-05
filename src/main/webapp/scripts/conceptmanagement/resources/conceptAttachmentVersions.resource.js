'use strict';

angular.module('metadatamanagementApp')
  .factory('ConceptAttachmentVersionsResource', function($resource) {
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
  });

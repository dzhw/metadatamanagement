'use strict';

angular.module('metadatamanagementApp')
  .factory('ConceptAttachmentVersionsResource', function($resource) {
    return $resource('/api/concepts/:conceptId/attachments/:filename/' +
    'versions', {
      studyId: '@conceptId',
      filename: '@filename'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

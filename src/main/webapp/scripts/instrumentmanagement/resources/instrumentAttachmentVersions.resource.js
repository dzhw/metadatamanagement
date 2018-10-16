'use strict';

angular.module('metadatamanagementApp')
  .factory('InstrumentAttachmentVersionsResource', function($resource) {
    return $resource(
      '/api/instruments/:instrumentId/attachments/:filename/versions', {
      studyId: '@instrumentId',
      filename: '@filename'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

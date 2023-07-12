'use strict';

angular.module('metadatamanagementApp')
  .factory('InstrumentAttachmentVersionsResource', function($resource) {
    return $resource(
      '/api/instruments/:instrumentId/attachments/:filename/versions', {
      instrumentId: '@instrumentId',
      filename: '@filename'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

'use strict';

angular.module('metadatamanagementApp')
  .factory('DataSetAttachmentVersionsResource', ['$resource',  function($resource) {
    return $resource(
      '/api/data-sets/:dataSetId/attachments/:filename/versions', {
      dataSetId: '@dataSetId',
      filename: '@filename'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);


'use strict';

/* Data Package Attachments Versions Resource */
angular.module('metadatamanagementApp')
  .factory('DataPackageAttachmentVersionsResource', ['$resource',  function($resource) {
    return $resource(
      '/api/data-packages/:dataPackageId/attachments/:filename/versions', {
      dataPackageId: '@dataPackageId',
      filename: '@filename'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);


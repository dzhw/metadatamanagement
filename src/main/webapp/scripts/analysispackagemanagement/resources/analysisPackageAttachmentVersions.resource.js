'use strict';

angular.module('metadatamanagementApp')
  .factory('AnalysisPackageAttachmentVersionsResource', function($resource) {
    return $resource(
      '/api/analysis-packages/:analysisPackageId/' +
      'attachments/:filename/versions', {
      dataPackageId: '@analysisPackageId',
      filename: '@filename'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

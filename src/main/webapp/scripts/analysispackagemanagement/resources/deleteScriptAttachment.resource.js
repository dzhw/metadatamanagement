'use strict';

angular.module('metadatamanagementApp')
  .factory('ScriptSoftwarePackagesResource', function($resource) {
    return $resource(
      '/api/analysis-packages/:analysisPackageId' +
      '/scripts/:scriptUuid/attachments/:fileName', {
        analysisPackageId: '@analysisPackageId',
        scriptUuid: '@scriptUuid',
        fileName: '@fileName'
      }, {
        'delete': {
          method: 'DELETE'
        }
      });
  });

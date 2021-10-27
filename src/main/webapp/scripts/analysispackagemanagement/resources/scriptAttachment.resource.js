'use strict';

angular.module('metadatamanagementApp')
  .factory('ScriptAttachmentResource', function($resource,
                                                CleanJSObjectService) {
    return $resource(
      '/api/analysis-packages/:analysisPackageId' +
      '/scripts/:scriptUuid/attachments/:fileName', {
        analysisPackageId: '@analysisPackageId',
        fileName: '@fileName',
        scriptUuid: '@scriptUuid'
      }, {
        'findByAnalysisPackageId': {
          method: 'GET',
          isArray: true
        },
        'save': {
          method: 'PUT',
          transformRequest: function(attachment) {
            var copy = angular.copy(attachment);
            CleanJSObjectService.deleteEmptyStrings(copy);
            CleanJSObjectService.removeEmptyJsonObjects(copy);
            return angular.toJson(copy);
          },
          'delete': {
            method: 'DELETE'
          }
        }
      });
  });

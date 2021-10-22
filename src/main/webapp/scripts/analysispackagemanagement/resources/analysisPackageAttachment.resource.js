'use strict';

angular.module('metadatamanagementApp')
.factory('AnalysisPackageAttachmentResource', function($resource,
  CleanJSObjectService) {
      return $resource(
        '/api/analysis-packages/:analysisPackageId/attachments/:fileName', {
          analysisPackageId: '@analysisPackageId',
          fileName: '@fileName'
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
          }
        }
      });
    });

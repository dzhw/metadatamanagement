'use strict';

angular.module('metadatamanagementApp')
  .factory('ScriptSoftwarePackagesResource', function($resource,
                                                      CleanJSObjectService) {
    return $resource(
      '/api/analysis-packages/:analysisPackageId' +
      '/scripts/attachments/:fileName', {
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

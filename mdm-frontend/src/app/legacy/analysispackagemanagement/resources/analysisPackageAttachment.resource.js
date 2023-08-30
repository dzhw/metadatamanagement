'use strict';

angular.module('metadatamanagementApp')
  .factory('AnalysisPackageAttachmentResource', ['$resource', 'CleanJSObjectService',  function($resource,
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
          params: {
            analysisPackageId: '@analysisPackageId',
            fileName: function(data) {
              var value = '';
              if (data.fileName.charAt(0) === '.') {
                value = '\\';
              }
              return value + data.fileName;
            }
          },
          transformRequest: function(attachment) {
            var copy = angular.copy(attachment);
            CleanJSObjectService.deleteEmptyStrings(copy);
            CleanJSObjectService.removeEmptyJsonObjects(copy);
            return angular.toJson(copy);
          }
        },
        'delete': {
          method: 'DELETE',
          params: {
            analysisPackageId: '@analysisPackageId',
            fileName: function(data) {
              var value = '';
              if (data.fileName.charAt(0) === '.') {
                value = '\\';
              }
              return value + data.fileName;
            }
          }
        }
      });
  }]);


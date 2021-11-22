'use strict';

/* Data Package Attachment Resource */
angular.module('metadatamanagementApp')
  .factory('DataPackageAttachmentResource', function($resource,
                                                     CleanJSObjectService) {
    return $resource(
      '/api/data-packages/:dataPackageId/attachments/:fileName', {
        dataPackageId: '@dataPackageId',
        fileName: '@fileName'
      }, {
        'findByDataPackageId': {
          method: 'GET',
          isArray: true
        },
        'save': {
          method: 'PUT',
          params: {
            analysisPackageId: '@dataPackageId',
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
  });

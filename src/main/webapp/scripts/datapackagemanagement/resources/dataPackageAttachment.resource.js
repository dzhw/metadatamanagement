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
          transformRequest: function(attachment) {
            var copy = angular.copy(attachment);
            CleanJSObjectService.deleteEmptyStrings(copy);
            CleanJSObjectService.removeEmptyJsonObjects(copy);
            return angular.toJson(copy);
          }
        }
      });
    });

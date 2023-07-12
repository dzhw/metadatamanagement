'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('DataSetAttachmentResource', function($resource,
    CleanJSObjectService) {
    return $resource('/api/data-sets/:dataSetId/attachments/:fileName', {
      dataSetId: '@dataSetId',
      fileName: '@fileName'
    }, {
      'findByDataSetId': {
        method: 'GET',
        isArray: true
      },
      'save': {
        method: 'PUT',
        params: {
          dataSetId: '@dataSetId',
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
          dataSetId: '@dataSetId',
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

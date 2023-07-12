'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('InstrumentAttachmentResource', function($resource,
    CleanJSObjectService) {
    return $resource('/api/instruments/:instrumentId/attachments/:fileName', {
      instrumentId: '@instrumentId',
      fileName: '@fileName'
    }, {
      'findByInstrumentId': {
        method: 'GET',
        isArray: true
      },
      'save': {
        method: 'PUT',
        params: {
          instrumentId: '@instrumentId',
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
          instrumentId: '@instrumentId',
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

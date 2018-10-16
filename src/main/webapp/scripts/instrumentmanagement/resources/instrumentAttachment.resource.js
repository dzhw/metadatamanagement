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
        transformRequest: function(attachment) {
          var copy = angular.copy(attachment);
          CleanJSObjectService.deleteEmptyStrings(copy);
          CleanJSObjectService.removeEmptyJsonObjects(copy);
          return angular.toJson(copy);
        }
      }
    });
  });

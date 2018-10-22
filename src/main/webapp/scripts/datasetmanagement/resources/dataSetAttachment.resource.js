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
        transformRequest: function(attachment) {
          var copy = angular.copy(attachment);
          CleanJSObjectService.deleteEmptyStrings(copy);
          CleanJSObjectService.removeEmptyJsonObjects(copy);
          return angular.toJson(copy);
        }
      }
    });
  });

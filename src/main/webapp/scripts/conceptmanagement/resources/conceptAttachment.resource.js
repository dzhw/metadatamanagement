'use strict';

angular.module('metadatamanagementApp')
.factory('ConceptAttachmentResource',
  function($resource, CleanJSObjectService) {
      return $resource('/api/concepts/:conceptId/attachments/:fileName', {
        conceptId: '@conceptId',
        fileName: '@fileName'
      }, {
        'findByConceptId': {
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

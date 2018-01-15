'use strict';

/* Study Attachment Resource */
angular.module('metadatamanagementApp')
.factory('StudyAttachmentResource', function($resource, CleanJSObjectService) {
      return $resource('/api/studies/:studyId/attachments/:fileName', {
        studyId: '@studyId',
        fileName: '@fileName'
      }, {
        'findByStudyId': {
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

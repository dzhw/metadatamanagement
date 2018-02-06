'use strict';

/* Survey Attachment Resource */
angular.module('metadatamanagementApp')
  .factory('SurveyAttachmentResource', function($resource,
    CleanJSObjectService) {
    return $resource('/api/surveys/:surveyId/attachments/:fileName', {
      surveyId: '@surveyId',
      fileName: '@fileName'
    }, {
      'findBySurveyId': {
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

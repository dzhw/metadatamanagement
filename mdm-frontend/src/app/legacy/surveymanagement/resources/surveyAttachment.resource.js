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
        params: {
          surveyId: '@surveyId',
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
          surveyId: '@surveyId',
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

'use strict';

angular.module('metadatamanagementApp')
  .factory('SurveyAttachmentVersionsResource', function($resource) {
    return $resource('/api/surveys/:surveyId/attachments/:filename/versions', {
      studyId: '@surveyId',
      filename: '@filename'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  });

'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('SurveyAttachmentResource', function($resource) {
    return $resource('/api/surveys/:id/attachments', {
      id: '@id'
    }, {
      'findBySurveyId': {
        method: 'GET',
        isArray: true
      }
    });
  });

'use strict';

/* Instrument Resource */
angular.module('metadatamanagementApp')
  .factory('QuestionImageMetadataResource', function($resource) {
    return $resource('/api/questions/:id/images', {
      id: '@id'
    }, {
      'findByQuestionId': {
        method: 'GET',
        isArray: true
      }
    });
  });

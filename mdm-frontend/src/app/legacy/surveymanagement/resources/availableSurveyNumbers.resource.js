'use strict';

/* Available Survey Numbers Resource */
angular.module('metadatamanagementApp')
  .factory('AvailableSurveyNumbersResource', ['$resource',  function($resource) {
    return $resource(
      '/api/data-acquisition-projects/:id/available-survey-numbers', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);


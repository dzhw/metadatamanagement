'use strict';

angular.module('metadatamanagementApp')
  .factory('SurveyResource', function($resource) {
    return $resource('/api/surveys/:id', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        params: {
          projection: 'complete'
        }
      },
      'save': {
        method: 'PUT'
      },
      'delete': {
        method: 'DELETE'
      }
    });
  });

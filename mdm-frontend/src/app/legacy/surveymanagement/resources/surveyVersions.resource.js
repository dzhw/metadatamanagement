'use strict';

/* Survey Versions Resource */
angular.module('metadatamanagementApp')
  .factory('SurveyVersionsResource', ['$resource',  function($resource) {
    return $resource('/api/surveys/:id/versions', {
      id: '@id'
    }, {
      'get': {
        method: 'GET',
        isArray: true
      }
    });
  }]);


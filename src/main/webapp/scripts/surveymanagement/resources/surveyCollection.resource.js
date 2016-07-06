'use strict';
/* No use actually */
angular.module('metadatamanagementApp')
  .factory('SurveyCollectionResource', function($resource) {
    return $resource('/api/surveys', {
      projection: 'complete'
    }, {
      'query': {
        method: 'GET',
      }
    });
  });

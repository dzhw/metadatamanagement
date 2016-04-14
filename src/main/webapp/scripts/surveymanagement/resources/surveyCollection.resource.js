'use strict';

angular.module('metadatamanagementApp')
    .factory('SurveyCollection', function($resource) {
      return $resource('/api/surveys',
        {projection: 'complete'}, {
        'query': {
          method: 'GET',
        }
      });
    });

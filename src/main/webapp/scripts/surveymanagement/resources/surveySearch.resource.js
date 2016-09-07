'use strict';

angular.module('metadatamanagementApp').factory('SurveySearchResource',
function($resource) {
  return $resource('api/surveys/search', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      url: 'api/surveys/search/findByIdIn',
      params: {
        projection: 'surveyTextOnly',
        ids: '@ids'
      }
    }
  });
});

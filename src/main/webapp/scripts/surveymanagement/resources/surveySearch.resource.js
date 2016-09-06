'use strict';

angular.module('metadatamanagementApp').factory('SurveySearchResource',
function($resource) {
  return $resource('api/surveys/search/findByIdIn', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      params: {
        projection: 'surveyTextOnly',
        ids: '@ids'
      }
    }
  });
});

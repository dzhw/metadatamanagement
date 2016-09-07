'use strict';

angular.module('metadatamanagementApp').factory('QuestionSearchResource',
function($resource) {
  return $resource('api/questions/search', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      url: 'api/questions/search/findByIdIn',
      params: {
        projection: 'questionTextOnly',
        ids: '@ids'
      }
    }
  });
});

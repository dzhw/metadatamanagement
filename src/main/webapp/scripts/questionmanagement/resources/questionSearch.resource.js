'use strict';

angular.module('metadatamanagementApp').factory('QuestionSearchResource',
function($resource) {
  return $resource('api/questions/search/findByIdIn', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      params: {
        projection: 'questionTextOnly',
        ids: '@ids'
      }
    }
  });
});

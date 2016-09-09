'use strict';

angular.module('metadatamanagementApp').factory('QuestionReferencedResource',
function($resource) {
  return $resource('api/questions/search', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      url: 'api/questions/search/findByIdIn',
      params: {
        projection: 'referenced',
        ids: '@ids'
      }
    }
  });
});

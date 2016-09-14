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
    },
    'findByDataAcquisitionProjectId':
    {
      method: 'GET',
      url: 'api/questions/search/findByDataAcquisitionProjectId',
      params: {
        projection: 'referenced',
        ids: '@ids'
      }
    },
    'findBySuccessorsContaining': {
      method: 'GET',
      url: 'api/questions/search/findBySuccessorsContaining',
      params: {
        projection: 'referenced',
        id: '@id'
      }
    }
  });
});

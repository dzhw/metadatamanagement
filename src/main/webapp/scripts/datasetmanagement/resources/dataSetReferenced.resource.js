'use strict';

angular.module('metadatamanagementApp').factory('DataSetReferencedResource',
function($resource) {
  return $resource('api/data-sets/search', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      url: 'api/data-sets/search/findByIdIn',
      params: {
        projection: 'referenced',
        ids: '@ids'
      }
    },
    'findByVariableIdsContaining':
    {
      method: 'GET',
      url: 'api/data-sets/search/findByVariableIdsContaining',
      params: {
        projection: 'referenced',
        id: '@id'
      }
    }
  });
});

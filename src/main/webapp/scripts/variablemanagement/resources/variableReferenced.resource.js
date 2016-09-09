'use strict';

angular.module('metadatamanagementApp').factory('VariableReferencedResource',
function($resource) {
  return $resource('api/variables/search', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      url: 'api/variables/search/findByIdIn',
      params: {
        projection: 'referenced',
        ids: '@ids'
      }
    }
  });
});

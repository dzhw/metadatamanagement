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
    }
  });
});

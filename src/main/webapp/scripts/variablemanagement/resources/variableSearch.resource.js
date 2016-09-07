'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchResource',
function($resource) {
  return $resource('api/variables/search', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      url: 'api/variables/search/findByIdIn',
      params: {
        projection: 'variableTextOnly',
        ids: '@ids'
      }
    }
  });
});

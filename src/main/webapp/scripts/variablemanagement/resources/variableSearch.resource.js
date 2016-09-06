'use strict';

angular.module('metadatamanagementApp').factory('VariableSearchResource',
function($resource) {
  return $resource('api/variables/search/findByIdIn', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      params: {
        projection: 'variableTextOnly',
        ids: '@ids'
      }
    }
  });
});

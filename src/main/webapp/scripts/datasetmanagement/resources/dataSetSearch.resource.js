'use strict';

angular.module('metadatamanagementApp').factory('DataSetSearchResource',
function($resource) {
  return $resource('api/data-sets/search', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      url: 'api/data-sets/search/findByIdIn',
      params: {
        projection: 'dataSetTextOnly',
        ids: '@ids'
      }
    }
  });
});

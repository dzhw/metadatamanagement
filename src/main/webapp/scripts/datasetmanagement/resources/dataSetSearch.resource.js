'use strict';

angular.module('metadatamanagementApp').factory('DataSetSearchResource',
function($resource) {
  return $resource('api/data-sets/search/findByIdIn', {},
  {
    'findByIdIn':
    {
      method: 'GET',
      params: {
        projection: 'dataSetTextOnly',
        ids: '@ids'
      }
    }
  });
});

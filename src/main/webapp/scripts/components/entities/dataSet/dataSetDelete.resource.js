'use strict';

angular.module('metadatamanagementApp')
.factory('DataSetDeleteResource', function($resource) {
  return $resource(
    'api/data_sets/delete',
    {}, {'deleteByDataAcquisitionProjectId': {
      method: 'POST',
      params: {dataAcquisitionProjectId: '@dataAcquisitionProjectId'}
    }
  });
});

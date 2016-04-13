'use strict';

angular.module('metadatamanagementApp')
.factory('VariableDeleteResource', function($resource) {
  return $resource(
    'api/variables/delete',
    {},
    {'deleteByDataAcquisitionProjectId': {
      method: 'POST',
      params: {dataAcquisitionProjectId: '@dataAcquisitionProjectId'}
    }
  });
});

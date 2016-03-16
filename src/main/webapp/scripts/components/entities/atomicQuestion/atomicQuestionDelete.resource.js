'use strict';

angular.module('metadatamanagementApp')
.factory('AtomicQuestionDeleteResource', function($resource) {
  return $resource(
    'api/atomic_questions/delete',
    {}, {'deleteByDataAcquisitionProjectId': {
      method: 'POST',
      params: {dataAcquisitionProjectId: '@dataAcquisitionProjectId'}
    }
  });
});

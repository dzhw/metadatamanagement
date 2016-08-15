'use strict';

angular.module('metadatamanagementApp')
  .factory('QuestionDeleteResource', function($resource) {
    return $resource(
      'api/questions/delete', {}, {
        'deleteByDataAcquisitionProjectId': {
          method: 'POST',
          params: {
            dataAcquisitionProjectId: '@dataAcquisitionProjectId'
          }
        }
      });
  });

'use strict';

angular.module('metadatamanagementApp')
  .factory('StudyDeleteResource', function($resource) {
    return $resource(
      'api/studies/delete', {}, {
        'deleteByDataAcquisitionProjectId': {
          method: 'POST',
          params: {
            dataAcquisitionProjectId: '@dataAcquisitionProjectId'
          }
        }
      });
  });
